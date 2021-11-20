package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question q1;
    private Question q2;

    @BeforeEach
    void setUp() {
        q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("질문을 삭제한다")
    void deleteTest() {
        // given, when
        q1.delete(UserTest.JAVAJIGI);

        // then
        assertThat(q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제시 작성자가 다르면 예외를 출력한다")
    void notWriterTest() {
        assertThatThrownBy(() -> q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제시 작성자와 다른 답변자가 있으면 예외를 출력한다")
    void differentAnswerWriterTest() {
        q1.addAnswer(AnswerTest.A2);

        assertThatThrownBy(() -> q1.delete(UserTest.JAVAJIGI))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("삭제이력을 저장한다")
    void makeDeleteHistoryTest() {
        q1.addAnswer(AnswerTest.A1);

        System.out.println(q1.delete(UserTest.JAVAJIGI));

        assertThat(q1.delete(UserTest.JAVAJIGI).size()).isEqualTo(3);
    }
}
