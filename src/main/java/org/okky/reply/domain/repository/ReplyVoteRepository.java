package org.okky.reply.domain.repository;


import org.okky.reply.domain.model.ReplyVote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@RepositoryDefinition(domainClass = ReplyVote.class, idClass = String.class)
public interface ReplyVoteRepository {
    void save(ReplyVote vote);
    @Query("from ReplyVote v " +
            "where v.replyId=:replyId " +
            "and v.voterId=:voterId ")
    Optional<ReplyVote> find(@Param("replyId") String replyId, @Param("voterId") String voterId);
    void delete(ReplyVote vote);
}
