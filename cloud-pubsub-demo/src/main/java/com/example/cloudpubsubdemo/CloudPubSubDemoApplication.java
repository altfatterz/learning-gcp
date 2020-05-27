package com.example.cloudpubsubdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.cloud.gcp.pubsub.support.converter.PubSubMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CloudPubSubDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudPubSubDemoApplication.class, args);
    }

    @Bean
    public PubSubMessageConverter pubSubMessageConverter(ObjectMapper objectMapper) {
        return new JacksonPubSubMessageConverter(objectMapper);
    }

    @RestController
    static class MessageController {

        private static final String TOPIC = "greetings";
        private static final String MEASUREMENTS_TOPIC = "measurements";
        private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
        private final PubSubTemplate pubSubTemplate;

        public MessageController(PubSubTemplate pubSubTemplate) {
            this.pubSubTemplate = pubSubTemplate;
        }

        @PostMapping("/messages")
        public String sendMessage(@RequestBody String message) {
            logger.info("Message received: {}", message);
            pubSubTemplate.publish(TOPIC, message);
            return "Your message was published asynchronously, status is unknown";
        }

        @PostMapping("/measurements")
        public void sendMeasurement(@RequestBody Measurement measurement) {
            logger.info("Measurement received: {}", measurement);
            Map<String, String> headers = new HashMap<>();
            headers.put("TimeMillis", System.currentTimeMillis() + "");
            headers.put("foo", "bar");
            pubSubTemplate.publish(MEASUREMENTS_TOPIC, measurement, headers);
        }
    }


    static class Measurement {

        String deviceId;
        Double temperature;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        @Override
        public String toString() {
            return "Measurement{" +
                    "deviceId='" + deviceId + '\'' +
                    ", temperature=" + temperature +
                    '}';
        }
    }
}
