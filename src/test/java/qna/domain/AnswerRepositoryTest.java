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


    @Test
    void save() {
        final User writer = userRepository.save(UserTest.JAVAJIGI);
        final Question question = questionRepository.save(QuestionTest.Q1.writeBy(writer));
        final Answer A1 = new Answer(writer, question, "A1");
        final Answer actual = answerRepository.save(A1); //save 에 transaction 걸려있음. IDENTITY 전략이라 insert 적용
        assertThat(actual).isEqualTo(A1);
    }

    @Test
    void findById() {
        final User writer = userRepository.save(UserTest.SANJIGI);
        final Question question = questionRepository.save(QuestionTest.Q2.writeBy(writer));
        final Answer answer = answerRepository.save(new Answer(writer, question, "A2"));
        final Optional<Answer> actual = answerRepository.findById(answer.getId());
        assertThat(actual.get()).isEqualTo(answer);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 리스트를 QuestionId 로 찾는다")
    void findByQuestionIdAndDeletedFalse() {
        final User writer = userRepository.save(UserTest.TESTUSER);
        final List<Question> questions = questionRepository.saveAll(Arrays.asList(QuestionTest.Q3.writeBy(writer), QuestionTest.Q4.writeBy(writer)));
        final Answer A3 = new Answer(writer, questions.get(0), "A3");
        final Answer A4 = new Answer(writer, questions.get(1), "A4");
        final List<Answer> actualAnswers = answerRepository.saveAll(Arrays.asList(A3, A4));
        //actualAnswers.get(0).setDeleted(true);
        answerRepository.delete(A3);

        final List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q3.getId());
        assertThat(findAnswers.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 를 id 로 찾는다")
    void findByIdAndDeletedFalse() {
        final User writer = userRepository.save(UserTest.TESTUSER);
        final List<Question> questions = questionRepository.saveAll(Arrays.asList(QuestionTest.Q3.writeBy(writer), QuestionTest.Q4.writeBy(writer)));
        final Answer A3 = new Answer(writer, questions.get(0), "A3");
        final Answer A4 = new Answer(writer, questions.get(1), "A4");
        final List<Answer> actualAnswers = answerRepository.saveAll(Arrays.asList(A3, A4));
        //actualAnswers.get(0).setDeleted(true);
        answerRepository.delete(A3);

        final Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(A3.getId());
        assertThat(findAnswer.isPresent()).isFalse();
    }
}