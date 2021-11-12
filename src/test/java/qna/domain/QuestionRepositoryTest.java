package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questions;

    @Test
    @DisplayName("QuestionRepository 저장 후 ID not null 체크")
    void save() {
        // when
        Question actual = questions.save(QuestionTest.Q1);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("QuestionRepository 저장 후 DB조회 객체 동일성 체크")
    void identity() {
        // when
        Question actual = questions.save(QuestionTest.Q2);
        Question expect = questions.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("삭제안된 Question 조회 검증, 조회결과있음")
    void findByIdAndDeletedFalse() {
        // given
        // when
        Question question = questions.save(QuestionTest.Q2);
        Question actual = questions.findByIdAndDeletedFalse(question.getId()).get();

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("삭제안된 Question 조회 검증, 조회결과없음")
    void findByIdAndDeletedFalse_deleted_true() {
        // given
        // when
        Question question = questions.save(QuestionTest.Q2);
        question.setDeleted(true);
        Optional<Question> actual = questions.findByIdAndDeletedFalse(question.getId());

        // then
        assertThat(actual.isPresent()).isFalse();
    }

    @Test
    @DisplayName("삭제안된 전체 질문 목록 불러오기 검증")
    void findByDeletedFalse() {
        // given
        // when
        Question question = questions.save(QuestionTest.Q2);

        List<Question> actual = questions.findByDeletedFalse();

        assertThat(actual).contains(question);
    }
}
