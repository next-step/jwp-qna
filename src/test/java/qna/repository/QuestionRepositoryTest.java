package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("woobeen", "password", "name", "drogba02@naver.com");
    }

    @DisplayName("데이터 조회 테스트")
    @Nested
    class QueryTest {

        @DisplayName("deleted가 false 값을 조회하면 모두 조회되어야 한다")
        @Test
        void find_test() {
            Question question = new Question("question-title", "question-contents");
            Question question2 = new Question("question-title", "question-contents");
            questionRepository.saveAll(Arrays.asList(question, question2));

            List<Question> questions = questionRepository.findByDeletedFalse();
            assertThat(questions).hasSize(2);
        }

        @DisplayName(
            "id 로 deleted 가 false 인 것을 조회하면 정상적으로 조회되어야 한다" + "deleted 가 true 라면 조회되지 않아야 한다")
        @Test
        void find_test2() {
            Question question = new Question("question-title", "question-contents");
            Question question2 = new Question("question-title", "question-contents");
            questionRepository.saveAll(Arrays.asList(question, question2));

            question2.setDeleted(true);
            questionRepository.save(question2);

            Optional<Question> result = questionRepository.findByIdAndDeletedFalse(
                question.getId());
            Optional<Question> result2 = questionRepository.findByIdAndDeletedFalse(
                question2.getId());

            assertAll(() -> assertTrue(result.isPresent()), () -> assertFalse(result2.isPresent()));
        }

        @DisplayName("Question 을 저장 후 조회하면 연관관계인 writer 까지 정상적으로 조회되어야 한다")
        @Test
        void find_relation_test() {
            User writer = user;
            writer = userRepository.save(writer);
            Question question = new Question("question-title", "question-contents").writeBy(writer);
            question = questionRepository.save(question);

            User 조회된_writer = question.getWriter();
            assertThat(조회된_writer).isNotNull();
            assertEquals(조회된_writer, writer);
        }
    }

    @DisplayName("데이터 추가,수정,삭제 테스트")
    @Nested
    class CommandTest {

        @DisplayName("엔티티를 저장하면 정상적으로 저장되어야 한다")
        @Test
        void save_test() {
            Question question = new Question("question-title", "question-contents");
            questionRepository.save(question);

            assertThat(question.getId()).isNotNull();
        }

        @DisplayName("엔티티의 필드가 수정되면 수정된 상태로 조회되어야 한다")
        @Test
        void update_test() {
            Question question = new Question("question-title", "question-contents");
            Question question2 = new Question("question-title", "question-contents");
            questionRepository.saveAll(Arrays.asList(question, question2));

            question2.setDeleted(true);
            questionRepository.save(question2);

            Optional<Question> result = questionRepository.findByIdAndDeletedFalse(
                question.getId());
            Optional<Question> result2 = questionRepository.findByIdAndDeletedFalse(
                question2.getId());

            assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertFalse(result2.isPresent())
            );
        }

        @DisplayName("entity 를 삭제하면 정상적으로 삭제되어야 한다")
        @Test
        void delete_test() {
            Question question = new Question("question-title", "question-contents");
            Question question2 = new Question("question-title", "question-contents");
            questionRepository.saveAll(Arrays.asList(question, question2));

            List<Question> questions = questionRepository.findAll();

            questionRepository.delete(question);
            List<Question> deleted_questions = questionRepository.findAll();

            assertAll(
                () -> assertThat(questions).hasSize(2),
                () -> assertThat(deleted_questions).hasSize(1));
        }
    }
}
