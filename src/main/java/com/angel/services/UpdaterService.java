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

import java.util.Map;

/**
 * This class takes the update action request, calls the corresponding action on the Dao object and returns the response to the apigateway
 */
public class UpdaterService extends GeneralService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    /**
     * @param event   ApiGatewayProxyRequestEvent representation of the http request
     * @param context The representation on which the actual lambda is running
     * @return A response event to the ApiGateway request action
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of Employees GetterService");


        String employeeId = "";
        ObjectMapper mapper = new ObjectMapper();

        try {

            Map<String, String> qps = event.getPathParameters();
            if (qps != null) {
                if (qps.get("id") != null) {
                    employeeId = qps.get("id");
                }
            }
            String bodyStr = event.getBody();
            if (bodyStr == null) {
                logger.log("Empty body");
                response = createMessageResponse(400, Constants.INVALID_REQUEST);
            }
            logger.log(bodyStr);
            Employee employee = mapper.readValue(bodyStr, Employee.class);
            employee.setId(employeeId);
            logger.log("after mapper");


            EmployeeDao edo = new EmployeeDao();
            logger.log("dao creation");
            edo.update(employee);
            logger.log("dao update action");

            logger.log("Everything as expected");

            response = createMessageResponse(200, String.format("Successful employee update with Id: %s", employeeId));

        } catch (Exception e) {
            response = createMessageResponse(400, e.toString());
        }
        logger.log(response.toString());
        return response;
    }
}
