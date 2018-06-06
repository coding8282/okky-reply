package org.okky.reply.domain.repository;

import org.okky.reply.domain.model.Reply;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.Optional;

@RepositoryDefinition(domainClass = Reply.class, idClass = String.class)
public interface ReplyRepository extends RevisionRepository<Reply, String, Long> {
    void save(Reply reply);
    Optional<Reply> findById(String id);
    boolean existsById(String id);
    boolean existsByArticleId(String articleId);
    long countByReplierId(String replierId);
    long countByArticleId(String articleId);
    void delete(Reply reply);
}
