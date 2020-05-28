package com.example.cloudrundemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class CloudRunDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudRunDemoApplication.class, args);
    }

    @RestController
    static class GreetingController {

        @Value("${TARGET:World}")
        String target;

        @GetMapping("/")
        String greet() {
            return "Hello " + target + "!!!";
        }
    }

    @RestController
    static class MessageController {

        private static Logger logger = LoggerFactory.getLogger(MessageController.class);

        @PostMapping("/messages")
        public void process(@RequestBody String message) {
            logger.info("Processed message {}", message);
        }

    }
}
