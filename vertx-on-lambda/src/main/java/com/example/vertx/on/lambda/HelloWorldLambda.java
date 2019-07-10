package com.example.vertx.on.lambda;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import xyz.jetdrone.vertx.lambda.Lambda;

/**
 * You can use a Lambda function to process any kind of events.
 * AWS Lambda sends events as JSON messages so it is convenient
 * to let use the generified interface.
 *
 * As a utility helper most events can be converted to POJOs
 * by using the provided implementations under the <pre>event</pre>
 * package.
 */
public class HelloWorldLambda implements Lambda<JsonObject> {

  @Override
  public void handle(Message<JsonObject> event) {

    final String responseBody = "Hello world!";

    event.reply(
      new JsonObject()
        .put("statusCode", 200)
        .put("body", responseBody)
        .put("isBase64Encoded", false)
        .put("headers", new JsonObject()
          .put("X-Powered-By", "AWS Lambda & Eclipse Vert.x")));
  }
}
