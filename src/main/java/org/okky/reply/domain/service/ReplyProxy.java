package org.okky.reply.domain.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.share.TopicUtil;
import org.okky.share.event.DomainEvent;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;
import static org.okky.share.util.JsonUtil.toJson;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReplyProxy {
    NotificationMessagingTemplate snsClient;

    public void sendEvent(DomainEvent event) {
        String topic = TopicUtil.toTopicName(event);
        String message = toJson(event);
        String subject = null;
        snsClient.sendNotification(topic, message, subject);
    }
}
