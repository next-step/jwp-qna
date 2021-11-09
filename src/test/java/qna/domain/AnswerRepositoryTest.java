package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;


@DataJpaTest
@DisplayName("답변 저장소")
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private static Stream<Arguments> example() {
        return Stream.of(Arguments.of(AnswerTest.A1), Arguments.of(AnswerTest.A2));
    }

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository,
        @Autowired QuestionRepository questionRepository) {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        questionRepository.save(QuestionTest.Q1);
    }

    @ParameterizedTest(name = "{displayName}[{index}] {0} can be saved")
    @DisplayName("저장")
    @MethodSource("example")
    void save(Answer answer) {
        //when
        Answer actual = answerRepository.save(answer);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
            () -> assertThat(actual.getQuestion()).isEqualTo(answer.getQuestion()),
            () -> assertThat(actual.getWriter()).isEqualTo(answer.getWriter())
        );
    }

    @ParameterizedTest(name = "{displayName}[{index}] {0} can be found by id")
    @DisplayName("아이디로 검색")
    @MethodSource("example")
    void findByIdAndDeletedFalse(Answer answer) {
        //given
        Answer expected = answerRepository.save(answer);

        //when
        Answer actual = answerById(expected.getId());

        //then
        assertThat(actual)
            .isEqualTo(expected);
    }

    private Answer answerById(Long id) {
        return answerRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("id(%s) is not found", id)));
    }
}
