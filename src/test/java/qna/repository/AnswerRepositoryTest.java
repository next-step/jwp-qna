package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(new User(1L, "woobeen", "password", "name", "drogba02@naver.com"));
        question = new Question("test-title", "test-contents");
        question.writeBy(writer);
        question = questionRepository.save(question);
    }

    @DisplayName("데이터 추가,수정,삭제 테스트")
    @Nested
    class CommandTest {

        @DisplayName("엔티티를 저장하면 정상적으로 저장되어 id 값이 채번되어야 한다")
        @Test
        void save_test() {
            Answer answer = new Answer(writer, question, "test-contents");
            answerRepository.save(answer);

            assertThat(answer.getId()).isNotNull();
        }

        @DisplayName("엔티티의 deleted 를 변경하고 다시 조회하면 변경된 상태로 조회되어야 한다")
        @Test
        void update_test() {
            Answer answer = new Answer(writer, question, "test-contents");
            answerRepository.save(answer);

            answer.setDeleted(true);
            answerRepository.save(answer);

            assertThat(answer.isDeleted()).isTrue();
        }

        @DisplayName("entity 를 삭제하면 정상적으로 삭제되어야 한다")
        @Test
        void delete_test() {
            Answer answer = new Answer(writer, question, "test-contents");
            Answer answer2 = new Answer(writer, question, "test-contents2");

            answerRepository.save(answer);
            answerRepository.save(answer2);

            List<Answer> answers = answerRepository.findAll();
            answerRepository.delete(answer);

            List<Answer> deletedAnswers = answerRepository.findAll();

            assertAll(
                () -> assertThat(answers).hasSize(2),
                () -> assertThat(deletedAnswers).hasSize(1)
            );
        }
    }

    @DisplayName("데이터 조회 테스트")
    @Nested
    class QueryTest {

        @DisplayName("같은 questionId 를 가진 엔티티를 questionId 로 조회하면 모두 조회되어야 한다")
        @Test
        void find_test() {
            Answer answer = new Answer(writer, question, "test-contents");
            Answer answer2 = new Answer(writer, question, "test-contents2");

            answerRepository.save(answer);
            answerRepository.save(answer2);

            List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(question);

            assertAll(
                () -> assertThat(answers.get(0).getId()).isEqualTo(answer.getId()),
                () -> assertThat(answers.get(1).getId()).isEqualTo(answer2.getId())
            );
        }

        @DisplayName("id 와 deleted 가 false 인 엔티티를 동일 조건으로 조회하면 모두 조회되어야 한다")
        @Test
        void find_test2() {
            Answer answer = new Answer(writer, question, "test-contents");
            Answer answer2 = new Answer(writer, question, "test-contents2");

            answerRepository.save(answer);
            answerRepository.save(answer2);

            answer2.setDeleted(true);
            answerRepository.save(answer2);

            Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());
            Optional<Answer> result2 = answerRepository.findByIdAndDeletedFalse(answer2.getId());

            assertTrue(result.isPresent());
            assertFalse(result2.isPresent());
        }

        @DisplayName("Answer 를 조회하면 연관관계인 Writer 도 정상적으로 조회되어야 한다")
        @Test
        void find_relation_test() {
            Answer answer = new Answer(writer, question, "test-contents");
            answerRepository.save(answer);

            Answer result = answerRepository.findById(answer.getId()).get();
            User 연관관계_writer = result.getWriter();

            assertThat(연관관계_writer).isNotNull();
            assertThat(연관관계_writer.getId()).isEqualTo(writer.getId());
        }

        @DisplayName("Answer 를 조회하면 연관관계인 Question 도 정상적으로 조회되어야 한다")
        @Test
        void find_relation_test2() {
            Answer answer = new Answer(writer, question, "test-contents");
            answerRepository.save(answer);

            Answer result = answerRepository.findById(answer.getId()).get();
            Question 연관관계_question = result.getQuestion();

            assertThat(연관관계_question).isNotNull();
            assertThat(연관관계_question.getId()).isEqualTo(question.getId());
        }
    }
}
