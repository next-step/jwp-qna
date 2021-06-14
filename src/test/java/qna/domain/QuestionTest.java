package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문자일 경우 삭제 가능")
    void delete() {
        Q2.delete(UserTest.SANJIGI);
        assertTrue(Q2.isDeleted());
    }

    @Test
    @DisplayName("질문자가 아닐 경우 삭제 불가")
    void deleteFailedUser() {
        assertThrows(CannotDeleteException.class,
            () -> Q2.delete(UserTest.JAVAJIGI));
    }

    @Test
    @DisplayName("질문자와 모든 답변자가 동일인물이면 삭제 가능")
    void deleteWithAnswers() {
        Q2.delete(UserTest.SANJIGI);

        assertThat(Q2.isDeleted()).isTrue();
        assertThat(Q2.getAnswers()).hasSize(2);
        Q2.getAnswers().forEach(answer -> {
            assertThat(answer.isDeleted()).isTrue();
            assertThat(answer.getWriter()).isEqualTo(UserTest.SANJIGI);
        });
    }

    @Test
    @DisplayName("질문자와 답변자가 한 번이라도 다를 경우 삭제 불가")
    void deleteFailedWithAnswers() {
        assertThatThrownBy(() -> Q1.delete(UserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문과 답변 삭제 이력들을 반환")
    void returnDeleteHistories() {
        List<DeleteHistory> histories = Q2.delete(UserTest.SANJIGI);

        assertThat(histories).isNotNull();
        assertThat(histories).hasSizeGreaterThan(2);

        List<DeleteHistory> questionDeleteHistories = histories.stream()
            .filter(h -> h.getContentType().equals(ContentType.QUESTION))
            .collect(Collectors.toList());
        List<DeleteHistory> answerDeleteHistories = histories.stream()
            .filter(h -> h.getContentType().equals(ContentType.ANSWER))
            .collect(Collectors.toList());

        assertThat(questionDeleteHistories).hasSize(1);
        assertThat(answerDeleteHistories).hasSizeGreaterThan(1);
        histories.forEach(deleteHistory -> {
            assertThat(deleteHistory.getDeletedBy()).isEqualTo(UserTest.SANJIGI);
        });
    }
}
