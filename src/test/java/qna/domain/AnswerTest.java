package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
    private Answer yangsAnswer;
    private Question question;
    private User yang;
    private User wooobo;

    @BeforeEach
    void setup() {
        yang = new User("yangsi", "password", "yang", "rhfpdk92@naver.com");
        wooobo = new User("wooobo", "password", "wooobo", "email@naver.com");

        question = new Question("title", "contents");
        yangsAnswer = new Answer(yang, question, "contents");
    }


    @Test
    void 질문자의_질문삭제_성공() throws CannotDeleteException {
        yangsAnswer.delete(yang);
        assertThat(yangsAnswer.isDeleted()).isTrue();
    }

    @Test
    void 질문자의_삭제가_아닌경우_실패() {
        assertThatThrownBy(() -> yangsAnswer.delete(wooobo))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
