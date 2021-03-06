package com.angel.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.angel.beans.Employee;
import com.angel.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

/**
 * This class implements the General Dao interface to define the actual actions to perform
 */
public class EmployeeDao implements Dao<Employee> {
    //Some objects used to perform the database interling actions
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);
    static String tableName = System.getenv(Constants.TABLE_NAME);
    Table table = dynamoDB.getTable(tableName);
    //Jackson mapper was the better option to perform the parse in and out
    ObjectMapper mapper = new ObjectMapper();

    /**
     * @param id The Id to retreive the element
     * @return The actual employee representation for the employee searched
     */
    @Override
    public Optional<Employee> get(String id) throws IOException {
        Item item = table.getItem(Constants.ID, id);
        if (item == null ||  !Constants.STATUS_ACTIVE.equals(item.get(Constants.STATUS))) {
            return Optional.ofNullable(null);
        }
        Employee employee = mapper.readValue(item.toJSON(), Employee.class);
        return Optional.ofNullable(employee);
    }

    /**
     * @return An arraylist of all ACTIVE employees on the database
     * @throws IOException In the case of not compatible types to json representation
     */
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
        while (iterator.hasNext()) {
            Employee employee = mapper.readValue(iterator.next().toJSON(), Employee.class);
            employees.add(employee);
        }
        return employees;
    }

    /**
     * @param employee The actual employee to write to the database
     */
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

    /**
     * @param employee The employee to update on the database
     */
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

    /**
     * @param id The key to perform the delete action on
     */
    @Override
    public void delete(String id) throws ConditionalCheckFailedException {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey(Constants.ID, id)
                .withConditionExpression("attribute_exists(id)")
                .withUpdateExpression("set #st = :val1")
                .withNameMap(new NameMap().with("#st", Constants.STATUS))
                .withValueMap(new ValueMap().withString(":val1", Constants.STATUS_INACTIVE))
                .withReturnValues(ReturnValue.ALL_NEW);

        table.updateItem(updateItemSpec);

    }
}
