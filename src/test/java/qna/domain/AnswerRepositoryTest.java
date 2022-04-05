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

    @BeforeEach
    void setting() {
        userRepository.saveAll(Arrays.asList(UserTest.JAVAJIGI, UserTest.SANJIGI, UserTest.TESTUSER));
        questionRepository.saveAll(Arrays.asList(QuestionTest.Q1, QuestionTest.Q2, QuestionTest.Q3, QuestionTest.Q4));
    }

    @Test
    void save() {
        final Answer actual = answerRepository.save(AnswerTest.A1);
        assertThat(actual.getId()).isEqualTo(AnswerTest.A1.getId());
    }

    @Test
    void findById() {
        final Answer answer = answerRepository.save(AnswerTest.A2);
        final Optional<Answer> actual = answerRepository.findById(AnswerTest.A2.getId());
        assertThat(actual.get()).isEqualTo(answer);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 리스트를 QuestionId 로 찾는다")
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> actualAnswers = answerRepository.saveAll(Arrays.asList(AnswerTest.A3, AnswerTest.A4));
        //actualAnswers.get(0).setDeleted(true);
        answerRepository.delete(actualAnswers.get(0));

        final List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q3.getId());
        assertThat(findAnswers.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 를 id 로 찾는다")
    void findByIdAndDeletedFalse() {
        List<Answer> actualAnswers = answerRepository.saveAll(Arrays.asList(AnswerTest.A3, AnswerTest.A4));
        //actualAnswers.get(0).setDeleted(true);
        answerRepository.delete(actualAnswers.get(0));

        final Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(AnswerTest.A3.getId());
        assertThat(findAnswer.isPresent()).isFalse();
    }
}