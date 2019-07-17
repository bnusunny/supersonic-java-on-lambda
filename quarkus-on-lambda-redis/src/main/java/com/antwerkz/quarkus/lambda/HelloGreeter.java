package com.antwerkz.quarkus.lambda;

import javax.enterprise.context.ApplicationScoped;

import redis.clients.jedis.Jedis;

@ApplicationScoped
public class HelloGreeter {

    public String greet() {
        String redisHost = System.getenv("REDIS_HOST");
		Jedis jedis = new Jedis(redisHost);
		jedis.set("quarkus", "hello world");

        return "data writes to redis";
    }

}
