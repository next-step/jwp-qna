package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;

    private static Stream<Question> testFixtureProvider() {
        return Stream.of(Q1, Q2);
    }

    @ParameterizedTest
    @MethodSource("testFixtureProvider")
    void find(final Question question) {
        final Question saved = questions.save(question);
        final Question actual = questions.findById(saved.getId()).get();
        assertThat(actual).isEqualTo(saved);
        assertThat(actual).isSameAs(saved);
    }

    @ParameterizedTest
    @MethodSource("testFixtureProvider")
    void update(final Question question) {
        final Question saved = questions.save(question);

        Question updated = questions.findById(saved.getId()).get();
        updated.writeBy(UserTest.JAVAJIGI);

        final Question actual = questions.findById(saved.getId()).get();
        assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(actual).isSameAs(updated);
    }

    @ParameterizedTest
    @MethodSource("testFixtureProvider")
    void delete(final Question question) {
        final Question saved = questions.save(question);

        questions.deleteById(saved.getId());
        assertThat(questions.findById(saved.getId()).orElse(null)).isNull();
    }

}
