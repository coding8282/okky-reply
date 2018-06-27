package org.okky.reply.resource;

import lombok.AllArgsConstructor;
import org.okky.reply.application.ReplyService;
import org.okky.reply.application.command.ModifyReplyCommand;
import org.okky.reply.application.command.WriteReplyCommand;
import org.okky.reply.domain.repository.ReplyMapper;
import org.okky.reply.domain.repository.ReplyRepository;
import org.okky.reply.domain.repository.dto.ReplyDto;
import org.okky.share.PagingEnvelop;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
class ReplyResource {
    ReplyService service;
    ReplyMapper mapper;
    ReplyRepository repository;
    ContextHolder holder;

    @GetMapping(value = "/replies/{replyId}/exists")
    boolean exists(@PathVariable String replyId) {
        return repository.existsById(replyId);
    }

    @GetMapping(value = "/articles/{articleId}/replies/exists")
    boolean existsReplies(@PathVariable String articleId) {
        return repository.existsByArticleId(articleId);
    }

    @GetMapping(value = "/articles/{articleId}/replies/pinned", produces = APPLICATION_JSON_VALUE)
    ReplyDto getPinned(@PathVariable String articleId) {
        return mapper.selectPinned(articleId);
    }

    @GetMapping(value = "/articles/{articleId}/replies", produces = APPLICATION_JSON_VALUE)
    PagingEnvelop getByArticle(
            @PathVariable String articleId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("myId", holder.getId());
        params.put("articleId", articleId);
        params.put("offset", (pageNo - 1) * pageSize);
        params.put("limit", pageSize);

        List<ReplyDto> dtos = mapper.select(params);
        long totalCount = mapper.count(params);
        return new PagingEnvelop(pageSize, dtos, totalCount);
    }

    @GetMapping(value = "/members/{memberId}/replies/count")
    long getReplyCountOfMember(@PathVariable String memberId) {
        return repository.countByReplierId(memberId);
    }

    @GetMapping(value = "/articles/{articleIds}/replies/count", produces = APPLICATION_JSON_VALUE)
    List<Long> getReplyCountsOfArticles(@PathVariable List<String> articleIds) {
        return articleIds
                .stream()
                .map(articleId -> repository.countByArticleId(articleId))
                .collect(toList());
    }

    @PostMapping(value = "/articles/{articleId}/replies", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    void write(
            @PathVariable String articleId,
            @RequestBody WriteReplyCommand cmd) {
        cmd.setReplierId(holder.getId());
        cmd.setArticleId(articleId);
        service.write(cmd);
    }

    @PutMapping(value = "/replies/{replyId}/acceptances/toggle", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    void toggleAccept(@PathVariable String replyId) {
        service.toggleAccept(replyId);
    }

    @PutMapping(value = "/replies/{replyId}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    void update(@PathVariable String replyId, @RequestBody ModifyReplyCommand cmd) {
        cmd.setReplyId(replyId);
        service.modify(cmd);
    }

    @PutMapping(value = "/replies/{replyId}/pin/toggle")
    @ResponseStatus(NO_CONTENT)
    void togglePin(@PathVariable String replyId, String memo) {
        service.togglePin(replyId, memo);
    }

    @DeleteMapping(value = "/replies/{replyId}")
    @ResponseStatus(NO_CONTENT)
    void remove(@PathVariable String replyId) {
        service.remove(replyId);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/replies/{replyId}/force")
    @ResponseStatus(NO_CONTENT)
    void removeForce(@PathVariable String replyId) {
        service.removeForce(replyId);
    }
}
