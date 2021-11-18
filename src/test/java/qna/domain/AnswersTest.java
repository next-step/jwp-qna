package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {

    private Question question;
    private Answers answers;

    @BeforeEach
    void setUp() {
        question = new Question();
        question.writeBy(UserRepositoryTest.JAVAJIGI);

        answers = new Answers();
    }

    @Test
    @DisplayName("답변을 추가한다.")
    void add() {
        // given
        assertThat(answers.getAnswers().size()).isEqualTo(0);
        Answer answer = new Answer(1L, UserRepositoryTest.JAVAJIGI, question, "Answers Contents1");

        // when
        answers.add(answer);

        // then
        assertThat(answers.getAnswers().size()).isEqualTo(1);
    }

    @Test
    void delete_성공() {
        // given
        answers.add(new Answer(1L, UserRepositoryTest.JAVAJIGI, question, "Answers Contents1"));
        answers.add(new Answer(2L, UserRepositoryTest.SANJIGI, question, "Answers Contents2"));

        // when, throw
        assertThatThrownBy(() -> answers.delete(UserRepositoryTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}