package com.angel.services;

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
import org.json.simple.parser.JSONParser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Updater implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

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
        JSONParser parser = new JSONParser();
        String firstName, middleInitial, lastName, dateOfBirth, dateOfEmployment;
        firstName = middleInitial = lastName = dateOfBirth = dateOfEmployment = "";
        try {

            Map<String, String> qps = event.getPathParameters();
            if (qps != null) {
                if (qps.get("id") != null) {
                    employeeId = qps.get("id");
                }
            }
            logger.log("EmployeeId: " + employeeId);
            String bodyStr = event.getBody();
            if (bodyStr != null) {
                JSONObject body;

                body = (JSONObject) parser.parse(bodyStr);

                if (body.get("firstName") != null) {
                    firstName = (String) body.get("firstName");
                }
                if (body.get("middleInitial") != null) {
                    middleInitial = (String) body.get("middleInitial");
                }
                if (body.get("lastName") != null) {
                    lastName = (String) body.get("lastName");
                }
                if (body.get("dateOfBirth") != null) {
                    dateOfBirth = (String) body.get("dateOfBirth");
                }
                if (body.get("dateOfEmployment") != null) {
                    dateOfEmployment = (String) body.get("dateOfEmployment");
                }
            }
            Item item = new Item().withPrimaryKey("Id", employeeId)
                    .withString("FirstName", firstName)
                    .withString("MiddleInitial", middleInitial)
                    .withString("LastName", lastName)
                    .withString("DateOfBirth", dateOfBirth)
                    .withString("DateOfEmployment", dateOfEmployment)
                    // .withStringSet("Authors", new HashSet<String>(Arrays.asList("Author12", "Author22")))
                    // .withNumber("Price", 20).withString("Dimensions", "8.5x11.0x.75").withNumber("PageCount", 500)
                    .withString("Status", "ACTIVE");
            table.putItem(item);

            // String greeting = "Good " + time + ", " + name + " of " + city + ".";
            // if (day != null && day != "")
            //     greeting += " Happy " + day + "!";

            // response.setHeaders(Collections.singletonMap("x-custom-header", "my custom header value"));
            response.setStatusCode(200);

            Map<String, String> responseBody = new HashMap<String, String>();
            // responseBody.put("input", event.toString());
            responseBody.put("message", String.format("Successful employee creation with Id: %s", employeeId));
            String responseBodyString = new JSONObject(responseBody).toJSONString();

            response.setBody(responseBodyString);

        } catch (Exception e){
            response.setStatusCode(400);

            Map<String, String> responseBody = Collections.singletonMap("message", e.toString());
            String responseBodyString = new JSONObject(responseBody).toJSONString();
            response.setBody(responseBodyString);
        }
        logger.log(response.toString());
        return response;
    }
}
