package com.angel.services;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.angel.constants.Constants;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;

public class GeneralService {

    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

    public APIGatewayProxyResponseEvent createResponse(int statusCode, String message){
        response.setStatusCode(statusCode);
        Map<String, String> responseBody = Collections.singletonMap(Constants.MESSAGE, message);
        String responseBodyString = new JSONObject(responseBody).toJSONString();
        response.setBody(responseBodyString);
    return  response;

    }
}
