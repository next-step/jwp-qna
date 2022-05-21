package qna.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private User user;
    private Question question;

    @BeforeEach
    void setup() {
        user = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
    }

    @Test
    void findByQuestionIdAndDeletedFalse_조회_테스트() {
        Answer answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));

        List<Answer> results = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());

        assertThat(results.get(0)).isSameAs(answer);
    }

    @Test
    void findByIdAndDeletedFalse_조회_테스트() {
        Answer answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());

        Assertions.assertAll(
                () -> assertThat(result.isPresent()).isTrue(),
                () -> assertThat(result.get()).isSameAs(answer)
        );
    }
}