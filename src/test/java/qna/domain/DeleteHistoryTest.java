package qna.domain;

import java.time.LocalDateTime;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
    public static final DeleteHistory D1 = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.JAVAJIGI,
        LocalDateTime.now());

}
