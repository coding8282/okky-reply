package org.okky.reply.domain.service;

import lombok.experimental.FieldDefaults;
import org.okky.share.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;
import static org.okky.share.util.JsonUtil.toJson;

@Service
@FieldDefaults(level = PRIVATE)
public class ReplyProxy {
    @Autowired
    NotificationMessagingTemplate snsClient;

    @Value("${app.topic.reply-wrote}")
    String topicReplyWrote;
    @Value("${app.topic.reply-removed}")
    String topicReplyRemoved;
    @Value("${app.topic.replies-removed}")
    String topicRepliesRemoved;
    @Value("${app.topic.reply-pinned}")
    String topicReplyPinned;

    public void sendEvent(ReplyWrote event) {
        send(topicReplyWrote, event);
    }

    public void sendEvent(ReplyRemoved event) {
        send(topicReplyRemoved, event);
    }

    public void sendEvent(RepliesRemoved event) {
        send(topicRepliesRemoved, event);
    }

    public void sendEvent(ReplyPinned event) {
        send(topicReplyPinned, event);
    }

    // ------------------------------------
    private void send(String topic, DomainEvent event) {
        snsClient.sendNotification(topic, toJson(event), null);
    }
}
