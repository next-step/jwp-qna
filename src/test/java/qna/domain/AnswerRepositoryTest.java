package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    private User user;
    private Question question;

    @BeforeEach
    void init() {
        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(QuestionTest.Q1);
    }

    @Test
    @DisplayName("Answer 저장")
    void answer_repository_save() {
        Answer answer = new Answer(user, question, "Answers Contents1");
        Answer saveAnswer = answerRepository.save(answer);
        assertAll(
                () -> assertThat(saveAnswer.getId()).isNotNull(),
                () -> assertThat(saveAnswer.getWriter()).isEqualTo(answer.getWriter()),
                () -> assertThat(saveAnswer.getQuestion()).isEqualTo(answer.getQuestion())
        );
    }

    @Test
    @DisplayName("Answer Find By Question id And Deleted False Test")
    void answer_repository_findByQuestionIdAndDeletedFalse_return() {
        Answer answer = new Answer(user, question, "Answers Contents1");
        Answer saveAnswer = answerRepository.save(answer);
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertAll(
                () -> assertThat(answerList).hasSize(1),
                () -> assertThat(answerList).contains(saveAnswer)
        );
    }

    @Test
    @DisplayName("Answer Find By Id And Deleted False Test")
    void answer_repository_findByIdAndDeletedFalse_return() {
        Answer answer = new Answer(user, question, "Answers Contents1");
        Answer saveAnswer = answerRepository.save(answer);
        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(result.get()).isEqualTo(answer);
    }
}