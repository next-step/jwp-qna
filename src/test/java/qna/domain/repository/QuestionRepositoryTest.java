package qna.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.entity.Question;
import qna.domain.entity.QuestionTest;
import qna.domain.entity.User;
import qna.domain.entity.UserTest;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(UserTest.USER_JAVAJIGI);
        user2 = userRepository.save(UserTest.USER_SANJIGI);

        QuestionTest.QUESTION_OF_JAVAJIGI.writeBy(user1);
        QuestionTest.QUESTION_OF_SANJIGI.writeBy(user2);

        question1 = questionRepository.save(QuestionTest.QUESTION_OF_JAVAJIGI);
        question2 = questionRepository.save(QuestionTest.QUESTION_OF_SANJIGI);
    }

    @Test
    public void exists() {
        assertAll(
            () -> assertThat(question1.getId()).isNotNull(),
            () -> assertThat(question2.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("동등성 비교")
    public void isEqualTo() {
        assertAll(
            () -> assertThat(question1).isEqualTo(QuestionTest.QUESTION_OF_JAVAJIGI),
            () -> assertThat(question2).isEqualTo(QuestionTest.QUESTION_OF_SANJIGI)
        );
    }

    @Test
    @DisplayName("작성자 비교")
    public void isWriterEqualTo() {
        assertAll(
            () -> assertThat(question1.isOwner(user1)).isTrue(),
            () -> assertThat(question2.isOwner(user2)).isTrue()
        );
    }

    @Test
    @DisplayName("삭제되지 않은 질문리스트 조회")
    public void findByDeletedFalse() {
        List<Question> deletedQuestions = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(deletedQuestions.size()).isEqualTo(2),
            () -> assertThat(deletedQuestions).contains(question1, question2)
        );
    }

    @Test
    @DisplayName("질문 삭제후 삭제되지 않은 질문리스트 조회")
    public void findByDeletedFalse2() throws CannotDeleteException {
        question1.deleted(user1);

        List<Question> deletedQuestions = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(deletedQuestions.size()).isEqualTo(1),
            () -> assertThat(deletedQuestions).doesNotContain(question1),
            () -> assertThat(deletedQuestions).contains(question2)
        );
    }

    @Test
    public void findByIdAndDeletedFalse() {
        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(question1.getId());

        assertAll(
            () -> assertThat(questionOptional).isNotEmpty(),
            () -> assertThat(questionOptional.get()).isEqualTo(question1)
        );
    }
}
