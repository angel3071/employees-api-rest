package com.angel.services;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.angel.constants.Constants;
import com.angel.dao.EmployeeDao;

import java.util.Base64;
import java.util.Map;

/**
 * This class takes the delete action request, calls the corresponding action on the Dao object and returns the response to the apigateway
 */
public class DeleterService extends GeneralService implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    /**
     * @param event   ApiGatewayProxyRequestEvent representation of the http request
     * @param context The representation on which the actual lambda is running
     * @return A response event to the ApiGateway request action
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of Employees DeleterService");
        String employeeId = "";

        try {
            //To get the headers of the http request
            String authorization = "";
            Map<String, String> hps = event.getHeaders();
            if (hps != null) {
                authorization = hps.get("Authorization");
            }

            logger.log("Authorization: " + authorization);
            //The authorization part of things
            //I am compleatly on the same way of think that this would be better to perform on other underliyng layer,
            //but for the purpouse of this project this is a simple solutions to the requested requirement
            assert authorization.substring(0, 6).equals("Basic ");
            String basicAuthEncoded = authorization.substring(6);
            String basicAuthAsString = new String(
                    Base64.getDecoder().decode(basicAuthEncoded.getBytes()));
            logger.log("Basic Authorization Encoded: " + basicAuthEncoded);
            logger.log("Basic Authorization String: " + basicAuthAsString);
            String[] credentials = basicAuthAsString.split(":");
            //Of course this credentials would be better on a secure repository like a wallet for example
            if (!Constants.AUTHORIZATION_USER.equals(credentials[0]) || !Constants.AUTHORIZATION_PASSWORD.equals(credentials[1])) {
                response = createMessageResponse(401, "Unauthorized to perform this action");
                return response;
            }
            //To get the parameters of the http url request
            Map<String, String> qps = event.getPathParameters();
            if (qps != null) {
                if (qps.get("id") != null) {
                    employeeId = qps.get("id");
                }
            }
            logger.log("EmployeeId: " + employeeId);
            //The actual DAO instance
            EmployeeDao employeeDao = new EmployeeDao();
            logger.log("dao creation");
            employeeDao.delete(employeeId);
            logger.log("dao delete action");
            //An ok generated response
            response = createMessageResponse(200, String.format("Successful employee deletion with Id: %s", employeeId));


        } catch (ConditionalCheckFailedException e) {
            response = createMessageResponse(404, "Employee not found");

        }catch (Exception e) {
            //On the case of a internal server error
            response = createMessageResponse(400, e.getMessage());

        }
        //The actual response to cloudwatch
        logger.log(response.toString());
        return response;
    }
}
