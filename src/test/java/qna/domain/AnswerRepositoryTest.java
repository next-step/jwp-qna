package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.createAnswer;
import static qna.domain.UserTest.createUser;

@DataJpaTest
@EnableJpaAuditing
class AnswerRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void findByIdAndDeletedFalse() {
        User writer1 = testEntityManager.persist(createUser("javajigi"));
        User writer2 = testEntityManager.persist(createUser("sanjigi"));
        Question question = testEntityManager.persist(createQuestion(writer1));

        Answer deletedAnswer = createAnswer(writer1, question, true);
        Answer answer = createAnswer(writer2, question, false);

        answerRepository.save(deletedAnswer);
        answerRepository.save(answer);

        assertThat(answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId())).isEmpty();
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).hasValue(answer);
    }

    private Question createQuestion(User writer) {
        return new Question("title1", "contents1").writeBy(writer);
    }

}