package com.antwerkz.quarkus.lambda;

import javax.enterprise.context.ApplicationScoped;

import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import java.util.HashMap;

@ApplicationScoped
public class HelloGreeter {

	private static DynamoDbClient ddb = DynamoDbClient.builder().region(Region.AP_NORTHEAST_1)
			.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
			.httpClientBuilder(UrlConnectionHttpClient.builder()).build();

	public String greet() {
		return "Hello world!";
	}

	public String quote() {
		String itemID = "300";
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

		return "item added.";
	}

}
