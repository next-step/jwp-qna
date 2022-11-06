package qna.domain.answer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.domain.question.QuestionTest;
import qna.domain.user.User;

@DirtiesContext
@DataJpaTest
public class AnswerTest {

    public static Answer createAnswer(User writeUser) {
        return new Answer(writeUser, QuestionTest.createQuestion(writeUser), "Answers Contents1");
    }

}
