package org.okky.reply.application;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.reply.application.command.ToggleVoteCommand;
import org.okky.reply.domain.model.ReplyVote;
import org.okky.reply.domain.model.Voting;
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
        Voting voting = Voting.parse(cmd.getVoting());

        replyConstraint.checkExists(replyId);
        replyVoteConstraint.checkVoterExists(voterId);
        boolean alreadyVoted = repository.wasAlreadyVoted(replyId, voterId);
        if (alreadyVoted) {
            ReplyVote vote = repository.find(replyId, voterId).get();
            if (vote.isSameDirection(voting)) {
                unVote(vote);
            } else {
                vote.reverseDirection();
            }
        } else {
            vote(replyId, voterId, voting);
        }
    }

    // --------------------------------------------
    private void vote(String replyId, String voterId, Voting voting) {
        ReplyVote vote = new ReplyVote(replyId, voterId, voting);
        repository.save(vote);
    }

    private void unVote(ReplyVote vote) {
        repository.delete(vote);
    }
}
