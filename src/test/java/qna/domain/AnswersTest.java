package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.AnswerWrittenBySomeoneElseException;

public class AnswersTest {

    @DisplayName("다른 사용자의 답변을 삭제할 경우")
    @Test
    void invalidDeleteAnswers() {
        assertThatExceptionOfType(AnswerWrittenBySomeoneElseException.class)
            .isThrownBy(() -> {
                User loginUser = TestCreateFactory.createUser(1L);
                User anotherUser = TestCreateFactory.createUser(2L);
                Question question = TestCreateFactory.createQuestion(loginUser);
                Answer answer = TestCreateFactory.createAnswer(anotherUser, question);
                Answers answers = new Answers(Arrays.asList(answer));

                answers.delete(loginUser, LocalDateTime.now());
            }).withMessageMatching("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
