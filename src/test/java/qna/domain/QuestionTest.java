package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("Question 생성 테스트")
    void create() {
        assertThat(question).isEqualTo(new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Question 의 contents 업데이트 시 빈 값 입력 하면 예외발생")
    void validateUpdateContents(String contents) {
        assertThatThrownBy(() -> question.updateContents(contents))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("null 또는 빈값으로 contents를 업데이트 할 수 없습니다.");
    }

    @Test
    @DisplayName("Question 의 contents 정상 업데이트 테스트")
    void updateContents() {
        String changedContents = "변경된 내용";

        question.updateContents(changedContents);

        assertThat(question.getContents()).isEqualTo(changedContents);
    }

    @Test
    @DisplayName("Question delete 테스트 - 정상 삭제일 경우")
    void delete() {
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(question.isDeleted()),
                () -> assertThat(deleteHistories).isNotEmpty()
        );
    }

    @Test
    @DisplayName("Question delete 테스트 - 질문 작성자와 제거하려는 유저가 다를 때 예외 발생")
    void delete_throw_exception() {
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }
}
