package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", Contents.of("contents1")).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", Contents.of("contents2")).writeBy(UserTest.SANJIGI);

    @Test
    void 질문_삭제시_삭제상태가_False로_변경되야한다() throws Exception {
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        assertThat(question.isDeleted()).isFalse();

        question.delete(UserTest.JAVAJIGI);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    void 질문자와_다른_사용자가_질문을_삭제할_수_없다() throws Exception {
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 답변이_없는_경우_삭제_가능하다() throws Exception{
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(1L, UserTest.SANJIGI, question, Contents.of("Answers Contents1"));

        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");

    }
}