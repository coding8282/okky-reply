package org.okky.reply.application;

import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.okky.reply.TestMother;
import org.okky.reply.application.command.ModifyReplyCommand;
import org.okky.reply.application.command.WriteReplyCommand;
import org.okky.reply.domain.event.DomainEventPublisher;
import org.okky.reply.domain.model.Reply;
import org.okky.reply.domain.repository.ReplyRepository;
import org.okky.reply.domain.service.ReplyConstraint;
import org.okky.reply.domain.service.ReplyProxy;
import org.okky.share.event.ReplyWrote;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DomainEventPublisher.class)
@FieldDefaults(level = PRIVATE)
public class ReplyServiceTest extends TestMother {
    @InjectMocks
    ReplyService service;
    @Mock
    ReplyRepository repository;
    @Mock
    ReplyConstraint constraint;
    @Mock
    ReplyProxy proxy;
    @Mock
    ModelMapper mapper;
    @Mock
    Reply reply;
    @Mock
    ReplyWrote event;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(DomainEventPublisher.class);
    }

    @Test
    public void write() {
        WriteReplyCommand cmd = new WriteReplyCommand("a", "r", "rn", "b");
        when(mapper.toModel(cmd)).thenReturn(reply);
        when(mapper.toEvent(reply)).thenReturn(event);
        PowerMockito.doNothing().when(DomainEventPublisher.class);
        DomainEventPublisher.fire(ArgumentMatchers.any());

        service.write(cmd);

        InOrder o = inOrder(constraint, mapper, repository);
        o.verify(constraint).checkArticleExists("a");
        o.verify(constraint).rejectWriteIfArticleBlocked("a");
        o.verify(mapper).toModel(cmd);
        o.verify(repository).save(reply);
        o.verify(mapper).toEvent(reply);

        PowerMockito.verifyStatic(DomainEventPublisher.class);
        DomainEventPublisher.fire(ArgumentMatchers.any());
    }

    @Test
    public void modify() {
        ModifyReplyCommand cmd = new ModifyReplyCommand("r", "b");
        when(constraint.checkExistsAndGet("r")).thenReturn(reply);

        service.modify(cmd);

        InOrder o = inOrder(constraint, reply);
        o.verify(constraint).checkExistsAndGet("r");
        o.verify(reply).modify("b");
    }

    @Test
    public void toggleAccept() {
        when(constraint.checkExistsAndGet("r")).thenReturn(reply);

        service.toggleAccept("r");

        InOrder o = inOrder(constraint, reply);
        o.verify(constraint).checkExistsAndGet("r");
        o.verify(reply).toggleAccept();
    }

    @Test
    public void remove() {
        when(constraint.checkExistsAndGet("r")).thenReturn(reply);
        PowerMockito.doNothing().when(DomainEventPublisher.class);
        DomainEventPublisher.fire(ArgumentMatchers.any());

        service.remove("r");

        InOrder o = inOrder(constraint, repository);
        o.verify(constraint).checkExistsAndGet("r");
        o.verify(repository).delete(reply);

        PowerMockito.verifyStatic(DomainEventPublisher.class);
        DomainEventPublisher.fire(ArgumentMatchers.any());
    }

    @Test
    public void removeForce() {
        // TODO: 2018. 6. 17. 로직이 중복되어 있는 다른 메소드 콜을 검증할 수 없을까?
        when(constraint.checkExistsAndGet("r")).thenReturn(reply);
        PowerMockito.doNothing().when(DomainEventPublisher.class);
        DomainEventPublisher.fire(ArgumentMatchers.any());

        service.removeForce("r");

        InOrder o = inOrder(constraint, repository, proxy);
        o.verify(constraint).checkExistsAndGet("r");
        o.verify(repository).delete(reply);

        PowerMockito.verifyStatic(DomainEventPublisher.class);
        DomainEventPublisher.fire(ArgumentMatchers.any());
    }

    @Test
    public void togglePin_이미_고정된_답글인_경우_단순히_해제() {
        when(constraint.checkExistsAndGet("r")).thenReturn(reply);
        when(reply.getArticleId()).thenReturn("a");
        when(reply.pinned()).thenReturn(true);

        service.togglePin("r", "m");

        InOrder o = inOrder(constraint, reply, repository);
        o.verify(constraint).checkExistsAndGet("r");
        o.verify(reply).getArticleId();
        o.verify(constraint).rejectIfWriterNotMatched("a");
        o.verify(reply).pinned();
        o.verify(reply).unpin();
        o.verify(reply, never()).pin("m");
        o.verify(repository, never()).findPinned(anyString());
    }

    @Test
    public void togglePin_고정되지_않은_답글이고_기존에_고정되어_있는_답글_또한_없으면_새_답글을_고정() {
        when(constraint.checkExistsAndGet("r")).thenReturn(reply);
        when(reply.getArticleId()).thenReturn("a");
        when(reply.pinned()).thenReturn(false);
        when(repository.findPinned("a")).thenReturn(Optional.empty());

        service.togglePin("r", "m");

        InOrder o = inOrder(constraint, repository, reply);
        o.verify(constraint).checkExistsAndGet("r");
        o.verify(reply).getArticleId();
        o.verify(constraint).rejectIfWriterNotMatched("a");
        o.verify(reply).pinned();
        o.verify(repository).findPinned("a");
        o.verify(reply).pin("m");
        o.verify(reply, never()).unpin();
    }

    @Test
    public void togglePin_고정되지_않은_답글이고_기존_답글이_고정되어_있다면_기존_답글은_고정_해제하고_새_답글은_고정() {
        Reply previousPinnedReply = mock(Reply.class);
        when(constraint.checkExistsAndGet("r")).thenReturn(reply);
        when(reply.getArticleId()).thenReturn("a");
        when(reply.pinned()).thenReturn(false);
        when(repository.findPinned("a")).thenReturn(Optional.of(previousPinnedReply));

        service.togglePin("r", "m");

        InOrder o = inOrder(constraint, repository, reply, previousPinnedReply);
        o.verify(constraint).checkExistsAndGet("r");
        o.verify(reply).getArticleId();
        o.verify(constraint).rejectIfWriterNotMatched("a");
        o.verify(reply).pinned();
        o.verify(repository).findPinned("a");
        o.verify(previousPinnedReply).unpin();
        o.verify(reply).pin("m");
        o.verify(reply, never()).unpin();
    }
}