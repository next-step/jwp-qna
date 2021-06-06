package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerTest.A2;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    public void 로그인사용자와_질문한사람이_다른경우__삭제할수없다(){
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void 답변이_있는경우__삭제할_수없다(){
        Q1.addAnswer(A2);

        assertThatThrownBy(() -> Q1.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void 질문을삭제하면_답변도삭제해야한다() throws Exception{
        Q2.addAnswer(A2);
        Q2.delete(UserTest.SANJIGI);
        assertThat(Q2.answers().answers().stream().allMatch(Answer::isDeleted)).isTrue();
    }

    @Test
    public void 삭제한질문과_답변들에대한_이력이남는다() throws Exception{
        Answer A1 = new Answer(UserTest.JAVAJIGI, Q1, "contents");
        Q1.addAnswer(A1);
        List<DeleteHistory> deleteHistories = Q1.delete(UserTest.JAVAJIGI);

        assertThat(deleteHistories).contains(
                new DeleteHistory(ContentType.QUESTION, Q1.getId(), Q1.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, A1.getId(), A1.getWriter(), LocalDateTime.now())
        );
    }

}
