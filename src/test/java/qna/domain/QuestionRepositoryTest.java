package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questions;

    @Test
    @DisplayName("Question 저장 테스트")
    void save_question_test() {
        final Question question1 = new QuestionTest().Q1;
        final Question savedQuestion = questions.save(question1);

        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getTitle()).isEqualTo(question1.getTitle())
        );
    }

    @Test
    @DisplayName("Question 생성 및 저장 테스트")
    void create_new_question_and_save_test() {
        final Question question1 = new Question("사과의 색깔은?", "아래 항목에서 알맞은 사과의 색을 고르시오.\n 1. 파랑 2. 빨강");
        final Question savedQuestion = questions.save(question1);

        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getTitle()).isEqualTo("사과의 색깔은?")
        );
    }

    @Test
    @DisplayName("Question ID로 조회 테스트")
    void find_by_id_and_deleted_false_test() {
        final Question question1 = new QuestionTest().Q1;
        final Question savedQuestion = questions.save(question1);

        Optional<Question> searchResult = questions.findByIdAndDeletedFalse(savedQuestion.getId());

        assertAll(
                () -> assertThat(questions.findByIdAndDeletedFalse(savedQuestion.getId())).isPresent(),
                () -> assertThat(searchResult.get().getId()).isEqualTo(question1.getId())
        );
    }

    @Test
    @DisplayName("Question ID로 삭제된 항목 조회 테스트")
    void find_by_id_and_deleted_true_test() {

        final Question question1 = new QuestionTest().Q1;
        final Question savedQuestion = questions.save(question1);
        questions.delete(savedQuestion);

        Optional<Question> searchResult = questions.findByIdAndDeletedFalse(savedQuestion.getId());

        assertThat(searchResult).isNotPresent();
    }

    @Test
    @DisplayName("삭제되지 않은 Question 조회 테스트")
    void find_by_deleted_false_test() {
        final Question question1 = QuestionTest.Q1;
        final Question question2 = QuestionTest.Q2;

        questions.save(question1);
        questions.save(question2);
        
        assertThat(questions.findByDeletedFalse()).hasSize(2);
    }


}
