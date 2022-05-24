package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question yangsQuestion;
    private User yang;
    private User wooobo;

    @BeforeEach
    void setup() {
        yang = new User("yangsi", "password", "yang", "rhfpdk92@naver.com");
        wooobo = new User("wooobo", "password", "wooobo", "email@naver.com");
        yangsQuestion = new Question("title", "contents", false).writeBy(yang);

    }

    @Test
    void 질문자의_질문삭제_성공() throws CannotDeleteException {
        yangsQuestion.delete(yang);
        assertThat(yangsQuestion.isDeleted()).isTrue();
    }

    @Test
    void 질문자의_삭제가_아닌경우_실패() {
        assertThatThrownBy(() -> yangsQuestion.delete(wooobo))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }
}
