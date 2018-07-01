package org.okky.reply.domain.repository;

import org.okky.reply.domain.model.Reply;
import org.okky.reply.domain.repository.dto.ReplyReducedDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@RepositoryDefinition(domainClass = Reply.class, idClass = String.class)
public interface ReplyRepository extends RevisionRepository<Reply, String, Long> {
    boolean existsById(String id);
    boolean existsByArticleId(String articleId);
    void save(Reply reply);
    Optional<Reply> findById(String id);
    @Query("select new org.okky.reply.domain.repository.dto.ReplyReducedDto(r) from Reply r where r.id=:id")
    Optional<ReplyReducedDto> findDtoById(@Param("id") String id);
    @Query("select r.id from Reply r where r.articleId=:articleId")
    List<String> findIdsByArticleId(@Param("articleId") String articleId);
    @Query("select distinct r.replierId from Reply r where r.articleId=:articleId order by r.replierId desc")
    Page<String> findRepliersByArticleId(@Param("articleId") String articleId, Pageable pageable);
    @Query("select r from Reply r where r.articleId=:articleId and r.pinDetail is not null")
    Optional<Reply> findPinned(@Param("articleId") String articleId);
    long countByReplierId(String replierId);
    long countByArticleId(String articleId);
    void delete(Reply reply);
    void deleteByArticleId(String articleId);
}
