package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1")
        .writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private UserRepository users;

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    private Question question;
    private Answer answer;

    private User findUser(User user) {
        Optional<User> foundUser = users.findByUserId(UserTest.JAVAJIGI.getUserId());
        return foundUser.orElseGet(() -> users.save(user));
    }

    @BeforeAll()
    public void saveInitData() {
        users.save(UserTest.JAVAJIGI);
        users.save(UserTest.SANJIGI);
        questions.save(Q1);
        questions.save(Q2);
        answers.save(A1);
        answers.save(A2);
    }

    @BeforeEach
    public void setUp() throws Exception {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
    }

    @Test
    @DisplayName("Question 저장 테스트")
    void saveQuestion() {
        Question expected = new Question("title3", "contents3")
            .writeBy(findUser(UserTest.JAVAJIGI));
        Question actual = questions.save(expected);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("Question 조회 테스트")
    void findByTitle() {
        // Q1 조회
        Question expected = Q1;
        Optional<Question> actual = questions.findFirstByTitle(expected.getTitle());

        assertAll(
            () -> assertThat(actual).isPresent(),
            () -> assertThat(actual.get().getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("Question 수정 테스트 - Contents 수정")
    void updateQuestion() {
        Question questionBefore = questions.findFirstByTitle(Q1.getTitle()).get();

        // question 수정
        questionBefore.setContents(Q2.getContents());

        Question questionAfter = questions.findFirstByTitle(Q1.getTitle()).get();

        assertAll(
            () -> assertThat(questionAfter.getId()).isNotNull(),
            () -> assertThat(questionAfter.getContents()).isEqualTo(Q2.getContents())
        );
    }

    @Test
    @DisplayName("Question 삭제 테스트")
    void deleteQuestion() {
        String targetQuestionTitle = Q1.getTitle();
        Question questionBefore = questions.findFirstByTitle(targetQuestionTitle).get();

        // 연관 관계 객체 삭제
        List<Answer> answerListBefore = answers.findAnswerByQuestionId(questionBefore.getId());
        answerListBefore.forEach(answer -> {
            answers.delete(answer);
        });
        // question 삭제
        questions.delete(questionBefore);

        Optional<Question> questionAfter = questions.findFirstByTitle(targetQuestionTitle);
        List<Answer> answerListAfter = answers.findAnswerByQuestionId(questionBefore.getId());

        assertAll(
            () -> assertThat(questionAfter).isNotPresent(),
            () -> assertThat(answerListAfter).isEmpty()
        );
    }

    @Test
    @DisplayName("Question 삭제 테스트 - deleted=true 성공")
    public void deleteSuccess() throws Exception {
        assertThat(question.isDeleted()).isFalse();
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
        assertThat(deleteHistories).isNotEmpty();
    }

    @Test
    @DisplayName("Question 삭제 테스트 - deleted=true 실패")
    public void deleteFail() {
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }
}
