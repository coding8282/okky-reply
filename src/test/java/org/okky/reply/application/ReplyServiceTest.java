package org.okky.reply.application;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.okky.reply.TestMother;
import org.okky.reply.application.command.ModifyReplyCommand;
import org.okky.reply.application.command.WriteReplyCommand;
import org.okky.reply.domain.model.Reply;
import org.okky.reply.domain.repository.ReplyRepository;
import org.okky.reply.domain.service.ReplyConstraint;
import org.okky.reply.domain.service.ReplyProxy;
import org.okky.share.event.ReplyRemoved;
import org.okky.share.event.ReplyWrote;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void write() {
        WriteReplyCommand cmd = new WriteReplyCommand("a", "r", "rn", "b");
        when(mapper.toModel(cmd)).thenReturn(reply);
        when(mapper.toEvent(reply)).thenReturn(event);

        service.write(cmd);

        InOrder o = inOrder(constraint, mapper, repository, proxy);
        o.verify(constraint).checkArticleExists("a");
        o.verify(constraint).rejectWriteIfArticleBlocked("a");
        o.verify(mapper).toModel(cmd);
        o.verify(repository).save(reply);
        o.verify(mapper).toEvent(reply);
        o.verify(proxy).sendEvent(event);
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

        service.remove("r");

        InOrder o = inOrder(constraint, repository, proxy);
        o.verify(constraint).checkExistsAndGet("r");
        o.verify(repository).delete(reply);
        o.verify(proxy).sendEvent(isA(ReplyRemoved.class));
    }

    @Test
    public void removeForce() {
        // TODO: 2018. 6. 17. 로직이 중복되어 있는 다른 메소드 콜을 검증할 수 없을까?
        when(constraint.checkExistsAndGet("r")).thenReturn(reply);

        service.removeForce("r");

        InOrder o = inOrder(constraint, repository, proxy);
        o.verify(constraint).checkExistsAndGet("r");
        o.verify(repository).delete(reply);
        o.verify(proxy).sendEvent(isA(ReplyRemoved.class));
    }
}