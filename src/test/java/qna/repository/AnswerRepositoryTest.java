package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Answer answer;

    private User user;

    private Question question;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("mins99", "1234", "ms", "mins99@slipp.net"));
        question = questionRepository.save(new Question("title3", "contents3").writeBy(user));
        answer = new Answer(user, question, "Answers Contents3");
    }

    @Test
    void save() {
        // given
        final Answer expected = new Answer(user, question, "Answers Contents4");

        // when
        final Answer actual = answerRepository.save(expected);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(actual.getQuestion()).isEqualTo(expected.getQuestion()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void findById() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        final Answer actual = answerRepository.findById(expected.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAll() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        final List<Answer> actual = answerRepository.findAll();

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void update() {
        // given
        final Answer expected = answerRepository.save(answer);
        final String contents = expected.getContents();

        // when
        expected.setContents(contents + " new");
        final Answer actual = answerRepository.findById(expected.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual.getContents()).isNotEqualTo(contents);
    }

    @Test
    void delete() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        answerRepository.deleteById(expected.getId());
        final Optional<Answer> actual = answerRepository.findById(expected.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void findByIdAndDeletedFalse() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        final Answer actual = answerRepository.findByIdAndDeletedFalse(expected.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByIdAndDeletedFalseSetDeletedTrue() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        expected.deleteAnswer(expected.getWriter());
        final Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(expected.getId());

        // then
        assertThat(actual).isEmpty();
    }
}
