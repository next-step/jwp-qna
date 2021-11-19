package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
    @DisplayName("답변들을 삭제한다.")
    void delete_성공() throws CannotDeleteException {
        // given
        Answer answer1 = new Answer(1L, UserRepositoryTest.JAVAJIGI, question, "Answers Contents1");
        Answer answer2 = new Answer(2L, UserRepositoryTest.JAVAJIGI, question, "Answers Contents2");
        answers.add(answer1);
        answers.add(answer2);
        List<DeleteHistory> deleteHistories = Arrays.asList(
                DeleteHistory.ofAnswer(answer1.getId(), answer1.getWriter()),
                DeleteHistory.ofAnswer(answer2.getId(), answer2.getWriter())
        );

        // when
        List<DeleteHistory> result = answers.delete(UserRepositoryTest.JAVAJIGI);

        // then
        assertThat(answer1.isDeleted()).isTrue();
        assertThat(answer2.isDeleted()).isTrue();
        assertThat(result).isEqualTo(deleteHistories);
    }

    @Test
    @DisplayName("다른 사람이 쓴 답변이 있을 경우 실패한다.")
    void delete_실패() {
        // given
        answers.add(new Answer(1L, UserRepositoryTest.JAVAJIGI, question, "Answers Contents1"));
        answers.add(new Answer(2L, UserRepositoryTest.SANJIGI, question, "Answers Contents2"));

        // when, then
        assertThatThrownBy(() -> answers.delete(UserRepositoryTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}