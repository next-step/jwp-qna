package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questions;

    @Test
    void save() {

        // when
        Question expected = questions.save(Q1);
        Question actual = questions.findByTitle(Q1.getTitle()).get();

        // then
        assertAll(
            () -> assertThat(actual).isEqualTo(expected),
            () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void writerId_작성자_일치() {

        // when
        Question question = questions.save(Q2);

        // then
        assertThat(question.getWriterId()).isEqualTo(UserTest.SANJIGI.getId());
    }

    @Test
    @DisplayName("Question 을 통해 Answer 에 question.getId() 등록")
    void addAnswer() {
        // given
        Question actual = questions.save(Q2);
        Answer expect = AnswerTest.A1;

        // when
        actual.addAnswer(expect);

        // then
        assertThat(expect.getQuestionId()).isEqualTo(actual.getId());
    }

    @Test
    @DisplayName("Question 의 작성 User 확인")
    void isOwner() {
        // when
        Question actual = questions.save(Q2);

        // then
        assertAll(
            () -> assertThat(actual.isOwner(UserTest.SANJIGI)).isTrue(),
            () -> assertThat(actual.isOwner(UserTest.JAVAJIGI)).isFalse()
        );
    }
}
