package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.fixtures.UserTestFixture;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(UserTestFixture.손상훈);
        question = questionRepository.save(new Question(writer, "title1", "content1"));
    }

    @Test
    @DisplayName("주어진 답변을 영속화한다")
    void save_question_test() {
        // given
        Answer answer = answerRepository.save(new Answer(writer, question, "content1"));
        // when
        answerRepository.save(answer);
        // then
        assertThat(answer.getId()).isNotNull();
    }

    @Test
    @DisplayName("주어진 답변 ID로 조회한다")
    void find_answer_with_id_test() {
        // given
        Answer answer = answerRepository.save(new Answer(writer, question, "content1"));
        // when
        Answer expectedAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();
        // then
        assertThat(answer).isEqualTo(expectedAnswer);
    }

    @Test
    @DisplayName("주어진 작성자로 다수 조회한다.")
    void find_answers_with_writer() {
        // given
        answerRepository.saveAll(Arrays.asList(
                new Answer(writer, question, "content1"),
                new Answer(writer, question, "content2"),
                new Answer(writer, question, "content3")
        ));
        // when
        List<Answer> answers = answerRepository.findByWriterAndDeletedFalse(writer);
        // then
        assertThat(answers).hasSize(3);
    }
}
