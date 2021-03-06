package org.okky.reply.domain.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.okky.share.event.DomainEvent;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import static java.lang.Class.forName;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.ON_SUCCESS;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
class DomainEventPump {
    ObjectMapper mapper;

    @SqsListener(value = "${app.queue.reply}", deletionPolicy = ON_SUCCESS)
    @SneakyThrows
    void receive(String json) {
        String message = mapper.readTree(json).get("Message").asText();
        String eventName = mapper.readTree(message).get("eventName").asText();
        DomainEvent event = (DomainEvent) mapper.readValue(message, forName(eventName));
        DomainEventPublisher.fire(event);
    }
}
