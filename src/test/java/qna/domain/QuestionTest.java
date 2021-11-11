package qna.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeAll
    void setUp() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    private static Stream<Arguments> provideQuestions() {
        return Stream.of(
            Arguments.of(Q1),
            Arguments.of(Q2)
        );
    }

    @DisplayName("Question객체룰 입력으로 받는 save통하여 저장한 후 조회하면, 결과의 속성과 입력객체의 속성은 동일하다.")
    @ParameterizedTest
    @MethodSource("provideQuestions")
    void saveTest(Question expected) {
        Question actual = questionRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
            () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter())
        );
    }

    @DisplayName("findByDeletedFalse를 호출하면 deleted 값이 false인 Question객체 콜렉션을 반환한다.")
    @Test
    void findByDeletedFalseTest() {
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).containsExactly(Q1, Q2);
    }

    @DisplayName("Question객체의 id와 deleted가 false를 객체를 입력으로 하는 findByIdAndDeletedFalse메서드를 호출하면, 해당 조건에 맞는 Optional<Question> 객체를 리턴한다.")
    @ParameterizedTest
    @MethodSource("provideQuestions")
    void findByIdAndDeletedFalseTest(Question expected) {
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(expected.getId());
        assertThat(actual).isEqualTo(Optional.of(expected));
    }
}
