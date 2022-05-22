package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.QnaDataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QnaDataJpaTest
class QuestionRepositoryTest {
    private Question question1;
    private Question question2;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void before() {
        User user1 = new User("user1", "password", "name", "user1@com");
        User user2 = new User("user2", "password", "name", "user2@com");

        question1 = new Question("title1", "contents1").writeBy(user1);
        question2 = new Question("title2", "contents2").writeBy(user2);
    }

    @Test
    void id로_조회() {
        // given
        Question question = new Question("title", "contents");
        Question saved = questionRepository.save(question);
        // when
        Optional<Question> result = questionRepository.findById(saved.getId());
        // then
        assertAll(
                () -> assertThat(result)
                        .map(Question::getTitle)
                        .hasValue("title"),
                () -> assertThat(result)
                        .map(Question::getContents)
                        .hasValue("contents")
        );
    }

    @Test
    void 저장() {
        // given
        userRepository.save(question1.getWriter());
        // when
        Question result = questionRepository.save(question1);
        questionRepository.flush();
        // then
        assertThat(result).isNotNull();
    }

    @Test
    void 삭제() {
        // given
        Question question = new Question("title", "contents");
        Question saved = questionRepository.save(question);
        // when
        questionRepository.deleteById(saved.getId());
        Optional<Question> result = questionRepository.findById(saved.getId());
        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void 수정() {
        // given
        Question question = new Question("title", "contents");
        Question saved = questionRepository.save(question);

        saved.writeContents("update contents");
        // when
        Optional<Question> result = questionRepository.findById(saved.getId());
        // then
        assertThat(result)
                .map(Question::getContents)
                .hasValue("update contents");
    }

    @ParameterizedTest
    @MethodSource(value = "question을_리턴한다")
    void id로_삭제되지_않은_질문_찾기(Question question) {
        // given
        question.delete();
        Question saved = questionRepository.save(question);
        // when
        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(saved.getId());
        // then
        assertThat(result.isPresent()).isFalse();
    }

    static Stream<Arguments> question을_리턴한다() {
        return Stream.of(
                Arguments.of(
                        new Question("title1", "contents1"),
                        false
                ),
                Arguments.of(
                        new Question("title2", "contents2"),
                        false
                )
        );
    }

    @Test
    void 삭제되지_않은_질문들을_조회한다() {
        // given
        userRepository.saveAll(Arrays.asList(question1.getWriter(), question2.getWriter()));
        questionRepository.saveAll(Arrays.asList(question1, question2));
        // when
        List<Question> result = questionRepository.findByDeletedFalse();
        // then
        assertThat(result).hasSize(2);
    }
}