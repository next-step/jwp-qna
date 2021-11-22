package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, Q1,
        "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, Q1,
        "Answers Contents2");

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    private User findUser(User user) {
        Optional<User> foundUser = users.findByUserId(UserTest.JAVAJIGI.getUserId());
        return foundUser.orElseGet(() -> users.save(user));
    }

    private Question findQuestion(Question question) {
        Optional<Question> foundQuestion = questions.findFirstByTitle(question.getTitle());
        return foundQuestion.orElseGet(() -> questions.save(question));
    }

    @Test
    @DisplayName("Answer 저장 테스트")
    void saveAnswer() {
        Question question = findQuestion(Q2);
        Answer expected = new Answer(findUser(UserTest.JAVAJIGI), question, "Answers Contents3");
        Answer actual = answers.save(expected);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
            // 관계성 체크
            () -> assertThat(actual.getQuestion()).isEqualTo(question),
            () -> assertThat(question.getLastAnswer()).isEqualTo(actual)
        );
    }

    @Test
    @DisplayName("Answer 조회 테스트")
    void findAnswerByWriterId() {
        // A1 을 조회
        Answer expected = A1;
        List<Answer> actual = answers.findAnswerByWriterId(expected.getWriter().getId());

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.size()).isGreaterThan(0),
            () -> assertThat(actual.get(0).getId()).isNotNull(),
            () -> assertThat(actual.get(0).getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("Answer 수정 테스트 - Contents 수정")
    void updateAnswer() {
        Answer answerBefore = answers.findAnswerByWriterId(A1.getWriter().getId()).get(0);

        // A2 contents 로 수정
        answerBefore.setContents(A2.getContents());

        Answer answerAfter = answers.findAnswerByWriterId(A1.getWriter().getId()).get(0);

        assertAll(
            () -> assertThat(answerAfter.getId()).isEqualTo(answerBefore.getId()),
            () -> assertThat(answerAfter.getContents()).isEqualTo(A2.getContents())
        );
    }

    @Test
    @DisplayName("Answer 수정 테스트 - mapping question 수정")
    void updateAnswerWithQuestion() {
        final Long targetWriterId = A1.getWriter().getId();
        Answer answerBefore = answers.findAnswerByWriterId(targetWriterId).get(0);

        // Answer의 question mapping Q1 -> Q2 로 수정
        Question questionBefore = findQuestion(Q1);
        Question questionAfter = findQuestion(Q2);
        answerBefore.setQuestion(questionAfter);

        Answer answerAfter = answers.findAnswerByWriterId(targetWriterId).get(0);

        assertAll(
            () -> assertThat(answerAfter.getQuestion()).isEqualTo(questionAfter),
            () -> assertThat(answerAfter).isEqualTo(questionAfter.getLastAnswer()),
            () -> assertThat(answerAfter).isNotEqualTo(questionBefore.getLastAnswer())
        );
    }

    @Test
    @DisplayName("Answer 삭제 테스트")
    void deleteAnswer() {
        Long targetAnswerWriterId = 1L;
        List<Answer> answerBefore = answers.findAnswerByWriterId(targetAnswerWriterId);

        answerBefore.forEach(answer -> {
            // 연관 관계 삭제 (메모리 sync)
            answer.setQuestion(null);
            // answer 삭제
            answers.delete(answer);
        });

        List<Answer> answerAfter = answers.findAnswerByWriterId(targetAnswerWriterId);

        assertThat(answerAfter).isEmpty();
    }

}
