package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerTest.*;

public class AnswersTest {

    @DisplayName("Answers일급컬렉션의 삭제기능 테스트")
    @Test
    public void deleteAnswers() throws CannotDeleteException {

        List<Answer> answerList = new ArrayList<>();
        answerList.add(A1);
        answerList.add(A1);

        Answers answers = Answers.create(answerList);

        DeleteHistories deleteHistories = answers.delete(UserTest.JAVAJIGI, LocalDateTime.now());

        assertThat(deleteHistories.getDeleteHistories()).hasSize(2);
    }

    @DisplayName("질문삭제시 댓글에 다른 작성자가 있을때 테스트")
    @Test
    public void whenExistAnotherUserDeleteAnswers() {

        List<Answer> answerList = new ArrayList<>();
        answerList.add(A1);
        answerList.add(A2);

        Answers answers = Answers.create(answerList);

        assertThatThrownBy(() -> {
            answers.delete(UserTest.JAVAJIGI, LocalDateTime.now());
        }).isInstanceOf(CannotDeleteException.class);
    }

}
