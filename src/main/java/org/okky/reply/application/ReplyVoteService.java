package org.okky.reply.application;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.reply.application.command.ToggleVoteCommand;
import org.okky.reply.domain.model.ReplyVote;
import org.okky.reply.domain.model.VotingDirection;
import org.okky.reply.domain.repository.ReplyVoteRepository;
import org.okky.reply.domain.service.ReplyConstraint;
import org.okky.reply.domain.service.ReplyVoteConstraint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReplyVoteService {
    ReplyVoteRepository repository;
    ReplyConstraint replyConstraint;
    ReplyVoteConstraint replyVoteConstraint;

    public void toggleVote(ToggleVoteCommand cmd) {
        String replyId = cmd.getReplyId();
        String voterId = cmd.getVoterId();
        VotingDirection direction = VotingDirection.parse(cmd.getDirection());

        replyConstraint.checkExists(replyId);
        replyVoteConstraint.checkVoterExists(voterId);
        boolean alreadyVoted = repository.wasAlreadyVoted(replyId, voterId);
        if (alreadyVoted) {
            ReplyVote vote = repository.find(replyId, voterId).get();
            if (vote.isSameDirection(direction))
                unvote(vote);
            else
                vote.reverseDirection();
        } else {
            vote(replyId, voterId, direction);
        }
    }

    // --------------------------------------------
    private void vote(String replyId, String voterId, VotingDirection direction) {
        ReplyVote vote = new ReplyVote(replyId, voterId, direction);
        repository.save(vote);
    }

    private void unvote(ReplyVote vote) {
        repository.delete(vote);
    }
}
