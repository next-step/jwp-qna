package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswersTest {

    private User loginUser;

    @BeforeEach
    public void setUp() {
        this.loginUser = UserTest.JAVAJIGI;
    }

    @Test
    @DisplayName("일급 컬렉션 생성 크기 확인")
    void create() {
        // given
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A1, AnswerTest.A2));

        // then
        assertThat(answers.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("일괄 삭제처리 성공")
    void deleteAllByWriter() throws CannotDeleteException {
        // given
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A1));

        // when
        answers.deleteAllByWriter(this.loginUser);

        //then
        assertThat(answers.getAnswers()).extracting("deleted").contains(true);
    }

    @Test
    @DisplayName("일괄 삭제처리 실패")
    void delete_failed_all_answer_by_writer() {
        // given
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A1, AnswerTest.A2));

        // then
        assertThatThrownBy(() -> answers.deleteAllByWriter(this.loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제처리 후 DeleteHistory 리스트 반환")
    void deleteAll_and_return_deleteHistories() throws CannotDeleteException {
        // given
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A1));

        // when
        List<DeleteHistory> deleteHistories = answers.deleteAllByWriter(this.loginUser);

        // then
        assertAll(
                () -> assertThat(deleteHistories).extracting("contentType").contains(ContentType.ANSWER),
                () -> assertThat(deleteHistories).extracting("deletedByUser").contains(this.loginUser)
        );
    }
}
