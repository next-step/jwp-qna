package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private User writer;

    @BeforeEach
    void setUp(@Autowired QuestionRepository questionRepository,
               @Autowired UserRepository userRepository) {
        writer = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        question = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
    }

    @DisplayName("Answer 저장")
    @Test
    void save() {
        final Answer actual = answerRepository.save(new Answer(writer, question, "Answers Contents1"));
        question.addAnswer(actual);

        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("Answer 전체 조회")
    @Test
    void findAll() {
        final Answer answer1 = answerRepository.save(new Answer(writer, question, "Answers Contents1"));
        final Answer answer2 = answerRepository.save(new Answer(writer, question, "Answers Contents2"));

        List<Answer> answers = answerRepository.findAll();

        assertThat(answers).hasSize(2);
        assertThat(answers).contains(answer1, answer2);
    }

    @DisplayName("삭제되지 않은 Answer id로 조회")
    @Test
    void findByIdAndDeletedFalse() {
        final Answer expected = answerRepository.save(new Answer(writer, question, "Answers Contents1"));

        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(expected.getId());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
        assertThat(actual.get().isDeleted()).isFalse();
    }

    @DisplayName("Question id로 삭제되지않은 Answer 리스트 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        final Answer answer1 = answerRepository.save(new Answer(writer, question, "Answers Contents1"));
        final Answer answer2 = answerRepository.save(new Answer(writer, question, "Answers Contents2"));

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(answer1.getQuestion().getId());

        assertThat(answers).hasSize(2);
        assertThat(answers).contains(answer1, answer2);
    }

}