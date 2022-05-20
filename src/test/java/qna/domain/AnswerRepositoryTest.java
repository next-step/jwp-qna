package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class AnswerRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AnswerRepository answerRepository;

    private User writer1;
    private User writer2;
    private Question question;

    @BeforeEach
    void setUp() {
        writer1 = testEntityManager.persist(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        writer2 = testEntityManager.persist(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));
        question = testEntityManager.persist(new Question("title1", "contents1").writeBy(writer1));
    }

    @Test
    void findByIdAndDeletedFalse() {
        Answer deletedAnswer = new Answer(writer1, question, "Answers Contents1");
        deletedAnswer.setDeleted(true);
        Answer answer = new Answer(writer2, question, "Answers Contents2");

        answerRepository.save(deletedAnswer);
        answerRepository.save(answer);

        assertThat(answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId())).isEmpty();
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).hasValue(answer);
    }

}