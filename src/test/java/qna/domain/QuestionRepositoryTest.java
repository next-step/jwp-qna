package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("질문 저장소")
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository) {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    static Stream<Arguments> example() {
        return Stream.of(Arguments.of(QuestionTest.Q1), Arguments.of(QuestionTest.Q2));
    }

    @ParameterizedTest(name = "{displayName}[{index}] {0} can be saved")
    @DisplayName("저장")
    @MethodSource("example")
    void save(Question question) {
        //when
        Question actual = questionRepository.save(question);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(actual.getWriter()).isEqualTo(question.getWriter())
        );
    }

    @ParameterizedTest(name = "{displayName}[{index}] {0} can be found by id")
    @DisplayName("아이디로 검색")
    @MethodSource("example")
    void findByIdAndDeletedFalse(Question question) {
        //given
        Question expected = questionRepository.save(question);

        //when
        Question actual = questionById(expected.getId());

        //then
        assertThat(actual)
            .isEqualTo(expected);
    }

    @ParameterizedTest(name = "{displayName}[{index}] {0} is contained in list")
    @DisplayName("삭제되지 않은 질문들 검색")
    @MethodSource("example")
    void findByQuestionIdAndDeletedFalse(Question question) {
        //given
        Question expected = questionRepository.save(question);

        // when
        List<Question> actual = questionRepository.findByDeletedFalse();

        //then
        assertThat(actual).contains(expected);
    }

    private Question questionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("id(%s) is not found", id)));
    }
}
