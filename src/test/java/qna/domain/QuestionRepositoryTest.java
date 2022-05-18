package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("QuestionRepository는 ")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    private Question QUESTION_FALSE;
    private Question QUESTION_TRUE;

    @BeforeEach
    void setUp() {
        QUESTION_FALSE = new Question("title", "contents", false);
        QUESTION_TRUE = new Question("title", "contents", true);
        questionRepository.saveAll(Arrays.asList(QUESTION_FALSE, QUESTION_TRUE));
    }

    @DisplayName("활성화 된 question를 모두 조회한다")
    @Test
    void findByDeletedFalse() {
        List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions).containsExactly(QUESTION_FALSE);
    }

    @DisplayName("id 값으로 활성화 된 question만 조회한다")
    @Test
    void findByIdAndDeletedFalseWithValidId() {
        Question question = questionRepository.findByIdAndDeletedFalse(QUESTION_FALSE.getId())
                .orElseThrow(NotFoundException::new);

        assertThat(question).isEqualTo(QUESTION_FALSE);
    }

    @DisplayName("id 값이 없다면 NotFoundException 던진다")
    @Test
    void findByIdAndDeletedFalseWithInvalidId() {
        assertThatThrownBy(() -> questionRepository.findByIdAndDeletedFalse(QUESTION_TRUE.getId())
                .orElseThrow(NotFoundException::new)).isInstanceOf(NotFoundException.class);
    }
}
