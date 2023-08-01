package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoDAOFactory;

public class FillDatabase {
    private static final List<Long> TIMESTAMPS = new ArrayList<>() {{
        add(1680975363086L);
        add(1680975793552L);
        add(1680976284636L);
        add(1680976564984L);
        add(1680976703763L);
        add(1680978436873L);
        add(1680978573727L);
        add(1680979081636L);
        add(1680979350346L);
    }};

    private DatabaseFiller filler;

    @BeforeEach
    public void setup() {
        if (filler == null) {
            filler = new DatabaseFiller(new DynamoDAOFactory());
        }
    }
    @Test
    public void fillUsers() {
        filler.addUsers();
    }

    @Test
    public void fillFollowers() {
        filler.addFollowers();
    }

    @Test
    public void clearUsers() {
        filler.clearUsers();
    }

    @Test
    public void clearFollowers() {
        filler.clearFollowers();
    }

    @Test
    public void clearFeed() {
        filler.clearFeed(TIMESTAMPS);
    }
}
