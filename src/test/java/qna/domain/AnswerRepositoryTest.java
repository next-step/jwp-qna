package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest //JPA(DB) 와 관련된 Test
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private User writer;
    private Question question;

    @BeforeEach
    void setting() {
        UserInfo userInfo = new UserInfo("id", "pwd", "writer", "writer@slipp.net");
        writer = userRepository.save(new User(userInfo));
        question = questionRepository.save(QuestionTest.Q1.writeBy(writer));
    }

    @Test
    void save() {
        final Answer A1 = new Answer(writer, question, new Contents("A1"));
        final Answer actual = answerRepository.save(A1); //save 에 transaction 걸려있음. IDENTITY 전략이라 insert 적용
        assertThat(actual).isEqualTo(A1);
    }

    @Test
    void findById() {
        final Answer answer = new Answer(writer, question, new Contents("AnswerIs"));
        final Answer actual = answerRepository.save(answer);
        final Optional<Answer> answers = answerRepository.findById(actual.getId());
        assertThat(actual.getContents()).isEqualTo(answer.getContents());
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 리스트를 QuestionId 로 찾는다")
    void findByQuestionIdAndDeletedFalse() {
        final Answer answer1 = new Answer(writer, question, new Contents("AnswerIs1"));
        final Answer answer2 = new Answer(writer, question, new Contents("AnswerIs2"));
        final List<Answer> actualAnswers = answerRepository.saveAll(Arrays.asList(answer1, answer2));
        //actualAnswers.get(0).setDeleted(true);
        answerRepository.delete(answer1);

        final List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(findAnswers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 를 id 로 찾는다")
    void findByIdAndDeletedFalse() {
        final Answer answer1 = new Answer(writer, question, new Contents("AnswerIs1"));
        final Answer answer2 = new Answer(writer, question, new Contents("AnswerIs2"));
        final List<Answer> actualAnswers = answerRepository.saveAll(Arrays.asList(answer1, answer2));
        //actualAnswers.get(0).setDeleted(true);
        answerRepository.delete(answer1);

        final Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(actualAnswers.get(0).getId());
        assertThat(findAnswer.isPresent()).isFalse();
    }

}