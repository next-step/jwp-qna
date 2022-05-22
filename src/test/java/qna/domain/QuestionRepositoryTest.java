package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.createQuestion;
import static qna.domain.UserTest.createUser;

@DataJpaTest
@EnableJpaAuditing
class QuestionRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void findByDeletedFalse() throws CannotDeleteException {
        User writer1 = testEntityManager.persist(createUser("javajigi"));
        questionRepository.save(createQuestion(writer1, true));
        Question question = questionRepository.save(createQuestion(writer1, false));

        assertThat(questionRepository.findByDeletedFalse()).containsExactly(question);
    }

    @Test
    void findByIdAndDeletedFalse() throws CannotDeleteException {
        User writer1 = testEntityManager.persist(createUser("javajigi"));
        Question deleteQuestion = questionRepository.save(createQuestion(writer1, true));
        Question question = questionRepository.save(createQuestion(writer1, false));

        assertThat(questionRepository.findByIdAndDeletedFalse(deleteQuestion.getId())).isEmpty();
        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).hasValue(question);
    }
}