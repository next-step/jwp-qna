package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    private Question question;

    @BeforeEach
    void setup() {
        question = new Question("title", "Contents");
        questionRepository.save(question);
    }

    @Test
    void 삭제되지_않은_question_조회() {
        List<Question> byDeletedFalse = questionRepository.findByDeletedFalse();
        assertThat(byDeletedFalse).contains(question);
    }

    @Test
    void id로_삭제되지않은_question_조회() {
        Question findQuestion = questionRepository.findByIdAndDeletedFalse(1L).orElseThrow(NotFoundException::new);
        assertThat(findQuestion).isEqualTo(question);
    }

}
