package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Answer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    private void saveAnswer() {
        answerRepository.save(A1);
        answerRepository.save(A2);
    }

    @Test
    @DisplayName("데이터를 저장한다.")
    void save_test() {
        Answer save = answerRepository.save(A1);
        assertAll(
                () -> assertThat(save.getId()).isNotNull(),
                () -> assertThat(save.getContents()).isEqualTo(A1.getContents())
        );
    }

    @Test
    @DisplayName("데이터를 모두 조회한다.")
    void find_all_test() {
        saveAnswer();
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Id로 데이터를 조회한다.")
    void find_by_id_test() {
        saveAnswer();
        Optional<Answer> answerOptional = answerRepository.findByIdAndDeletedFalse(Q1.getId());
        answerOptional.ifPresent(answer -> {
            assertAll(
                    () -> assertThat(answer.getContents()).isEqualTo("Answers Contents1"),
                    () -> assertThat(answer.getQuestionId()).isNull(),
                    () -> assertThat(answer.getWriterId()).isNull(),
                    () -> assertThat(answer.isDeleted()).isFalse()
            );
        });
    }

    @Test
    @DisplayName("QuestionId로 조회한다.")
    void find_By_Question_Id_Deleted_False_test() {
        saveAnswer();
        List<Answer> questionIdAndDeletedFalse = answerRepository.findByQuestionIdAndDeletedFalse(null);
        assertThat(questionIdAndDeletedFalse.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("전체 데이터를 삭제한다.")
    void delete_all_test() {
        saveAnswer();
        answerRepository.deleteAll();
        assertThat(answerRepository.findAll().size()).isZero();
    }
}
