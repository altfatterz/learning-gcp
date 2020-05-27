package com.example.cloudpubsubdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.AcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.cloud.gcp.pubsub.support.converter.PubSubMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            pubSubTemplate.publish(TOPIC, message.trim());
            return "Your message was published asynchronously, status is unknown";
        }

        @GetMapping("/messages")
        public List<String> pull(@RequestParam("subscription") String subscription,
                                 @RequestParam("maxMessages") Integer maxMessages) {
            List<AcknowledgeablePubsubMessage> messages = pubSubTemplate.pull(subscription, maxMessages, true);
            List<String> result = messages.stream()
                    .map(acknowledgeablePubsubMessage ->
                            acknowledgeablePubsubMessage.getPubsubMessage().getData().toStringUtf8())
                    .collect(Collectors.toList());

            try {
                if (!messages.isEmpty()){
                    pubSubTemplate.ack(messages).get();
                }
            } catch (Exception e) {
                logger.error("Acking failed", e);
                return Collections.emptyList();
            }

            return result;
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
