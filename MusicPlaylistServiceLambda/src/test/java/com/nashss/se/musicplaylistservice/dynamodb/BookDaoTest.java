package com.nashss.se.musicplaylistservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

public class BookDaoTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private BookDao bookDao;

    @BeforeEach
    public void setup() {
        initMocks(this);
        bookDao = new BookDao(dynamoDBMapper);
    }

}
