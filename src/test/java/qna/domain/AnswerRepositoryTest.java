package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private Answer deletedAnswer;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);

        deletedAnswer = new Answer(UserTest.JAVAJIGI, question, "Answers Contents1");
        deletedAnswer.setDeleted(true);
        answer = new Answer(UserTest.SANJIGI, question, "Answers Contents2");

        answerRepository.save(deletedAnswer);
        answerRepository.save(answer);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).containsExactly(answer);
    }

    @Test
    void findByIdAndDeletedFalse() {
        assertThat(answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId())).isEmpty();
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).hasValue(answer);
    }

}