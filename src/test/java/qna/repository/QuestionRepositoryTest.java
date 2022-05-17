package qna.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question.QuestionBuilder("title1")
            .contents("contents1")
            .build()
            .writeBy(UserRepositoryTest.JAVAJIGI);
    public static final Question Q2 = new Question.QuestionBuilder("title2")
            .contents("contents2")
            .build()
            .writeBy(UserRepositoryTest.SANJIGI);
}
