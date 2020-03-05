package com.amazonaws.serverless.sample.quarkus.sns2firehose;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.firehose.FirehoseClient;
import software.amazon.awssdk.services.firehose.model.PutRecordRequest;
import software.amazon.awssdk.services.firehose.model.Record;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProcessingService {

    public void process(SNSEvent snsEvent, LambdaLogger logger, FirehoseClient firehoseClient) {
        logger.log("before send to firehose");

        // get the sns record
        SNSRecord snsRecord = snsEvent.getRecords().get(0);

        // prepare the pur record request
        PutRecordRequest putRecordRequest = PutRecordRequest.builder()
                .deliveryStreamName("sns-message-stream")
                .record(Record.builder().data(SdkBytes.fromUtf8String(snsRecord.toString() + "\n")).build())
                .build();

        // Put record into the DeliveryStream
        firehoseClient.putRecord(putRecordRequest);

        logger.log("after send to firehose");
    }
}
