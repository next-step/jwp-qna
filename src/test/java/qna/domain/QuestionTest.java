package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("질문자가 아닌 유저가 질문을 삭제할 수 없다")
    @Test
    void deleteQuestion() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI, LocalDateTime.now()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문을 삭제할때 답변도 삭제해야 된다.")
    @Test
    void delete_질문_답변() {
        //given
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");

        //when
        question.delete(UserTest.JAVAJIGI, LocalDateTime.now());

        //then
        assertAll(
                () -> assertThat(answer.isDeleted()).isTrue(),
                () -> assertThat(question.isDeleted()).isTrue()
        );
    }

    @DisplayName("다른 사람이 쓴 답변이 있으면 삭제할 수 없다.")
    @Test
    void delete_다른_사람이_쓴_답변_삭제_불가() {
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        new Answer(1L, UserTest.SANJIGI, question, "Answers Contents1");

        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI, LocalDateTime.now()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문 삭제시 삭제 히스토리가 남아야 한다.")
    @Test
    void delete_질문_히스토리() {
        //given
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");

        //when
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI, LocalDateTime.now()).getDeleteHistories();

        //then
        assertThat(deleteHistories.size()).isEqualTo(2);
    }

    @DisplayName("답변이 없는 경우 삭제 가능하다.")
    @Test
    void delete_답변_없는_경우() {
        //given
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);

        //when
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI, LocalDateTime.now()).getDeleteHistories();

        //then
        assertAll(
                () -> assertThat(deleteHistories.size()).isEqualTo(1),
                () -> assertThat(question.isDeleted()).isTrue()
        );
    }

    @DisplayName("질문자와 답변의 모든 답변자가 같으면 삭제 가능하다.")
    @Test
    void delete_질문자_모든_답변자_같은_경우() {
        //given
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        Answer answer2 = new Answer(2L, UserTest.JAVAJIGI, question, "Answers Contents2");
        Answer answer3 = new Answer(3L, UserTest.JAVAJIGI, question, "Answers Contents3");

        //when
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI, LocalDateTime.now()).getDeleteHistories();

        //then
        assertAll(
                () -> assertThat(deleteHistories.size()).isEqualTo(4),
                () -> assertThat(question.isDeleted()).isTrue(),
                () -> assertThat(answer.isDeleted()).isTrue(),
                () -> assertThat(answer2.isDeleted()).isTrue(),
                () -> assertThat(answer3.isDeleted()).isTrue()
        );
    }

    @DisplayName("질문을 삭제한 시각과 삭제내역에 남는 시각은 같아야 한다.")
    @Test
    void delete_시각과_삭제내역_시각_같은_경우() {
        //given
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        LocalDateTime deleteAt = LocalDateTime.now();

        //when
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI, deleteAt).getDeleteHistories();

        //then
        assertThat(deleteHistories.get(0).isSameDate(deleteAt)).isTrue();
    }
}
