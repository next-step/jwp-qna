package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DomainTestFactory.*;

class AnswersTest {

    @Test
    @DisplayName("답변 삭제 테스트 : 삭제된 답변 1개, 삭제되지 않은 답변 1개")
    public void deleteTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        Answer answer2 = createAnswer(user, question);
        answer2.setDeleted(true);
        List<Answer> answerList = new ArrayList<>();
        answerList.add(answer);
        answerList.add(answer2);
        Answers answers = new Answers(answerList);

        assertThat(answers.delete(user).getDeleteHistories()).hasSize(1);
    }
}
