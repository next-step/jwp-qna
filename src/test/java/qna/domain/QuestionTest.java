package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.NotFoundException;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void writerId_작성자_일치() {

        // then
        assertThat(Q1.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId());
    }

    @Test
    void deleted() {

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
    @DisplayName("Question 을 통해 Answer 에 question.getId() 등록")
    void addAnswer() {
        // given
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

        // when
        Q1.addAnswer(answer);

        // then
        assertThat(Q1.getWriterId()).isEqualTo(answer.getId());
    }

    @Test
    @DisplayName("Question 의 작성 User 확인")
    void isOwner() {

        // then
        assertAll(
            () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
            () -> assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }
}
