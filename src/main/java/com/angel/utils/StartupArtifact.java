package com.angel.utils;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.angel.beans.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class StartupArtifact implements RequestHandler<Map<String, Object>, Object>{

    /**
     * Handles a Lambda Function request
     *
     * @param input   The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    @Override
    public Object handleRequest(Map<String, Object> input, Context context) {
        context.getLogger().log("Entering the StartupArtifact");
        ObjectMapper mapper = new ObjectMapper();

        try{
            Employee employee = mapper.readValue(new File("./InitialEmployees.json"), Employee.class);
            context.getLogger().log("Initial employee: " + employee.getFirstName());
        }catch (IOException e){
            e.printStackTrace();
        }

        String responseURL = (String)input.get("ResponseURL");
        try {
            URL url = new URL(responseURL);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            JSONObject cloudFormationJsonResponse = new JSONObject();
            try {
                cloudFormationJsonResponse.put("Status", "SUCCESS");
                cloudFormationJsonResponse.put("PhysicalResourceId", context.getLogStreamName());
                cloudFormationJsonResponse.put("StackId", input.get("StackId"));
                cloudFormationJsonResponse.put("RequestId", input.get("RequestId"));
                cloudFormationJsonResponse.put("LogicalResourceId", input.get("LogicalResourceId"));
                cloudFormationJsonResponse.put("Data", new JSONObject().put("CFAttributeRefName", "some String value useful in your CloudFormation template"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.write(cloudFormationJsonResponse.toString());
            out.close();
            int responseCode = connection.getResponseCode();
            context.getLogger().log("Response Code: " + responseCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
