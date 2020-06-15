package com.example;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.util.Optional;

public class Greeter implements HttpFunction {

    @Override
    public void service(HttpRequest httpRequest,
                        HttpResponse httpResponse) throws Exception {
        BufferedWriter writer = httpResponse.getWriter();

        Optional<String> name = httpRequest.getFirstQueryParameter("name");
        if (name.isPresent()) {
            writer.write("Hello " + name.get() + "!");
        } else {
            writer.write("Hello World!");
        }


    }
}
