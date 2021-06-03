package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    void setup() {
        user = new User("lkimilhol", "1234", "김일호", "lkimilhol@gmail.com");
        question = new Question("질문", "내용");
        userRepository.save(user);
        questionRepository.save(question);
    }

    @Test
    @DisplayName("답변 생성")
    void create() {
        //given
        //when
        Answer answer = answerRepository.save(new Answer(user, question, "Answer"));
        //then
        assertThat(answer.getId() > 0).isTrue();
    }

    @Test
    @DisplayName("답변 조회 - id")
    void findById() {
        //given
        //when
        Answer answer = answerRepository.save(new Answer(user, question, "Answer"));
        Optional<Answer> answerOptional = answerRepository.findByIdAndDeletedFalse(answer.getId());
        //then
        assertThat(answerOptional.isPresent()).isTrue();
    }

    @Test
    @DisplayName("답변 조회 - questionId")
    void findByQuestionId() {
        //given
        //when
        Answer answer = answerRepository.save(new Answer(user, question, "Answer"));
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());
        //then
        assertThat(answers.size() > 0).isTrue();
        assertThat(answers.get(0).getQuestion()).isEqualTo(answer.getQuestion());
    }
}
