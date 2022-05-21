package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questions;

    @DisplayName("동등성 비교테스트")
    @Test
    void identityTest() {
        Question savedQuestion = questions.save(Q1);
        Optional<Question> isQuestion = questions.findById(savedQuestion.getId());
        assertThat(isQuestion.isPresent()).isTrue();
        assertThat(isQuestion.get()).isSameAs(savedQuestion);
    }

    @DisplayName("writerID 로 검색하여 동일한 Writer 만 가져오기")
    @ParameterizedTest
    @MethodSource("provideQuestion")
    void findByWriterTest(final Question question) {
        questions.saveAll(Arrays.asList(Q1, Q2));
        List<Question> questionList = questions.findByWriterId(question.getWriterId());
        assertThat(questionList.stream().mapToLong(Question::getWriterId).distinct().count()).isEqualTo(1);
    }

    private static Stream<Arguments> provideQuestion() {
        return Stream.of(
                Arguments.of(Q1),
                Arguments.of(Q2)
        );
    }

}
