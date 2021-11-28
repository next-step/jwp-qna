package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @BeforeAll()
    public void saveInitData() {
        users.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("User 저장 테스트")
    void saveUser() {
        User savedUser = users.save(SANJIGI);

        assertAll(
            () -> assertThat(savedUser.getId()).isNotNull(),
            () -> assertThat(savedUser.getEmail()).isEqualTo(SANJIGI.getEmail())
        );
    }

    @Test
    @DisplayName("User 조회 테스트")
    void findByUserId() {
        // JAVAJIGI 조회
        User expected = JAVAJIGI;
        Optional<User> actual = users.findByUserId(expected.getUserId());

        assertAll(
            () -> assertThat(actual).isPresent(),
            () -> assertThat(actual.get().getId()).isNotNull(),
            () -> assertThat(actual.get().getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    @DisplayName("User 수정 테스트")
    void updateUser() {
        User userBefore = users.findByUserId(JAVAJIGI.getUserId()).get();

        // email 수정
        userBefore.setEmail(SANJIGI.getEmail());

        User userAfter = users.findByUserId(JAVAJIGI.getUserId()).get();

        assertAll(
            () -> assertThat(userAfter.getId()).isEqualTo(userBefore.getId()),
            () -> assertThat(userAfter.getEmail()).isEqualTo(SANJIGI.getEmail())
        );
    }

    @Test
    @DisplayName("User 삭제 테스트")
    void deleteUser() {
        final User targetUser = JAVAJIGI;

        User userBefore = users.findByUserId(targetUser.getUserId()).get();
        List<Answer> answerListBefore = answers.findAnswerByWriterId(JAVAJIGI.getId());
        List<Question> questionListBefore = questions.findQuestionsByWriterId(JAVAJIGI.getId());

        // 연관 관계 객체 삭제
        answerListBefore.forEach(answer -> {
            answers.delete(answer);
        });
        questionListBefore.forEach(question -> {
            List<Answer> answerListOfQuestion = answers.findAnswerByQuestionId(question.getId());
            answerListOfQuestion.forEach(answer -> {
                answers.delete(answer);
            });
            questions.delete(question);
        });

        // user 삭제
        users.delete(userBefore);

        Optional<User> userAfter = users.findByUserId(targetUser.getUserId());

        assertAll(
            () -> assertThat(userAfter).isNotPresent()
        );
    }
}
