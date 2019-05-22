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


public class GetterEmployee extends GeneralService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    private static EmployeeDao edo;


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of Employees GetterEmployee");

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

            edo = new EmployeeDao();
            logger.log("Dao creation");
            Employee employee = getEmployee(employeeId);
            logger.log("getEmploye action");
            if(employee != null){
                response.setStatusCode(200);
                response.setBody(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(employee));
            } else {
                response = createMessageResponse(400, "Employee not found");
            }

        } catch (Exception e) {
            response = createMessageResponse(400, e.toString());

        }
        logger.log(response.toString());
        return response;
    }

    private static Employee getEmployee(String employeeId) {
        Optional<Employee> employee = edo.get(employeeId);
        return employee.orElseGet(null);
    }
}
