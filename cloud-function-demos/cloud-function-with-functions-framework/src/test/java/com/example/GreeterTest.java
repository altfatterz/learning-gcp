package com.example;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class GreeterTest {

    @Mock
    private HttpRequest request;
    @Mock
    private HttpResponse response;

    private StringWriter responseOut;
    private BufferedWriter writerOut;

    @Before
    public void beforeTest() throws IOException {
        MockitoAnnotations.initMocks(this);

        responseOut = new StringWriter();
        writerOut = new BufferedWriter(responseOut);
        when(response.getWriter()).thenReturn(writerOut);
    }

    @Test
    public void greet() throws Exception {
        new Greeter().service(request, response);

        writerOut.flush();
        assertThat(responseOut.toString(), is("Hello World!"));
    }

}
