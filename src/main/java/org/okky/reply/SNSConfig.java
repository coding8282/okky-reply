package org.okky.reply;

import com.amazonaws.services.sns.AmazonSNS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SNSConfig {
    @Value("${app.reply-topic}")
    String topic;

    @Bean
    NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSNS) {
        NotificationMessagingTemplate template = new NotificationMessagingTemplate(amazonSNS);
        template.setDefaultDestinationName(topic);
        return template;
    }
}
