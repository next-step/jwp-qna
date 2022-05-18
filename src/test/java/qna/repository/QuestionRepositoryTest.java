package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Question;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    private void saveQuestion() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    @Test
    @DisplayName("데이터를 저장한다.")
    void save_test() {
        Question save = questionRepository.save(Q1);
        assertAll(
                () -> assertThat(save.getId()).isNotNull(),
                () -> assertThat(save.getTitle()).isEqualTo("title1"),
                () -> assertThat(save.getContents()).isEqualTo("contents1")
        );
    }

    @Test
    @DisplayName("전체 데이터를 조회한다.")
    void find_all_test() {
        saveQuestion();
        List<Question> all = questionRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("deleted로 데이터를 조회한다.")
    void find_by_deleted_test() {
        saveQuestion();
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("id와 deleted로 데이터를 조회한다.")
    void find_by_id_and_deleted_test() {
        saveQuestion();
        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(Q1.getId());
        questionOptional.ifPresent(question -> {
            assertAll(
                    () -> assertThat(question.getTitle()).isEqualTo("title1"),
                    () -> assertThat(question.getContents()).isEqualTo("contents1"),
                    () -> assertThat(question.isDeleted()).isFalse()
            );
        });
    }

    @Test
    @DisplayName("전체 데이터를 삭제한다.")
    void delete_all_test() {
        saveQuestion();
        questionRepository.deleteAll();
        assertThat(questionRepository.findAll().size()).isZero();
    }
}
