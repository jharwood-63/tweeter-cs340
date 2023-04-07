package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoDAOFactory;

public class FillDatabase {
    @Test
    public void fillDatabase() {
        DatabaseFiller filler = new DatabaseFiller(new DynamoDAOFactory());
        filler.fillDatabase();
//        filler.deleteAllItems();
    }
}
