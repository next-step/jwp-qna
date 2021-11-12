package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void deleted() {
        // given
        // when
        Q1.setDeleted(true);

        // then
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    void deleted_기본값_false() {

        // then
        assertThat(Q1.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("질문의 답변리스트에 답변 추가하기")
    void addAnswer() {
        // given
        Question question = Q1;
        Answer answer = new Answer(UserTest.SANJIGI, question, "답변내용");

        // when
        question.addAnswer(answer);

        // then
        assertThat(question.getAnswers()).contains(answer);
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
}
