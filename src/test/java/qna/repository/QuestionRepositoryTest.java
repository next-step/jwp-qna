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
class QuestionRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;


    private User writer;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(UserTestFixture.손상훈);
    }

    @Test
    @DisplayName("주어진 질문을 영속화한다")
    void save_question_test() {
        // given
        Question question = new Question(writer, "title1", "content1");
        // when
        questionRepository.save(question);
        // then
        assertThat(question.getId()).isNotNull();
    }

    @Test
    @DisplayName("삭제되지 않은 질문을 모두 조회한다")
    void find_all_questions_test() {
        // given
        questionRepository.saveAll(Arrays.asList(
                new Question(writer, "title1", "content1"),
                new Question(writer, "title1", "content2"),
                new Question(writer, "title1", "content3")
        ));
        // when
        List<Question> questions = questionRepository.findByDeletedFalse();
        // then
        assertThat(questions.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("주어진 ID 값으로 질문을 조회한다")
    void find_with_question_id_test() {
        // given
        Question expectedQuestion = new Question(writer, "title1", "content1");
        // when
        expectedQuestion = questionRepository.save(expectedQuestion);
        // then
        Question question = questionRepository.findByIdAndDeletedFalse(expectedQuestion.getId()).get();
        assertThat(question).isEqualTo(expectedQuestion);
    }

    @Test
    @DisplayName("주어진 답변을 질문에 추가한다")
    void add_answer_to_question_test() {
        // given
        Question question = new Question(writer, "title1", "content1");
        Answer answer1 = new Answer(writer, question, "content1");

        // when
        question.addAnswer(answer1);
        questionRepository.save(question);

        // then
        assertThat(answer1.getId()).isNotNull();
    }
}
