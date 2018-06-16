package org.okky.reply.application;

import lombok.AllArgsConstructor;
import org.okky.reply.application.command.ToggleVoteCommand;
import org.okky.reply.domain.model.ReplyVote;
import org.okky.reply.domain.model.Voting;
import org.okky.reply.domain.repository.ReplyVoteRepository;
import org.okky.reply.domain.service.ReplyConstraint;
import org.okky.reply.domain.service.ReplyVoteConstraint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ReplyVoteService {
    private ReplyVoteRepository repository;
    private ReplyConstraint replyConstraint;
    private ReplyVoteConstraint replyVoteConstraint;

    public void toggleVote(ToggleVoteCommand cmd) {
        String replyId = cmd.getReplyId();
        String voterId = cmd.getVoterId();
        Voting voting = Voting.parse(cmd.getVoting());

        replyConstraint.checkExists(replyId);
        replyVoteConstraint.checkVoterExists(voterId);
        ReplyVote vote = repository.find(replyId, voterId).orElse(null);
        if (vote == null) {
            vote(replyId, voterId, voting);
        } else {
            if (vote.isSameDirection(voting)) {
                unVote(vote);
            } else {
                vote.reverseDirection();
            }
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
