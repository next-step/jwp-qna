package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    TestEntityManager em;

    User writer;
    Question question;

    @BeforeEach
    void setUp() {
        UserAuth userAuth = new UserAuth("user", "password");
        writer = userRepository.save(new User(userAuth, "name", "email@email.com"));
        question = new Question("title", "contents").writeBy(writer);
    }

    @Test
    @DisplayName("질문 저장")
    void save_question() {
        Question actual = questionRepository.save(question);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual).isEqualTo(question);
    }

    @Test
    @DisplayName("답변 저장 후 조회 결과 동일성 체크")
    void find_by_id() {
        Question expected = questionRepository.save(question);
        Optional<Question> findAnswer = questionRepository.findById(expected.getId());
        assertTrue(findAnswer.isPresent());
        findAnswer.ifPresent(actual -> assertThat(actual).isEqualTo(expected));
    }
    
    @Test
    @DisplayName("ID로 질문 조회 (삭제되지 않음)")
    void find_question_by_id_and_deleted_false() {
        Question expected = questionRepository.save(question);
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(expected.getId());
        assertTrue(findQuestion.isPresent());
        findQuestion.ifPresent(actual ->
                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual).isEqualTo(expected),
                        () -> assertFalse(actual.isDeleted())
                )
        );
    }

    @Test
    @DisplayName("ID로 질문 조회 (삭제)")
    void find_question_by_id_and_deleted_true() {
        Question expected = questionRepository.save(question);
        expected.delete(writer);
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedTrue(expected.getId());
        assertTrue(findQuestion.isPresent());
        findQuestion.ifPresent(actual ->
                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual).isEqualTo(expected),
                        () -> assertTrue(actual.isDeleted())
                )
        );
    }

    @Test
    @DisplayName("질문의 작성자를 지정")
    void write_by() {
        User expected = userRepository.save(new User(new UserAuth("user2", "password"), "name2", "email2@email.com"));
        Question actual = questionRepository.save(question).writeBy(expected);
        assertThat(actual.getWriter()).isEqualTo(expected);
    }

    @Test
    @DisplayName("질문에 답변을 추가(cascade 사용)")
    void add_answer() {
        Question saveQuestion = questionRepository.save(question);
        Answer expected = new Answer(saveQuestion.getWriter(), saveQuestion, "contents");
        Answer expected2 = new Answer(saveQuestion.getWriter(), saveQuestion, "contents2");
        saveQuestion.addAnswer(expected);
        saveQuestion.addAnswer(expected2);

        em.flush();
        em.clear();

        Optional<Question> findQuestion = questionRepository.findById(saveQuestion.getId());
        findQuestion.ifPresent(actual -> {
            assertAll(
                    () -> assertThat(actual.getAnswers()).contains(expected),
                    () -> assertThat(actual.getAnswers()).contains(expected2),
                    () -> assertThat(actual.getAnswers()).hasSize(2)
            );
        });
    }

    @Test
    @DisplayName("질문 삭제시 답변도 같이 삭제 (cascade 사용X)")
    void delete_question_and_delete_all_answer() {
        Question saveQuestion = questionRepository.save(question);
        Answer expected = answerRepository.save(new Answer(saveQuestion.getWriter(), saveQuestion, "contents"));
        saveQuestion.addAnswer(expected);

        em.flush();
        em.clear();

        Optional<Question> findQuestion = questionRepository.findById(saveQuestion.getId());
        findQuestion.ifPresent(q -> q.delete(saveQuestion.getWriter()));

        em.flush();
        em.clear();

        Optional<Question> deleteQuestion = questionRepository.findById(saveQuestion.getId());
        deleteQuestion.ifPresent(dq -> {
            assertAll(
                    () -> assertTrue(dq.isDeleted()),
                    () -> assertTrue(dq.getAnswers().get(0).isDeleted())
            );
        });
    }
}
