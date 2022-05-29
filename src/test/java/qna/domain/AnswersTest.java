package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {
    @Test
    void 모든_질문_삭제() throws CannotDeleteException {
        Answers answers = new Answers();
        answers.add(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "content1"));
        answers.add(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "content2"));

        assertThat(answers.deleteAll(UserTest.JAVAJIGI).size()).isEqualTo(2);
    }
}