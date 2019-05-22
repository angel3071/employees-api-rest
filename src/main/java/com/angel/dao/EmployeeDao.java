package com.angel.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.angel.beans.Employee;
import com.angel.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class EmployeeDao implements Dao<Employee> {
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = System.getenv(Constants.TABLE_NAME);

    Table table = dynamoDB.getTable(tableName);

    @Override
    public Optional<Employee> get(String id) {
        ObjectMapper mapper = new ObjectMapper();
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
    public List<Employee> getAll() {
        return null;
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
    public void udate(Employee employee) {

    }

    @Override
    public void delete(String id) {

    }
}
