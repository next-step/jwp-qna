package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {

    @Test
    @DisplayName("Answers add 테스트")
    void inputTest() {
        Answers answers = Answers.createEmptyNewInstance();
        answers.addAnswer(A1);
        answers.addAnswer(A2);

        assertThat(answers.getAnswers()).hasSize(2);
    }

    @Test
    @DisplayName("Answers 내에 작성작가 아닌 다른 사람이 삭제시 예외 처리")
    void deleteAllTest() {
        Answers answers = Answers.createEmptyNewInstance();
        answers.addAnswer(A1);
        answers.addAnswer(A2);

        assertThatThrownBy(
                () -> answers.deleteAll(SANJIGI)
        ).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("Answers deleteAll 메소드는 DeleteHistory 리스트를 반환한다.")
    void deleteAllTest2() throws CannotDeleteException {
        Answers answers = Answers.createEmptyNewInstance();
        answers.addAnswer(A1);

        List<DeleteHistory> deleteHistories = answers.deleteAll(JAVAJIGI);
        assertThat(deleteHistories).isNotNull();
    }
}
