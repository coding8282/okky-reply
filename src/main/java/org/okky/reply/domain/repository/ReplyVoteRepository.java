package org.okky.reply.domain.repository;


import org.okky.reply.domain.model.ReplyVote;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = ReplyVote.class, idClass = String.class)
public interface ReplyVoteRepository {
    void save(ReplyVote vote);
    boolean existsByReplyIdAndVoterId(String replyId, String voterId);
    Optional<ReplyVote> findByReplyIdAndVoterId(String replyId, String voterId);
    void delete(ReplyVote vote);

    default Optional<ReplyVote> find(String replyId, String voterId) {
        return findByReplyIdAndVoterId(replyId, voterId);
    }
    default boolean wasAlreadyVoted(String replyId, String voterId) {
        return existsByReplyIdAndVoterId(replyId, voterId);
    }
}
