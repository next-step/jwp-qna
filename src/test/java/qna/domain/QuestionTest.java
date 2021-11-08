package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

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
            () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
        );
    }
}
