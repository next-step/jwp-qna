package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {

    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() {
        question = new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
        answer = new Answer(UserRepositoryTest.JAVAJIGI, question, "Answers Contents1");

        question.addAnswer(answer);
    }

    @Test
    void 질문_삭제_권한_없을_때() {
        assertThatThrownBy(() -> question.validateQuestionOwner(UserRepositoryTest.SANJIGI, question)).isInstanceOf(CannotDeleteException.class);
    }
}
