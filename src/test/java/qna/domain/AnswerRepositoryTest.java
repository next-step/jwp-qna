package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class AnswerRepositoryTest {
    
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        questionRepository.save(QuestionTest.Q1);
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    @DisplayName("Answer 저장")
    @Test
    void save() {
        //given & when
        final Answer expected = answerRepository.save(AnswerTest.A1);
        final Optional<Answer> actual = answerRepository.findById(expected.getId());

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.get()).isSameAs(expected);
    }

    @DisplayName("id로 Answer 조회")
    @Test
    void findById() {
        //given
        final Answer expected = answerRepository.save(AnswerTest.A1);

        //when
        final Optional<Answer> actual = answerRepository.findById(expected.getId());

        //then
        assertThat(actual.get()).isSameAs(expected);
    }

    @DisplayName("Answer 전체 조회")
    @Test
    void findAll() {
        //given
        final Answer answer1 = answerRepository.save(AnswerTest.A1);
        final Answer answer2 = answerRepository.save(AnswerTest.A2);

        //when
        final List<Answer> result = answerRepository.findAll();

        //then
        assertThat(result).hasSize(2);
        assertThat(result.contains(answer1)).isTrue();
        assertThat(result.contains(answer2)).isTrue();
    }


    @DisplayName("questionId로 삭제되지 않은 Answer 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        //given
        AnswerTest.A1.setDeleted(true);
        final Answer deletedAnswer = answerRepository.save(AnswerTest.A1);
        final Answer answer = answerRepository.save(AnswerTest.A2);

        //when
        final List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        //then
        assertThat(result).hasSize(1);
        assertThat(result.contains(answer)).isTrue();
        assertThat(result.contains(deletedAnswer)).isFalse();
    }

    @DisplayName("id로 삭제되지 않은 Answer 조회")
    @Test
    void findByIdAndDeletedFalse() {
        //given
        AnswerTest.A1.setDeleted(true);
        final Answer deletedAnswer = answerRepository.save(AnswerTest.A1);
        final Answer answer = answerRepository.save(AnswerTest.A2);

        //when
        final Optional<Answer> deletedResult = answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId());
        final Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());

        //then
        assertThat(deletedResult.isPresent()).isFalse();
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get())
                .usingRecursiveComparison()
                .isEqualTo(answer);
    }
}
