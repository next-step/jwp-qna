package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.QuestionTest;

@DisplayName("Question_관련_테스트")
@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("save_확인")
    @Test
    void save() {
        Question result = questionRepository.save(QuestionTest.Q1);
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContents()).isEqualTo(QuestionTest.Q1.getContents()),
                () -> assertThat(result.getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId()),
                () -> assertThat(result.isDeleted()).isEqualTo(QuestionTest.Q1.isDeleted())
        );
    }

    @DisplayName("findByDeletedFalse_deleted_false인_데이터들_확인")
    @Test
    void findByDeletedFalse() {
        Question question1 = questionRepository.save(QuestionTest.Q1);
        Question question2 = questionRepository.save(QuestionTest.Q2);

        assertThat(questionRepository.findByDeletedFalse()).contains(question1, question2);
    }

    @DisplayName("findByIdAndDeletedFalse_id_기준으로_deleted_false인_값_확인")
    @Test
    void findByIdAndDeletedFalse() {
        Question question1 = questionRepository.save(QuestionTest.Q1);
        Question expectedResult = questionRepository.findByIdAndDeletedFalse(question1.getId()).orElse(null);
        assertThat(expectedResult).isEqualTo(question1);
    }
}
