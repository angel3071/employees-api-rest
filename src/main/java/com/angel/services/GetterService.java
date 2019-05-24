package com.angel.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.angel.beans.Employee;
import com.angel.dao.EmployeeDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Optional;

/**
 * This class takes the get action request, calls the corresponding action on the Dao object and returns the response to the apigateway
 */
public class GetterService extends GeneralService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    private static EmployeeDao edao;

    /**
     * @param event   ApiGatewayProxyRequestEvent representation of the http request
     * @param context The representation on which the actual lambda is running
     * @return A response event to the ApiGateway request action
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of Employees GetterService");

        ObjectMapper mapper = new ObjectMapper();

        String employeeId = "";

        try {

            Map<String, String> qps = event.getPathParameters();
            if (qps != null) {
                if (qps.get("id") != null) {
                    employeeId = qps.get("id");
                }
            }
            logger.log("EmployeeId: " + employeeId);

            edao = new EmployeeDao();
            logger.log("Dao creation");
            Optional<Employee> employee = edao.get(employeeId);
            logger.log("GetEmploye action");
            if (employee.isPresent()) {
                response.setStatusCode(200);
                response.setBody(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(employee.get()));
            } else {
                response = createMessageResponse(404, "Employee not found");
            }

        } catch (Exception e) {
            response = createMessageResponse(400, e.toString());

        }
        logger.log(response.toString());
        return response;
    }

}
