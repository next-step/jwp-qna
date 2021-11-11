package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Autowired
    AnswerRepository answers;

    @Test
    @DisplayName("Answer 저장 후 ID not null 체크")
    void save() {
        // when
        Answer actual = answers.save(AnswerTest.A1);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("Answer 저장 후 DB조회 객체 동일성 체크")
    void identity() {
        // when
        Answer actual = answers.save(AnswerTest.A1);
        Answer DB조회 = answers.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(DB조회);
    }

    @Test
    @DisplayName("toQuestion 맵핑 후 findByQuestionIdAndDeletedFalse 메소드 조회 포함 체크 ")
    void findByQuestionIdAndDeletedFalse() {
        // given
        AnswerTest.A1.toQuestion(Q1);
        AnswerTest.A1.setDeleted(false);

        // when
        Answer expect = answers.save(AnswerTest.A1);
        List<Answer> answerList = answers.findByQuestionIdAndDeletedFalse(Q1.getId());

        // then
        assertAll(
            () -> assertThat(answerList).contains(expect),
            () -> assertThat(expect.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("delete 처리 후 findByQuestionIdAndDeletedFalse 메소드 조회 미포함 체크 ")
    void findByQuestionIdAndDeletedFalse_deleted() {
        // given
        AnswerTest.A1.toQuestion(Q1);
        AnswerTest.A1.setDeleted(true);
        Answer expect = answers.save(AnswerTest.A1);

        // when
        List<Answer> answerList = answers.findByQuestionIdAndDeletedFalse(Q1.getId());

        // then
        assertAll(
            () -> assertThat(answerList.contains(expect)).isFalse(),
            () -> assertThat(expect.isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("삭제 안된 Answer 조회")
    void findByIdAndDeletedFalse() {
        // given
        Answer expect = answers.save(AnswerTest.A1);

        // when
        Answer actual = answers.findByIdAndDeletedFalse(expect.getId()).get();

        // then
        assertAll(
            () -> assertThat(actual).isEqualTo(expect),
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.isDeleted()).isFalse()
        );
    }
}
