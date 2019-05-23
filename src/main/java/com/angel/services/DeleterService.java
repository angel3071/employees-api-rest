package com.angel.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.angel.dao.EmployeeDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

public class DeleterService extends GeneralService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {



    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of Employees GetterService");



        String employeeId = "";
        ObjectMapper mapper = new ObjectMapper();


        try {
            String authorization = "";
             Map<String, String> hps = event.getHeaders();
             if (hps != null) {
                 authorization = hps.get("Authorization");
             }

            logger.log("Authorization: " + authorization);
            assert authorization.substring(0, 6).equals("Basic ");
            String basicAuthEncoded = authorization.substring(6);
            String basicAuthAsString = new String(
                    Base64.getDecoder().decode(basicAuthEncoded.getBytes()));
            logger.log("Basic Authorization Encoded: " + basicAuthEncoded);
            logger.log("Basic Authorization String: " + basicAuthAsString);
            String[] credentials = basicAuthAsString.split(":");
            if(!"Aladdin".equals(credentials[0]) || !"OpenSesame".equals(credentials[1])){
                response = createMessageResponse(401, "Unauthorized to perform this action");
                return  response;
            }

            Map<String, String> qps = event.getPathParameters();
            if (qps != null) {
                if (qps.get("id") != null) {
                    employeeId = qps.get("id");
                }
            }
            logger.log("EmployeeId: " + employeeId);

            EmployeeDao edo = new EmployeeDao();
            logger.log("dao creation");
            edo.delete(employeeId);
            logger.log("dao delete action");

            response = createMessageResponse(200, String.format("Successful employee deletion with Id: %s", employeeId));


        }catch (Exception e){
            response = createMessageResponse(400, e.toString());

        }
        logger.log(response.toString());
        return response;
    }
}
