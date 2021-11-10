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

@DataJpaTest
@DisplayName("Question 테스트")
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    private static Stream<Arguments> testQuestions() {
        return Stream.of(Arguments.of(Q1), Arguments.of(Q2));
    }

    @DisplayName("Save 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @MethodSource("testQuestions")
    void save_확인(Question expectedResult) {
        Question result = questionRepository.save(expectedResult);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectedResult.getId()),
                () -> assertThat(result.getTitle()).isEqualTo(expectedResult.getTitle()),
                () -> assertThat(result.getContents()).isEqualTo(expectedResult.getContents()),
                () -> assertThat(result.getWriterId()).isEqualTo(expectedResult.getWriterId()),
                () -> assertThat(result.isDeleted()).isEqualTo(expectedResult.isDeleted())
        );
    }
}
