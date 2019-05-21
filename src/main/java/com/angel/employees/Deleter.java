package com.angel.employees;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Deleter implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = System.getenv("TABLE_NAME");

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of Employees GetterEmployee");



        String employeeId = "";

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        Table table = dynamoDB.getTable(tableName);
        try {

            Map<String, String> qps = event.getPathParameters();
            if (qps != null) {
                if (qps.get("id") != null) {
                    employeeId = qps.get("id");
                }
            }
            logger.log("EmployeeId: " + employeeId);
            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                    .withPrimaryKey("Id", employeeId)
                    .withUpdateExpression("set #st = :val1")
                    .withNameMap(new NameMap().with("#st", "Status"))
                    .withValueMap(new ValueMap().withString(":val1", "INACTIVE"))
                    .withReturnValues(ReturnValue.ALL_NEW);

            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

            response.setStatusCode(200);

            Map<String, String> responseBody = new HashMap<String, String>();
            // responseBody.put("input", event.toString());
            responseBody.put("message", String.format("Successful employee deletion with Id: %s", employeeId));
            String responseBodyString = new JSONObject(responseBody).toJSONString();

            response.setBody(responseBodyString);

            // Check the response.
            System.out.println("Printing item after deleting");
            System.out.println(outcome.getItem().toJSONPretty());

        }catch (Exception e){
            response.setStatusCode(400);

            Map<String, String> responseBody = Collections.singletonMap("message", e.toString());
            String responseBodyString = new JSONObject(responseBody).toJSONString();
            response.setBody(responseBodyString);
        }
        logger.log(response.toString());
        return response;
    }
}