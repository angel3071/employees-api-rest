package com.angel.utils;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.angel.beans.Employee;
import com.angel.dao.EmployeeDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * This class is used to implement the startup functionality it runs on the stack deploy or update event and writes the InitialEmployess to the database
 */
public class StartupArtifact implements RequestHandler<Map<String, Object>, Object> {

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

        try {
            //This logic simply perform the write action to the databse on every of the Employee representation of the reseources file
            List<Employee> employees = mapper.readValue(new File("./InitialEmployees.json"), new TypeReference<List<Employee>>() {
            });
            context.getLogger().log("Initial employees found: " + employees.size());
            EmployeeDao edo = new EmployeeDao();
            context.getLogger().log("dao creation");
            //Some Java8 good things
            employees.forEach(employee -> edo.save(employee));
        } catch (IOException e) {
            context.getLogger().log(e.getMessage());
        } finally {
            String responseURL = (String) input.get("ResponseURL");
            try {
                //This logic its needed to inform to cloudformation about the status on the execution of this lambda function
                //without this logic cloudformation doesn't know when the lambda finalizes their execution and may incur on
                //unfortunate cloudformation really long delays so be careful
                //credits to Neil on https://stackoverflow.com/questions/32811947/how-do-we-access-and-respond-to-cloudformation-custom-resources-using-an-aws-lam?rq=1
                URL url = new URL(responseURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
                    context.getLogger().log(e.getMessage());
                }
                out.write(cloudFormationJsonResponse.toString());
                out.close();
                int responseCode = connection.getResponseCode();
                context.getLogger().log("Response Code: " + responseCode);
            } catch (IOException e) {
                context.getLogger().log(e.getMessage());
            }
        }


        return null;
    }
}
