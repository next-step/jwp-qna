package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @DisplayName("writeBy로 질문자 설정 가능")
    @Test
    void writeByTest() {
        Question question = new Question("title", "contents").writeBy(JAVAJIGI);
        assertThat(question.getWriter()).isEqualTo(JAVAJIGI);
        assertThat(question.getWriter()).isNotEqualTo(SANJIGI);
    }

    @DisplayName("질문자 동일여부 테스트")
    @Test
    void isOwnerTest() {
        assertTrue(Q1.isOwner(JAVAJIGI));
        assertTrue(Q2.isOwner(SANJIGI));
        assertFalse(Q1.isOwner(SANJIGI));
        assertFalse(Q2.isOwner(JAVAJIGI));
    }

    @DisplayName("질문에 답변 추가 테스트")
    @Test
    void addAnswerTest() {

        Answer answer1 = new Answer(1L, JAVAJIGI, Q1, "contents");
        Answer answer2 = new Answer(2L, JAVAJIGI, Q1, "contents");
        Answer answer3 = new Answer(3L, JAVAJIGI, Q1, "contents");

        Q1.addAnswer(answer1);
        Q1.addAnswer(answer2);
        Q1.addAnswer(answer3);
        Q1.addAnswer(answer3); // duplicate test

        assertThat(Q1.getAnswers()).hasSize(3)
                                   .contains(answer1, answer2, answer3);

        assertThat(answer1.getQuestion()).isEqualTo(Q1);
        assertThat(answer2.getQuestion()).isEqualTo(Q1);
        assertThat(answer3.getQuestion()).isEqualTo(Q1);
    }

    @DisplayName("질문 목록에서 답변 삭제 테스트")
    @Test
    void deleteAnswerTest() {

        Answer answer1 = new Answer(1L, JAVAJIGI, Q1, "contents");
        Answer answer2 = new Answer(2L, JAVAJIGI, Q1, "contents");

        Q1.addAnswer(answer1);
        Q1.addAnswer(answer2);
        Q1.deleteAnswer(answer2);

        assertThat(Q1.getAnswers()).hasSize(1)
                                   .contains(answer1)
                                   .doesNotContain(answer2);
    }
}
