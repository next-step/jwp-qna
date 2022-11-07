package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questions;

    @Test
    @DisplayName("Question 저장 테스트")
    void save_question_test() {
        final Question Q1 = new QuestionTest().Q1;
        final Question question1 = questions.save(Q1);
        assertThat(question1.getId()).isNotNull();
        assertThat(question1.getTitle()).isEqualTo(Q1.getTitle());
    }

    @Test
    @DisplayName("Question ID로 조회 테스트")
    void find_by_id_and_deleted_false_test() {
        final Question Q1 = new QuestionTest().Q1;
        final Question question1 = questions.save(Q1);
        Optional<Question> searchResult = questions.findByIdAndDeletedFalse(question1.getId());
        assertThat(searchResult.isPresent()).isTrue();
        assertThat(searchResult.get().getId()).isEqualTo(question1.getId());
    }

    @Test
    @DisplayName("Question ID로 삭제된 항목 조회 테스트")
    void find_by_id_and_deleted_true_test() {
        final Question Q1 = new QuestionTest().Q1;
        final Question question1 = questions.save(Q1);
        questions.delete(question1);
        Optional<Question> searchResult = questions.findByIdAndDeletedFalse(question1.getId());
        assertThat(searchResult).isEmpty();
    }

}
