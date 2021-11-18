package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User javajigi;

    private Question question;

    @BeforeEach
    void setup() {
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question("title1", "contents1").writeBy(javajigi);
    }

    @Test
    void save() {
        User savedUser = userRepository.save(javajigi);
        question.setWriter(savedUser);
        Question savedQuestion = questionRepository.save(question);

        assertAll(
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(savedQuestion.getContents()).isEqualTo(
                question.getContents()),
            () -> assertThat(savedQuestion.getWriter()).isEqualTo(question.getWriter()),
            () -> assertThat(savedQuestion.isDeleted()).isEqualTo(question.isDeleted()),
            () -> assertThat(savedQuestion.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void read() {
        User savedUser = userRepository.save(javajigi);
        question.setWriter(savedUser);
        Long savedId = questionRepository.save(question).getId();
        Question savedQuestion = questionRepository.findByIdAndDeletedFalse(savedId).get();

        assertAll(
            () -> assertThat(savedQuestion.getId()).isEqualTo(savedId),
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(savedQuestion.getContents()).isEqualTo(
                question.getContents()),
            () -> assertThat(savedQuestion.getWriter()).isEqualTo(question.getWriter()),
            () -> assertThat(savedQuestion.isDeleted()).isEqualTo(question.isDeleted()),
            () -> assertThat(savedQuestion.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    @DisplayName("질문을 저장하는 경우, 답변도 같이 저장된다.")
    void saveAfterAddAnswer() {
        User user1 = new User("user1", "password", "name", "email");
        User user2 = new User("user2", "password", "name", "email");
        userRepository.save(user1);
        userRepository.save(user2);
        Question question = new Question("title", "contents");
        question.setWriter(user1);
        questionRepository.save(question);

        Answer answer1 = new Answer(user1, question, "answer");
        Answer answer2 = new Answer(user2, question, "answer2");

        Long savedId = questionRepository.save(question).getId();

        Question savedQuestion = questionRepository.findByIdAndDeletedFalseWithAnswers(savedId)
            .get();

        assertThat(savedQuestion.getAnswers().getValues().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("답변 삭제후, 질문 저장")
    void 답변_삭제후_질문_저장() throws CannotDeleteException {
        User user1 = new User("user1", "password", "name", "email");
        userRepository.save(user1);
        Question question = new Question("title", "contents");
        question.setWriter(user1);
        questionRepository.save(question);

        Answer answer1 = new Answer(user1, question, "answer");
        Answer answer2 = new Answer(user1, question, "answer2");
        answer2.delete(user1, LocalDateTime.now());

        Long savedId = questionRepository.save(question).getId();

        Question savedQuestion = questionRepository.findByIdAndDeletedFalseWithAnswers(savedId)
            .get();

        assertThat((int) savedQuestion.getAnswers().getValues()
            .stream()
            .filter(answer -> !answer.isDeleted()).count()).isEqualTo(1);
    }
}
