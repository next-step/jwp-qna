package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TestEntityManager em;

    private User writer;

    private Question question;

    @BeforeEach
    void setUp() {
        writer = new User("shshon", "password", "손상훈", "shshon@naver.com");
        em.persist(writer);

        question = new Question("title", "content").writeBy(writer);
        em.persist(question);
    }

    @Test
    @DisplayName("주어진 답변을 영속화한다")
    void save_question_test() {
        Answer answer = new Answer(writer, question, "content");
        answerRepository.save(answer);
        assertThat(answer.getId()).isNotNull();
    }

    @Test
    @DisplayName("주어진 답변 ID로 조회한다")
    void find_answer_with_id_test() {
        Answer expectedAnswer = new Answer(writer, question, "content");
        expectedAnswer = answerRepository.save(expectedAnswer);
        Answer answer = answerRepository.findByIdAndDeletedFalse(question.getId()).get();
        assertThat(answer).isEqualTo(expectedAnswer);
    }

    @Test
    @DisplayName("주어진 작성자로 다수 조회한다.")
    void find_answers_with_writer() {
        answerRepository.saveAll(Arrays.asList(
                new Answer(writer, question, "content1"),
                new Answer(writer, question, "content2"),
                new Answer(writer, question, "content3")
        ));
        List<Answer> answers = answerRepository.findByWriterAndDeletedFalse(writer);
        assertThat(answers.size()).isEqualTo(3);
    }
}
