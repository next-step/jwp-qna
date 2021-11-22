package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
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

    private User findUser(User user) {
        Optional<User> foundUser = users.findByUserId(UserTest.JAVAJIGI.getUserId());
        return foundUser.orElseGet(() -> users.save(user));
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
        questions.delete(questionBefore); // 고아 객체(Answer) 삭제 고려


        Optional<Question> questionAfter = questions.findFirstByTitle(targetQuestionTitle);
        List<Answer> answerListAfter = answers.findAnswerByQuestionId(questionBefore.getId());

        assertAll(
            () -> assertThat(questionAfter).isNotPresent(),
            () -> assertThat(answerListAfter).isEmpty()
        );
    }
}
