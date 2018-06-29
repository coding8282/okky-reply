package org.okky.reply.domain.event;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.okky.reply.domain.repository.ReplyRepository;
import org.okky.reply.domain.service.ReplyProxy;
import org.okky.share.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
class DomainEventProcessor {
    ReplyRepository repository;
    ReplyProxy proxy;

    @EventListener
    void when(ReplyPinned event) {
        proxy.sendEvent(event);
    }

    @EventListener
    void when(ReplyWrote event) {
        proxy.sendEvent(event);
    }

    @EventListener
    void when(ReplyRemoved event) {
        proxy.sendEvent(event);
    }

    @EventListener
    void when(ArticleRemoved event) {
        List<String> ids = repository.findIdsByArticleId(event.getArticleId());
        repository.deleteByArticleId(event.getArticleId());
        proxy.sendEvent(new RepliesRemoved(ids));
    }
}

@Aspect
@Component
@Slf4j
class LogAspect {
    @After("execution(void org.okky.reply.domain.event.DomainEventProcessor.when(..))")
    void logging(JoinPoint jp) {
        String event = jp.getArgs()[0].getClass().getSimpleName();

        logger.info("Event: {}", event);
    }
}


