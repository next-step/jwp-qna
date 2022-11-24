package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("질문 삭제 권한 없는 경우 예외처리")
    @Test
    void 질문삭제_권한_예외처리() {
        assertAll(
                () -> Q1.checkQuestionDeleteAuth(UserTest.JAVAJIGI),
                () -> assertThatThrownBy(() -> Q1.checkQuestionDeleteAuth(UserTest.SANJIGI))
                        .isInstanceOf(CannotDeleteException.class)
        );
    }

    @DisplayName("질문삭제 시 답변과 작성자 불일치하는 질문 존재 시 예외처리")
    @Test
    void 질문삭제_답변작성자_불일치_예외처리() {
        User user1 = new User("testUser", "qwerty1234", "김철수", "testUser@nextstep.com");
        User user2 = new User("testUser2", "qwerty12345", "김영희", "testUser2@nextstep.com");
        Question question = new Question("title111", "contents111").writeBy(user1);
        Answer answer1 = new Answer(user1, question, "Answers Contents1");
        Answer answer2 = new Answer(user2, question, "Answers Contents2");
        question.addAnswer(answer1);

        assertAll(
                question::deleteAnswersBeforeDeleteQuestion,
                () -> question.addAnswer(answer2),
                () -> assertThatThrownBy(question::deleteAnswersBeforeDeleteQuestion).isInstanceOf(CannotDeleteException.class)
        );
    }
}
