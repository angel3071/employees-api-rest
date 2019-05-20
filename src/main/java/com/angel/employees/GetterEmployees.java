package com.angel.employees;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.json.simple.JSONObject;

import java.util.*;


public class GetterEmployees implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = System.getenv("TABLE_NAME");


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of Employees GetterEmployees");




        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        Table table = dynamoDB.getTable(tableName);
        try {


            Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
            expressionAttributeValues.put(":s", "ACTIVE");
            Map<String, String> expressionAttributeNames = new HashMap<>();
            expressionAttributeNames.put("#st", "Status");
            ItemCollection<ScanOutcome> items = table.scan(
                    "#st = :s",
                    "Id, FirstName",
                    expressionAttributeNames,
                    expressionAttributeValues);
            logger.log("Items accumulatedItemCount: " + items.getAccumulatedItemCount());
            String responseBodyString = "[\n";
            Iterator<Item> iterator = items.iterator();
            while(iterator.hasNext()){
                responseBodyString += iterator.next().toJSONPretty() + ", ";
            }
            responseBodyString += "\n]";
            if(responseBodyString.length() > 0 ){
                response.setStatusCode(200);
                response.setBody(responseBodyString);
            } else {
                response.setStatusCode(400);
                Map<String, String> responseBody = Collections.singletonMap("message", "Employee not found");
                responseBodyString = new JSONObject(responseBody).toJSONString();
                response.setBody(responseBodyString);
            }

        } catch (Exception e) {
            response.setStatusCode(400);

            Map<String, String> responseBody = Collections.singletonMap("message", e.toString());
            String responseBodyString = new JSONObject(responseBody).toJSONString();
            response.setBody(responseBodyString);

        }
        logger.log(response.toString());
        return response;
    }
}
