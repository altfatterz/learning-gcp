package com.example;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.util.Optional;

public class Greeter implements HttpFunction {

    @Override
    public void service(HttpRequest req, HttpResponse res) throws Exception {
        Optional<String> name = req.getFirstQueryParameter("name");
        res.getWriter().write("Hello " + name.orElse("World!"));
    }
}
