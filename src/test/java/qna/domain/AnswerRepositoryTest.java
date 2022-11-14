package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Answer answer;
    private Question question;
    private User user;
    private List<Answer> answers;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        question = questionRepository.save(new Question("title1", "contents1"));
        answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        answers = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());
        assertAll(
                () -> assertThat(answers).isNotNull(),
                () -> assertThat(answers).contains(answer)
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId()).orElseGet(null);

        assertAll(
                () -> assertThat(findAnswer).isNotNull(),
                () -> assertThat(findAnswer).isEqualTo(answer),
                () -> assertThat(findAnswer.getQuestion()).isEqualTo(answer.getQuestion())
        );
    }

}