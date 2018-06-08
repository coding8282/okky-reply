package org.okky.reply.domain.event;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.reply.domain.repository.ReplyRepository;
import org.okky.reply.domain.service.ReplyProxy;
import org.okky.share.event.ArticleRemoved;
import org.okky.share.event.RepliesRemoved;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
class ArticleRemovedEventProcessor {
    ReplyRepository repository;
    ReplyProxy proxy;

    @EventListener
    void when(ArticleRemoved event) {
        List<String> ids = repository.findIdsByArticleId(event.getArticleId());
        repository.deleteByArticleId(event.getArticleId());
        proxy.sendMessage(new RepliesRemoved(ids));
    }
}
