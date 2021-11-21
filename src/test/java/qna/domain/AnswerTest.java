package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.AnswerWrittenBySomeoneElseException;

@DataJpaTest
public class AnswerTest {

    @DisplayName("Answer 값 확인")
    @Test
    void init() {
        User user = TestCreateFactory.createUser(1L);
        Question question = TestCreateFactory.createQuestion(user);
        Answer answer = TestCreateFactory.createAnswer(user, question);

        assertThat(answer).isNotNull();
    }

    @DisplayName("로그인한 사용자의 답변 삭제")
    @Test
    void deleteAnswer() {
        User loginUser = TestCreateFactory.createUser(1L);
        Question question = TestCreateFactory.createQuestion(loginUser);
        Answer answer = TestCreateFactory.createAnswer(loginUser, question);

        answer.delete(loginUser, LocalDateTime.now());

        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("답변 삭제 시 로그인한 사용자의 답변이 아닐 경우")
    @Test
    void invalidDeleteAnswer() {
        assertThatExceptionOfType(AnswerWrittenBySomeoneElseException.class)
            .isThrownBy(() -> {
                User loginUser = TestCreateFactory.createUser(1L);
                User answerUser = TestCreateFactory.createUser(2L);
                Question question = TestCreateFactory.createQuestion(loginUser);
                Answer answer = TestCreateFactory.createAnswer(answerUser, question);

                answer.delete(loginUser, LocalDateTime.now());
            }).withMessageMatching("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
