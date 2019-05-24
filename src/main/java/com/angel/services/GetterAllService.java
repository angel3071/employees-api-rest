package com.angel.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.angel.beans.Employee;
import com.angel.dao.EmployeeDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * This class takes the getall action request, calls the corresponding action on the Dao object and returns the response to the apigateway
 */
public class GetterAllService extends GeneralService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    private static EmployeeDao edo;

    /**
     * @param event   ApiGatewayProxyRequestEvent representation of the http request
     * @param context The representation on which the actual lambda is running
     * @return A response event to the ApiGateway request action
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        ObjectMapper mapper = new ObjectMapper();
        logger.log("Loading Java Lambda handler of Employees GetterAllService");

        try {

            edo = new EmployeeDao();
            logger.log("Dao creation");
            List<Employee> employees = edo.getAll();
            logger.log("getEmployes action");
            if (employees.size() > 0) {
                response.setStatusCode(200);
                response.setBody(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(employees));
            } else {
                response = createMessageResponse(400, "Not employees found");
            }

        } catch (Exception e) {

            response = createMessageResponse(400, e.toString());

        }
        logger.log(response.toString());
        return response;
    }
}
