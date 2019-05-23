package com.angel.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.angel.beans.Employee;
import com.angel.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class EmployeeDao implements Dao<Employee> {
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = System.getenv(Constants.TABLE_NAME);

    Table table = dynamoDB.getTable(tableName);
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Optional<Employee> get(String id) {
        Item item = table.getItem(Constants.ID, id);
        if(item.get(Constants.STATUS) == null || !Constants.STATUS_ACTIVE.equals(item.get(Constants.STATUS))){
            return  Optional.ofNullable(null);
        }
        try {
            Employee employee = mapper.readValue(item.toJSON(), Employee.class);
            return Optional.ofNullable(employee);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(null);
    }

    @Override
    public List<Employee> getAll() throws IOException {
        List<Employee> employees = new ArrayList<>();
        Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        expressionAttributeValues.put(":s", Constants.STATUS_ACTIVE);
        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#st", Constants.STATUS);
        ItemCollection<ScanOutcome> items = table.scan(
                "#st = :s",
                null,
                expressionAttributeNames,
                expressionAttributeValues);
        Iterator<Item> iterator = items.iterator();
        while(iterator.hasNext()){
            Employee employee = mapper.readValue(iterator.next().toJSON(), Employee.class);
            employees.add(employee);
        }
        return employees;
    }

    @Override
    public void save(Employee employee) {
        Item item = new Item().withPrimaryKey(Constants.ID, employee.getId())
                .withString(Constants.FIRST_NAME, employee.getFirstName())
                .withString(Constants.MIDDLE_INITIAL, employee.getMiddleInitial())
                .withString(Constants.LAST_NAME, employee.getLastName())
                .withString(Constants.DATE_OF_BIRTH, employee.getDateOfBirth())
                .withString(Constants.DATE_OF_EMPLOYMENT, employee.getDateOfEmployment())
                .withString(Constants.STATUS, Constants.STATUS_ACTIVE);
        table.putItem(item);

    }

    @Override
    public void update(Employee employee) {
        Item item = new Item().withPrimaryKey(Constants.ID, employee.getId())
                .withString(Constants.FIRST_NAME, employee.getFirstName())
                .withString(Constants.MIDDLE_INITIAL, employee.getMiddleInitial())
                .withString(Constants.LAST_NAME, employee.getLastName())
                .withString(Constants.DATE_OF_BIRTH, employee.getDateOfBirth())
                .withString(Constants.DATE_OF_EMPLOYMENT, employee.getDateOfEmployment())
                .withString(Constants.STATUS, employee.getStatus());
        table.putItem(item);

    }

    @Override
    public void delete(String id) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey(Constants.ID, id)
                .withUpdateExpression("set #st = :val1")
                .withNameMap(new NameMap().with("#st", Constants.STATUS))
                .withValueMap(new ValueMap().withString(":val1", Constants.STATUS_INACTIVE))
                .withReturnValues(ReturnValue.ALL_NEW);

        table.updateItem(updateItemSpec);

    }
}
