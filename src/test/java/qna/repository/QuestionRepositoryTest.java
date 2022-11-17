package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.*;
import qna.fixtures.AnswerTestFixture;
import qna.fixtures.QuestionTestFixture;
import qna.fixtures.UserTestFixture;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestEntityManager em;

    private User writer;

    @BeforeEach
    void setUp() {
        writer = UserTestFixture.createUser();
        em.persist(writer);
    }

    @Test
    @DisplayName("주어진 질문을 영속화한다")
    void save_question_test() {
        Question question = QuestionTestFixture.createQuestion();
        questionRepository.save(question);
        assertThat(question.getId()).isNotNull();
    }

    @Test
    @DisplayName("삭제되지 않은 질문을 모두 조회한다")
    void find_all_questions_test() {
        questionRepository.saveAll(Arrays.asList(
                QuestionTestFixture.createQuestionWithWriter(writer),
                QuestionTestFixture.createQuestionWithWriter(writer),
                QuestionTestFixture.createQuestionWithWriter(writer)
        ));
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("주어진 ID 값으로 질문을 조회한다")
    void find_with_question_id_test() {
        Question expectedQuestion = QuestionTestFixture.createQuestionWithWriter(writer);
        expectedQuestion = questionRepository.save(expectedQuestion);
        Question question = questionRepository.findByIdAndDeletedFalse(expectedQuestion.getId()).get();
        assertThat(question).isEqualTo(expectedQuestion);
    }

    @Test
    @DisplayName("주어진 답변을 질문에 추가한다")
    void add_answer_to_question_test() {
        Question question = QuestionTestFixture.createQuestionWithWriter(writer);
        Answer answer = AnswerTestFixture.createAnswerWithWriterAndQuestion(writer, question);
        question.addAnswer(answer);

        questionRepository.save(question);

        assertThat(question.numberOfAnswers()).isEqualTo(1);
    }
}
