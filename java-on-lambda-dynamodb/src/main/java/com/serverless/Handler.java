package com.serverless;

import java.util.Collections;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import java.util.HashMap;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final DynamoDbClient ddb = DynamoDbClient.create();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		String itemID = "100";
		String quote = "My opinions may have changed, but not the fact that I’m right.";
		String table_name = "Quotes";

		HashMap<String, AttributeValue> item_values = new HashMap<String, AttributeValue>();

		item_values.put("ID", AttributeValue.builder().s(itemID).build());
		item_values.put("quote", AttributeValue.builder().s(quote).build());

		PutItemRequest request = PutItemRequest.builder().tableName(table_name).item(item_values).build();

		try {
			ddb.putItem(request);
		} catch (ResourceNotFoundException e) {
			System.err.format("Error: The table \"%s\" can't be found.\n", table_name);
			System.err.println("Be sure that it exists and that you've typed its name correctly!");
			System.exit(1);
		} catch (DynamoDbException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		Response responseBody = new Response("item added.", null);
		return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless")).build();
	}
}
