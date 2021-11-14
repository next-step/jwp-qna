package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.UnAuthorizedException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("delete(삭제) 메소드 호출시 delete 값을 true 로 변경")
    void deleted() {
        // given
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        // when
        question.delete(UserTest.JAVAJIGI);

        // then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("delete 컬럼의 기본 값은 false 입니다")
    void deleted_기본값_false() {

        // then
        assertThat(Q1.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("질문의 답변목록에 답변 추가하기")
    void addAnswer() {
        // given
        Question question = Q1;
        Answer expect = new Answer(UserTest.SANJIGI, question, "답변내용");

        // when
        question.addAnswer(expect);
        List<Answer> actual = question.getAnswers();

        // then
        assertThat(actual).contains(expect);
    }

    @Test
    @DisplayName("Question 의 작성 User 확인")
    void isOwner() {
        // given
        // when

        // then
        assertAll(
            () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
            () -> assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("게스트는 질문 할 수 없습니다. 예외 발생")
    void guest_answer_fail() {
        User guest = User.GUEST_USER;

        assertThatExceptionOfType(UnAuthorizedException.class) // then
            .isThrownBy(() -> {
                // when
                new Question("안녕하세요 질문이있습니다.", "미가입자도 질문 가능한가요?").writeBy(guest);
            }).withMessage(UnAuthorizedException.GUEST_USER_NOT_QUESTION);
    }
}
