package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;


    Question question;
    Answer answer;
    User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(new User("user", "password", "name", "email@email.com"));
        question = questionRepository.save(new Question("title", "contents")).writeBy(user);
        answer = new Answer(question.getWriter(), question, "contents");
    }

    @Test
    @DisplayName("답변 저장")
    void save_answer() {
        Answer actual = answerRepository.save(answer);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual).isEqualTo(answer);
    }

    @Test
    @DisplayName("답변 저장 후 조회 결과 동일성 체크")
    void find_by_id() {
        Answer expected = answerRepository.save(answer);
        Optional<Answer> findAnswer = answerRepository.findById(expected.getId());
        assertTrue(findAnswer.isPresent());
        findAnswer.ifPresent(actual -> assertThat(actual).isEqualTo(expected));
    }

    @Test
    @DisplayName("질문 ID로 삭제되지 않은 답변 리스트 조회")
    void find_by_question_id_and_deleted_false() {
        Answer expected = answerRepository.save(answer);
        List<Answer> actual = answerRepository.findByQuestionAndDeletedFalse(expected.getQuestion());
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual).contains(expected),
                () -> assertFalse(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("삭제되지 않은 답변 조회")
    void find_by_id_and_deleted_false() {
        Answer expected = answerRepository.save(answer);
        expected.delete();

        List<Answer> actual = answerRepository.findByQuestionAndDeletedFalse(expected.getQuestion());
        assertAll(
                () -> assertThat(actual).isEmpty(),
                () -> assertFalse(actual.contains(expected)),
                () -> assertTrue(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("답변의 주인이라면 true를 리턴")
    void is_owner_return_true() {
        Answer actual = answerRepository.save(answer);
        assertTrue(actual.isOwner(user));
    }

    @Test
    @DisplayName("질문 변경하기")
    void to_question() {
        Answer saveAnswer = answerRepository.save(answer);
        Question expected = questionRepository.save(new Question("question2", "contents2")).writeBy(user);
        saveAnswer.toQuestion(expected);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());
        assertTrue(findAnswer.isPresent());
        findAnswer.ifPresent(actual -> assertThat(actual.getQuestion()).isEqualTo(expected));
    }
}
