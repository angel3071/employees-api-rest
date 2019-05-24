package com.angel.services;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.angel.constants.Constants;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;

/**
 * Service layer design pattern was choosed based on the fact that on the serverless
 * architecture the controller was inhiterated by the platform itself, in this scenario
 * the GeneralService provides some common functionality for the other Services to inherit
 */
public class GeneralService {

    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

    /**
     * @param statusCode The HTTP code to return at the end of the request
     * @param message    A common way to inform about some problems or situations
     * @return The actual response from the lambda functon to the ApiGateway
     */
    public APIGatewayProxyResponseEvent createMessageResponse(int statusCode, String message) {
        response.setStatusCode(statusCode);
        Map<String, String> responseBody = Collections.singletonMap(Constants.MESSAGE, message);
        String responseBodyString = new JSONObject(responseBody).toJSONString();
        response.setBody(responseBodyString);
        return response;
    }
}
