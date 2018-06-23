package org.okky.reply.domain.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.reply.domain.model.Reply;
import org.okky.reply.domain.repository.ReplyRepository;
import org.okky.reply.resource.ContextHolder;
import org.okky.share.execption.ExternalServiceError;
import org.okky.share.execption.ModelConflicted;
import org.okky.share.execption.ModelNotExists;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReplyConstraint {
    ReplyRepository repository;
    RestTemplate template;
    ContextHolder holder;

    public void checkExists(String replyId) {
        checkExistsAndGet(replyId);
    }

    public Reply checkExistsAndGet(String replyId) {
        return repository
                .findById(replyId)
                .orElseThrow(() -> new ModelNotExists(format("해당 답글은 존재하지 않습니다: '%s'", replyId)));
    }

    public void checkArticleExists(String articleId) {
        try {
            ResponseEntity<Boolean> result = template.getForEntity(format("/articles/%s/exists", articleId), Boolean.class);
            if (!result.getBody())
                throw new ModelNotExists(format("해당 게시글은 존재하지 않기 때문에 답글을 달 수 없습니다: '%s'", articleId));
        } catch (HttpStatusCodeException e) {
            throw new ExternalServiceError(e.getResponseBodyAsByteArray());
        }
    }

    public void rejectWriteIfArticleBlocked(String articleId) {
        try {
            ResponseEntity<Boolean> result = template.getForEntity(format("/articles/%s/blocked", articleId), Boolean.class);
            if (result.getBody())
                throw new ModelConflicted(format("해당 게시글은 블락된 상태이기 때문에 답글을 달 수 없습니다: '%s'", articleId));
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == NOT_FOUND)
                throw new ModelNotExists(format("해당 게시글은 존재하지 않습니다: '%s'", articleId));
            else
                throw new ExternalServiceError(e.getResponseBodyAsByteArray());
        }
    }

    public void rejectIfWriterNotMatched(String articleId) {
        try {
            ResponseEntity<Boolean> result = template.getForEntity(format("/articles/%s/writers/%s/match", articleId, holder.getId()), Boolean.class);
            if (!result.getBody())
                throw new ModelConflicted(format("답글 고정/해제는 오로지 게시글 작성자만 가능합니다: '%s'", articleId));
        } catch (HttpStatusCodeException e) {
            throw new ExternalServiceError(e.getResponseBodyAsByteArray());
        }
    }
}
