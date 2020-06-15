package com.example;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;

import java.util.Map;

public class PubSubGreeter implements BackgroundFunction<PubSubMessage> {

    @Override
    public void accept(PubSubMessage pubSubMessage, Context context) throws Exception {
        System.out.println("Message ID:" + pubSubMessage.messageId);
    }
}

class PubSubMessage {
    // Cloud Functions uses GSON to populate this object.
    String data;
    Map<String, String> attributes;
    String messageId;
    String publishTime;
}