package com.example.cloudpubsubdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class CloudPubSubDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudPubSubDemoApplication.class, args);
    }

    @RestController
    static class MessageController {

        private static final String TOPIC = "greetings";
        private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
        private final PubSubTemplate pubSubTemplate;

        public MessageController(PubSubTemplate pubSubTemplate) {
            this.pubSubTemplate = pubSubTemplate;
        }

        @PostMapping("/messages")
        public String sendMessage(@RequestBody String message) {
            logger.info("Message2 received: {}", message);
            pubSubTemplate.publish(TOPIC, message);
            return "Your message was published asynchronously, status is unknown";
        }
    }
}
