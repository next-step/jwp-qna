package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    private Question question;
    private Question deletedQuestion;

    @BeforeEach
    void setup() {
        question = new Question("title", "Contents");
        deletedQuestion = new Question("deletedTitle", "deletedContents", true);
        questionRepository.save(question);
        questionRepository.save(deletedQuestion);
    }

    @Test
    void 삭제되지_않은_question_조회() {
        List<Question> byDeletedFalse = questionRepository.findByDeletedFalse();
        assertThat(byDeletedFalse).contains(question);
    }

    @Test
    void id로_삭제되지않은_question_조회() {
        Question findQuestion = questionRepository.findByIdAndDeletedFalse(question.getId()).orElseThrow(NotFoundException::new);
        assertThat(findQuestion).isEqualTo(question);
    }

    @Test
    void id값이_삭제되지않은_질문이_없으면() {
        assertThatThrownBy(() -> questionRepository.findByIdAndDeletedFalse(deletedQuestion.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

}
