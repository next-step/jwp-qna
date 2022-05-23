package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("생성자 테스트")
    void Question_create() {
        assertThat(Q1).isEqualTo(new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI));
    }

    @Test
    @DisplayName("작성자가 본인인지 확인")
    void Question_isOwner() {
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("Answer 추가 확인")
    void Question_addAnswer() {
        Q1.addAnswer(AnswerTest.A2);
        assertThat(AnswerTest.A2.getQuestion()).isEqualTo(Q1);
    }

    @Test
    @DisplayName("Question 삭제시 실패 - Question에 권한이 없는 경우")
    void Question_delete_fail_isNotOwner() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> Q1.delete(UserTest.SANJIGI));
    }

    @Test
    @DisplayName("Question 삭제시 실패 - 다른 사람이 등록한 Answer이 있는 경우")
    void Question_delete_fail_withOtherUserAnswer() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> Q1.delete(UserTest.JAVAJIGI));
    }

    @Test
    @DisplayName("Question 삭제시 성공")
    void Question_delete_success() {
        new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents1");
        List<DeleteHistory> deleteHistoryList = Q2.delete(UserTest.SANJIGI);
        assertThat(deleteHistoryList).hasSize(2);
    }

}
