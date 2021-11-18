package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswersTest {

    @DisplayName("Answers일급 콜렉션 객체 생성")
    @Test
    void create() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));

        assertThat(answers.size()).isEqualTo(2);
    }

    @DisplayName("답변들을 삭제 상태로 변경시켜준다.")
    @Test
    void delete() throws CannotDeleteException {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));

        answers.delete(UserTest.JAVAJIGI);

        assertThat(answers.getAnswerGroup().get(0).isDeleted()).isTrue();
        assertThat(answers.getAnswerGroup().get(1).isDeleted()).isTrue();
    }


    @DisplayName("답변 등록자가 삭제하려는 사람과 같을때 삭제 되는지 검증")
    @Test
    void deleteAnswersByUser() throws CannotDeleteException {
        final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        final Answer A2 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents2");
        Answers answers = new Answers(Arrays.asList(A1, A2));

        answers.delete(A1.getWriter());

        assertThat(answers.getAnswerGroup().get(0).isDeleted()).isTrue();
        assertThat(answers.getAnswerGroup().get(1).isDeleted()).isTrue();
    }

    @DisplayName("답변 등록자가 삭제하려는 사람과 다를때 에러가 생기는지 검증")
    @Test
    void deleteAnswersByUserError() throws CannotDeleteException {
        final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        final Answer A2 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents2");
        Answers answers = new Answers(Arrays.asList(A1, A2));

        assertThatThrownBy(() -> {
            answers.delete(new User("lsm", "password", "이승민", "test@test.com"));
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
