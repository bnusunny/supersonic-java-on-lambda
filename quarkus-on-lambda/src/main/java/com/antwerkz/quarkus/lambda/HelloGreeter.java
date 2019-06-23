package com.antwerkz.quarkus.lambda;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloGreeter {

    public String greet() {
        return "Hello world!";
    }

}
