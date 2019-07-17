package com.serverless;

import java.util.Collections;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import redis.clients.jedis.Jedis;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		
		String redisHost = System.getenv("REDIS_HOST");
		Jedis jedis = new Jedis(redisHost);
		jedis.set("foo", "bar");
		
		Response responseBody = new Response("data writen to redis", null);

		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.build();
	}
}
