package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    private Question deleteQuestion;
    private Question question;

    @BeforeEach
    void setUp() {
        deleteQuestion = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        deleteQuestion.setDeleted(true);
        question = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

        questionRepository.save(deleteQuestion);
        questionRepository.save(question);
    }

    @Test
    void findByDeletedFalse() {
        List<Question> byDeletedFalse = questionRepository.findByDeletedFalse();
        assertThat(byDeletedFalse).containsExactly(question);
    }

    @Test
    void findByIdAndDeletedFalse() {
        assertThat(questionRepository.findByIdAndDeletedFalse(deleteQuestion.getId())).isEmpty();
        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).hasValue(question);
    }
}