package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository target;

    @Autowired
    private TestEntityManager manager;

    private static final User BRIAN = new User("1", "1234", "brian", "b@gmail.com");
    private static final User ANGELA = new User("2", "4321", "angela", "a@gmail.com");

    private static final Question JPA = new Question("JPA", "Pros?");
    private static final Question TDD = new Question("TDD", "Pros?");

    private static Stream<Answer> getAnswers() {
        return Stream.of(
            new Answer(BRIAN, JPA, "LazyLoading"),
            new Answer(ANGELA, TDD, "Not Sure")
        );
    }

    @ParameterizedTest
    @MethodSource("getAnswers")
    void find(final Answer answer) {
        final Answer saved = target.save(answer);
        final Answer found = target.findById(saved.getId()).get();
        assertThat(found).isEqualTo(saved);
        assertThat(found).isSameAs(saved);
        flushAndClear();
    }

    @ParameterizedTest
    @MethodSource("getAnswers")
    void update(final Answer answer) {
        target.save(answer);

        Answer updated = target.findById(answer.getId()).get();
        updated.setContents(String.format("updated %s", answer.getContents()));

        final Answer actual = target.findById(answer.getId()).get();
        assertThat(actual).isEqualTo(updated);
        assertThat(actual).isSameAs(updated);
        flushAndClear();
    }

    @ParameterizedTest
    @MethodSource("getAnswers")
    void delete(final Answer answer) {
        target.save(answer);

        target.deleteById(answer.getId());
        assertThat(target.findById(answer.getId()).orElse(null)).isNull();
    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }

}