package qna.repository;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User user1;
    private Question question;
    private Question question1;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("iamsojung", "password", "sojung", "email@gmail.com"));
        user1 = userRepository.save(new User("iamsojung1", "password1", "sojung1", "email1@gmail.com"));
        question = questionRepository.save(new Question("title", "contents").writeBy(user));
        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));

    }

    @Test
    @DisplayName("save 검증 테스트")
    void saveTest() {
        assertAll(
            () -> assertThat(question.getId()).isNotNull(),
            () -> assertThat(question.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(question.isOwner(user)).isTrue(),
            () -> assertThat(question.getTitle()).isEqualTo(question.getTitle())

        );
    }

    @Test
    @DisplayName("저장한 question 와 id로 조회한 question 이 같은지 동등성 테스트")
    void read() {
        Question findQuestion = questionRepository.findById(question.getId()).get();
        assertThat(question).isEqualTo(findQuestion);
    }


    @Test
    @DisplayName("질문 생성 후 1개만 삭제 시 남은 질문 개수 확인")
    void findByDeletedFalseTest() throws CannotDeleteException {
        question1.delete(user1);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertThat(findQuestions).containsExactlyInAnyOrder(question);
        assertThat(findQuestions).hasSize(1);
    }


    @Test
    @DisplayName("findByIdAndDeletedFalse 테스트")
    void findByIdAndDeletedFalse() {
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("질문 생성 후 모두 삭제 시 남은 개수 확인")
    void findQuestionNotDeletedCount() throws CannotDeleteException {
        question.delete(user);
        question1.delete(user1);
        List<Question> findQuestions = questionRepository.findByDeletedTrue();

        assertThat(findQuestions).contains(question, question1);
        assertThat(findQuestions).hasSize(2);
    }

    @Test
    @DisplayName("질문자가 답변과 질문이 같은 경우 삭제 확인")
    public void questionAnswerSameWriterDelete() throws CannotDeleteException {
        Answer answer = new Answer(1L, user, question, "Answers Contents1");
        question.addAnswer(answer);

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer.isDeleted()).isFalse();

        question.delete(user);

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자가 답변과 질문이 다른 경우 삭제 확인")
    public void questionAnswerDiffWriterDelete() {
        Answer answer = new Answer(1L, user, question, "Answers Contents1");

        question.addAnswer(answer);

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer.isDeleted()).isFalse();

        assertThatThrownBy(() -> {
            question.delete(user1);
        }).isInstanceOf(CannotDeleteException.class);

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer.isDeleted()).isFalse();
    }
}
