package com.example.cloudpubsubdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class CloudPubsubDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudPubsubDemoApplication.class, args);
    }

    @RestController
    static class MessageController {

        private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

        @PostMapping("/")
        public void send(@RequestBody String message) {
            logger.info("Message2 received: {}", message);
        }
    }
}
