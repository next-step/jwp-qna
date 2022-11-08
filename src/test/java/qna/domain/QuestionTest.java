package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import qna.exception.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1");
    public static final Question Q2 = new Question("title2", "contents2");

    private Question question;

    @BeforeEach
    void init() {
        question = Q1.writeBy(UserTest.JAVAJIGI);
        question.clearAnswers();
    }

    @Test
    @DisplayName("질문 삭제시 작성자가 다르면 삭제 불가")
    public void question_delete_exception_writer_not_same() {

        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문자가 같은 경우 삭제 가능")
    public void question_delete() throws CannotDeleteException {
        Question question1 = Q1.writeBy(UserTest.JAVAJIGI);
        question1.delete(UserTest.JAVAJIGI);

        assertThat(question1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문에 답변이 있을 때 질문자와 답변자가 다른 경우 예외")
    public void delete_question_answer_same() {

        question.addAnswer(AnswerTest.A2);

        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문에 답변이 있을 때 질문자와 답변자가 같은 경우 모두 삭제")
    public void delete() throws CannotDeleteException {
        question.addAnswer(AnswerTest.A1);

        List<DeleteHistory> deleteHistoryList = question.delete(UserTest.JAVAJIGI);

        assertThat(deleteHistoryList.size()).isEqualTo(2);
    }


}
