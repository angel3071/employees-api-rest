package com.angel.employees;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;


public class Getter implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = System.getenv("TABLE_NAME");


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of Employees Getter");



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
            Item item = table.getItem("Id", employeeId);
            if(item.get("Status") != null && "ACTIVE".equals(item.get("Status"))){
                response.setStatusCode(200);
                String responseBodyString = item.toJSONPretty();
                response.setBody(responseBodyString);
            } else {
                response.setStatusCode(400);
                Map<String, String> responseBody = Collections.singletonMap("message", "Employee not found");
                String responseBodyString = new JSONObject(responseBody).toJSONString();
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
