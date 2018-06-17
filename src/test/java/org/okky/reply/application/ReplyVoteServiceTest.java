package org.okky.reply.application;

import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.okky.reply.TestMother;
import org.okky.reply.application.command.ToggleVoteCommand;
import org.okky.reply.domain.model.ReplyVote;
import org.okky.reply.domain.repository.ReplyVoteRepository;
import org.okky.reply.domain.service.ReplyConstraint;
import org.okky.reply.domain.service.ReplyVoteConstraint;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = PRIVATE)
public class ReplyVoteServiceTest extends TestMother {
    @InjectMocks
    ReplyVoteService service;
    @Mock
    ReplyVoteRepository repository;
    @Mock
    ReplyConstraint replyConstraint;
    @Mock
    ReplyVoteConstraint replyVoteConstraint;
    @Mock
    ReplyVote vote;

    /**
     * 예를 들어 'UP'인 상태에서 'UP' toggle 요청이 들어오면 삭제
     */
    @Test
    public void toggleVote_투표를_이미_했고_같은_방향인_경우_삭제() {
        ToggleVoteCommand cmd = new ToggleVoteCommand("r", "v", "UP");
        when(vote.isSameDirection(any())).thenReturn(true);
        when(repository.wasAlreadyVoted("r", "v")).thenReturn(true);
        when(repository.find("r", "v")).thenReturn(Optional.of(vote));

        service.toggleVote(cmd);

        InOrder o = inOrder(repository, replyConstraint, replyVoteConstraint, vote);
        o.verify(replyConstraint).checkExists("r");
        o.verify(replyVoteConstraint).checkVoterExists("v");
        o.verify(repository).delete(vote);
    }

    /**
     * 예를 들어 'UP'인 상태에서 'DOWN' toggle 요청이 들어오면 방향만 수정
     */
    @Test
    public void toggleVote_투표를_이미_했고_다른_방향인_경우_삭제() {
        ToggleVoteCommand cmd = new ToggleVoteCommand("r", "v", "UP");
        when(vote.isSameDirection(any())).thenReturn(false);
        when(repository.wasAlreadyVoted("r", "v")).thenReturn(true);
        when(repository.find("r", "v")).thenReturn(Optional.of(vote));

        service.toggleVote(cmd);

        InOrder o = inOrder(repository, replyConstraint, replyVoteConstraint, vote);
        o.verify(replyConstraint).checkExists("r");
        o.verify(replyVoteConstraint).checkVoterExists("v");
        o.verify(vote).reverseDirection();
    }

    @Test
    public void toggleVote_투표를_아직_하지_않은_경우는_단순히_투표() {
        ToggleVoteCommand cmd = new ToggleVoteCommand("r", "v", "DOWN");
        when(repository.wasAlreadyVoted("r", "v")).thenReturn(false);

        service.toggleVote(cmd);

        InOrder o = inOrder(repository, replyConstraint, replyVoteConstraint);
        o.verify(replyConstraint).checkExists("r");
        o.verify(replyVoteConstraint).checkVoterExists("v");
        o.verify(repository).save(isA(ReplyVote.class));
    }
}