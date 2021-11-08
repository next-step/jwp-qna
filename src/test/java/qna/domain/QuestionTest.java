package qna.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    private static Stream<Arguments> providerQuestions() {
        return Stream.of(
                Arguments.of(Q1),
                Arguments.of(Q2)
        );
    }

    @ParameterizedTest
    @MethodSource("providerQuestions")
    public void 질문_생성(Question excepted) {
        Question actual = questionRepository.save(excepted);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(excepted.getId()),
                () -> assertThat(actual.getWriterId()).isEqualTo(excepted.getWriterId()),
                () -> assertThat(actual.getContents()).isEqualTo(excepted.getContents()),
                () -> assertThat(actual.getTitle()).isEqualTo(excepted.getTitle())
        );
    }
}
