package com.antwerkz.quarkus.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.inject.Inject;
import java.util.Map;

public class HelloLambda implements RequestHandler<Map<String, Object>, APIGatewayProxyResponseEvent> {

    @Inject
    HelloGreeter greeter;

    ObjectWriter writer = new ObjectMapper().writerFor(ServiceStatus.class);


    @Override
    public APIGatewayProxyResponseEvent handleRequest(Map<String, Object> request, final Context context) {
    
        ServiceStatus result = new ServiceStatus();
        result.setGreeting(greeter.quote());
        result.setContext(context.toString());

        try {
            return new APIGatewayProxyResponseEvent().withBody(writer.writeValueAsString(result)).withStatusCode(200);
          } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent().withBody(e.getMessage()).withStatusCode(500);
          }
      
    }

}
