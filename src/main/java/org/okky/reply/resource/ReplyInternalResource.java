package org.okky.reply.resource;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.reply.domain.repository.ReplyRepository;
import org.okky.reply.domain.repository.dto.ReplyReducedDto;
import org.okky.share.execption.ResourceNotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/internal")
@RestController
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
class ReplyInternalResource {
    ReplyRepository repository;

    @GetMapping(value = "/articles/{articleId}/repliers", produces = APPLICATION_JSON_VALUE)
    List<String> findRepliersById(
            @PathVariable String articleId,
            @PageableDefault(size = 200) Pageable pageable) {
        Page<String> page = repository.findRepliersByArticleId(articleId, pageable);
        return page.getContent();
    }

    @GetMapping(value = "/replies/{replyId}", produces = APPLICATION_JSON_VALUE)
    ReplyReducedDto find(@PathVariable String replyId) {
        return repository
                .findDtoById(replyId)
                .orElseThrow(ResourceNotFound::new);
    }
}
