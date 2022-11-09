package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private User writer;
    private User otherUser;
    private Question question;

    @BeforeEach
    void setUp() {
        writer = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        otherUser = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        question = new Question("title1", "contents1").writeBy(writer);
    }

    @Test
    @DisplayName("질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(true)로 변경한다.")
    void delete_삭제_상태로_변경() {
        question.delete();
        assertTrue(question.isDeleted());
    }

    @Test
    @DisplayName("로그인 사용자와 질문 작성자가 다른 경우 예외가 발생한다.")
    void delete_작성자가_아닌_경우() {
        assertThrows(ForbiddenException.class, () -> question.deleteByWriter(otherUser));
    }

    @Test
    @DisplayName("질문에 답변을 추가한다.")
    void addAnswer_답변_추가() {
        Answer answer = new Answer(writer, question, "Answers Contents1");
        question.addAnswer(answer);
        assertThat(question.getAnswers()).contains(answer);
    }

    @Test
    @DisplayName("질문에 답변이 있는 경우 질문 삭제를 시도하면 예외가 발생한다.")
    void delete_답변이_있는_경우() {
        Answer answer = new Answer(writer, question, "Answers Contents1");
        question.addAnswer(answer);
        assertThrows(IllegalStateException.class, () -> question.deleteByWriter(writer));
    }
}
