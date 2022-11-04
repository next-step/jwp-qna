package qna.domain;

import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public class NewEntityTestBase {
    protected User NEWUSER1;
    protected User NEWUSER2;
    protected Question Q1;
    protected Question Q2;

    @BeforeEach
    void setUp() {
        NEWUSER1 = new User("id1","pass","name","email");
        NEWUSER2 = new User("id2","pass","name","email");
        Q1 = new Question("title", "contents").writeBy(NEWUSER1);
        Q2 = new Question("title", "contents").writeBy(NEWUSER2);
    }
}
