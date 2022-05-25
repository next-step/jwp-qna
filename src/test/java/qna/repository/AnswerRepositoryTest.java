package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private Question question;

    @BeforeEach
    public void setup() {
        user = userRepository.save(new User("bestsilver", "password", "name", "bestsilver@ggg.net"));
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        Answer answer = new Answer(user, question, "Answers Contents1");

        answerRepository.save(answer);
    }

    @Test
    void 답변_등록_테스트() {
        Answer answer = answerRepository.save(new Answer(user, question, "Answers Contents3"));
        assertThat(answer.getContents()).isEqualTo("Answers Contents3");
    }

    @Test
    void findByQuestionIdAndDeletedFalse_테스트() {
        answerRepository.save(new Answer(user, question, "Answers Content2"));

        List<Answer> byQuestionIdAndDeletedFalse = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(byQuestionIdAndDeletedFalse.size()).isEqualTo(2);
    }

    @Test
    void findByIdAndDeletedFalse_테스트() {
        Answer answer3 = new Answer(user, question, "Answers Contents3");
        answerRepository.save(answer3);
        Optional<Answer> byIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(answer3.getId());

        assertThat(byIdAndDeletedFalse.isPresent()).isTrue();
        assertThat(byIdAndDeletedFalse.get()).isEqualTo(answer3);
    }

    @Test
    void 타인이_작성한_Answer_삭제시_에러_throw() {
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "Answers Contents3");

        assertThrows(CannotDeleteException.class, () -> {
            answer.delete(user);
        });
    }
}
