package org.okky.reply.domain.event;

import lombok.experimental.FieldDefaults;
import org.okky.share.event.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE)
public class DomainEventPublisher {
    static ApplicationEventPublisher publisher;

    public static void fire(DomainEvent event) {
        publisher.publishEvent(event);
    }

    @Autowired
    private void setPublisher(ApplicationEventPublisher publisher) {
        DomainEventPublisher.publisher = publisher;
    }
}
