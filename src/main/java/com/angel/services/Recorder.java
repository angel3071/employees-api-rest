package com.angel.services;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.angel.beans.Employee;
import com.angel.constants.Constants;
import com.angel.dao.EmployeeDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Recorder extends GeneralService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {


        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of Employees Recorder");

        ObjectMapper mapper = new ObjectMapper();

        try {

            String bodyStr = event.getBody();
            if (bodyStr == null ) {
                logger.log("Empty body");
                response = createMessageResponse(400, Constants.INVALID_REQUEST);
            }
            logger.log(bodyStr);
            Employee employee = mapper.readValue(bodyStr, Employee.class);
            logger.log("after mapper");

            employee.setId(UUID.randomUUID().toString());
            logger.log("after setid");

            EmployeeDao edo = new EmployeeDao();
            logger.log("dao creation");
            edo.save(employee);
            logger.log("dao save action");

            logger.log("Everything as expected");

            response = createMessageResponse(201, String.format("Successful employee creation with Id: %s", employee.getId()));
            Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put("Location", String.format("employees/%s", employee.getId()));
            response.setHeaders(customHeaders);

        } catch(IllegalArgumentException iae){
            logger.log("Not all the parameters was provided");
            response = createMessageResponse(400, "All parameters must be provided");

        } catch (Exception e) {
            logger.log("Body json string related exception");
            response = createMessageResponse(400, Constants.INVALID_REQUEST);
        }

        logger.log(response.toString());
        return response;
    }
}
