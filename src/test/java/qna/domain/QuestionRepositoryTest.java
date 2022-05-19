package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class QuestionRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private QuestionRepository questionRepository;

    private Question deleteQuestion;
    private Question question;

    @BeforeEach
    void setUp() {
        User writer1 = testEntityManager.persist(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        User writer2 = testEntityManager.persist(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));

        deleteQuestion = new Question("title1", "contents1").writeBy(writer1);
        deleteQuestion.setDeleted(true);
        question = new Question("title2", "contents2").writeBy(writer2);

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