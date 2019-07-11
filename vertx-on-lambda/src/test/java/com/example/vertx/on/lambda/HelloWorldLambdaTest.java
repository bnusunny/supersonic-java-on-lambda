package com.example.vertx.on.lambda;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import xyz.jetdrone.vertx.lambda.Lambda;

import java.util.ServiceLoader;

@RunWith(VertxUnitRunner.class)
public class HelloWorldLambdaTest {

  @Rule
  public RunTestOnContext rule = new RunTestOnContext();

  @Before
  public void beforeTest() {
    // register all lambda's into the eventbus
    ServiceLoader<Lambda> serviceLoader = ServiceLoader.load(Lambda.class);
    for (Lambda fn : serviceLoader) {
      fn.init(rule.vertx());
      rule.vertx().eventBus().localConsumer(fn.getClass().getName(), fn);
    }
  }

  @Test
  public void shouldGetAnEchoMessage(TestContext should) {
    final Async test = should.async();
    final EventBus eb = rule.vertx().eventBus();

    eb.<JsonObject>send(HelloWorldLambda.class.getName(), new JsonObject(), msg -> {
      if (msg.failed()) {
        should.fail(msg.cause());
      } else {
        final JsonObject response = msg.result().body();

        // validate the reply
        should.assertEquals(200, response.getInteger("statusCode"));
        should.assertEquals("Hello world!", response.getString("body"));
        should.assertEquals(false, response.getBoolean("isBase64Encoded"));

        JsonObject headers = response.getJsonObject("headers");
        should.assertNotNull(headers);
        should.assertEquals("AWS Lambda & Eclipse Vert.x", headers.getString("X-Powered-By"));

        test.complete();
      }
    });
  }
}
