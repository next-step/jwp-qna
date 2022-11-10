package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("질문자와 답변자가 다른 경우 - 질문에 답변이 존재하는 경우 삭제를 시도하면 예외가 발생한다.")
    void delete_질문자와_답변자가_다른_경우() {
        Answer answer1 = new Answer(writer, question, "Answers Contents1");
        Answer answer2 = new Answer(otherUser, question, "Answers Contents2");
        question.addAnswer(answer1);
        question.addAnswer(answer2);

        assertThrows(IllegalStateException.class, () -> question.deleteByWriter(writer));
    }

    @Test
    @DisplayName("질문자와 답변자가 같은 경우 - 질문자와 모든 답변자가 같은 경우 삭제가 가능하다.")
    void delete_질문자와_답변자가_같은_경우() {
        Answer answer1 = new Answer(writer, question, "Answers Contents1");
        Answer answer2 = new Answer(writer, question, "Answers Contents2");
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        question.deleteByWriter(writer);

        assertTrue(question.isDeleted());
    }

    @Test
    @DisplayName("질문 삭제가 가능한 경우 - 질문을 삭제할 때 답변 또한 삭제된다.")
    void delete_질문과_답변_삭제() {
        Answer answer1 = new Answer(writer, question, "Answers Contents1");
        Answer answer2 = new Answer(writer, question, "Answers Contents2");
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        question.deleteByWriter(writer);

        assertAll(() -> {
            assertTrue(question.isDeleted());
            assertTrue(answer1.isDeleted());
            assertTrue(answer2.isDeleted());
        });
    }

    @Test
    @DisplayName("질문을 삭제하는 경우 삭제 이력에 대한 정보를 생성한다.")
    void delete_삭제_이력_생성() {
        Answer answer1 = new Answer(writer, question, "Answers Contents1");
        Answer answer2 = new Answer(writer, question, "Answers Contents2");
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        DeleteHistories deleteHistories = question.deleteByWriter(writer);

        assertThat(deleteHistories.values()).hasSize(3);
    }
}
