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
import com.angel.beans.Employee;
import com.angel.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Recorder implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = System.getenv(Constants.TABLE_NAME);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {


        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of Employees Recorder");

        ObjectMapper mapper = new ObjectMapper();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        Table table = dynamoDB.getTable(tableName);

        try {

            String bodyStr = event.getBody();
            if (bodyStr == null ) {
                logger.log("Empty body");
                response.setStatusCode(400);
                Map<String, String> responseBody = Collections.singletonMap(Constants.MESSAGE, Constants.INVALID_REQUEST);
                String responseBodyString = new JSONObject(responseBody).toJSONString();
                response.setBody(responseBodyString);
            }
            Employee employee = mapper.readValue(bodyStr, Employee.class);

            employee.setId(UUID.randomUUID().toString());
            logger.log("Id generated for new employee");
            Item item = new Item().withPrimaryKey(Constants.ID, employee.getId())
                .withString(Constants.FIRST_NAME, employee.getFirstName())
                .withString(Constants.MIDDLE_INITIAL, employee.getMiddleInitial())
                .withString(Constants.LAST_NAME, employee.getLastName())
                .withString(Constants.DATE_OF_BIRTH, employee.getDateOfBirth())
                .withString(Constants.DATE_OF_EMPLOYMENT, employee.getDateOfEmployment())
                .withString(Constants.STATUS, Constants.STATUS_ACTIVE);
            table.putItem(item);

            logger.log("Everything as expected");
            response.setStatusCode(201);
            Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put("Location", String.format("employees/%s", employee.getId()));
            response.setHeaders(customHeaders);
            Map<String, String> responseBody = new HashMap<String, String>();
            responseBody.put(Constants.MESSAGE, String.format("Successful employee creation with Id: %s", employee.getId()));
            String responseBodyString = new JSONObject(responseBody).toJSONString();
            response.setBody(responseBodyString);

        } catch(IllegalArgumentException iae){
            logger.log("Not all the parameters was provided");
            response.setStatusCode(400);
            Map<String, String> responseBody = Collections.singletonMap(Constants.MESSAGE, "All parameters must be provided");
            String responseBodyString = new JSONObject(responseBody).toJSONString();
            response.setBody(responseBodyString);
        } catch (Exception e) {
            logger.log("Body json string related exception");
            response.setStatusCode(400);
            Map<String, String> responseBody = Collections.singletonMap(Constants.MESSAGE, Constants.INVALID_REQUEST);
            String responseBodyString = new JSONObject(responseBody).toJSONString();
            response.setBody(responseBodyString);
        }

        logger.log(response.toString());
        return response;
    }
}
