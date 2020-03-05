package com.amazonaws.serverless.sample.quarkus.sns2firehose;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.firehose.FirehoseClient;

import javax.inject.Inject;

public class TestLambda implements RequestHandler<SNSEvent, Void> {

    @Inject
    ProcessingService service;

    FirehoseClient firehoseClient = FirehoseClient.builder()
            .region(Region.US_WEST_2)
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .httpClientBuilder(UrlConnectionHttpClient.builder())
            .build();

    @Override
    public Void handleRequest(SNSEvent snsEvent, Context context) {
        LambdaLogger logger = context.getLogger();

        logger.log("before service");

        service.process(snsEvent, logger, firehoseClient);

        logger.log("after service");

        return null;
    }
}
