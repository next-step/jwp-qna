package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        // given
        final User writer = userRepository.save(
            TestUserFactory.create(
                "javajigi", "password", "name", "javajigi@slipp.net"
            )
        );
        final Question expected = TestQuestionFactory.create("title1", "contents1", writer);

        // when
        final Question actual = questionRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isNotNull(),
            () -> assertThat(actual.getContents()).isNotNull()
        );
    }

    @Test
    void findByDeletedFalse() {
        // given
        final User writer = userRepository.save(
            TestUserFactory.create(
                "javajigi", "password", "name", "javajigi@slipp.net"
            )
        );
        final Question question = TestQuestionFactory.create("title1", "contents1", writer);

        // when
        questionRepository.save(question);
        final List<Question> actual = questionRepository.findByDeletedFalse();

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void findByIdAndDeletedFalse() {
        // given
        final User writer = userRepository.save(
            TestUserFactory.create(
                "javajigi", "password", "name", "javajigi@slipp.net"
            )
        );
        final Question question = TestQuestionFactory.create("title1", "contents1", writer);

        // when
        questionRepository.save(question);
        final Question actual = questionRepository.findByIdAndDeletedFalse(question.getId())
            .orElseThrow(NoSuchElementException::new);

        // then
        assertThat(actual).isEqualTo(question);
    }
}
