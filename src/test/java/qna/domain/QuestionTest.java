package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.assertions.QnaAssertions.답변삭제여부_검증;
import static qna.assertions.QnaAssertions.삭제불가_예외발생;
import static qna.assertions.QnaAssertions.질문삭제여부_검증;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

public class QuestionTest {

    private User javajigi;
    private User sanjigi;
    private Question question;

    @BeforeEach
    void setUp(){
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        sanjigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        question = new Question(1L,"title1", "contents1").writeBy(javajigi);
    }

    @Test
    void 로그인유저와_질문자가_동일한경우_삭제가능() throws CannotDeleteException{
        List<DeleteHistory> deleteHistories = question.deleteByUser(javajigi);

        질문삭제여부_검증(question);
        List<DeleteHistory> expected = Arrays.asList(new DeleteHistory(ContentType.QUESTION,question.getId(),javajigi,LocalDateTime.now()));
        삭제히스토리_검증(expected,deleteHistories);
    }

    @Test
    void 로그인유저와_질문자가_다른경우_삭제불가(){
        ThrowingCallable tryDelete = () -> {
            question.deleteByUser(sanjigi);
        };
        삭제불가_예외발생(tryDelete);
    }

    @Test
    public void 질문에_달린_답변이_없는경우_삭제가능() throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = question.deleteByUser(javajigi);

        질문에_답변이없는지_확인();
        질문삭제여부_검증(question);
        List<DeleteHistory> expected = Arrays.asList(new DeleteHistory(ContentType.QUESTION,question.getId(),javajigi,LocalDateTime.now()));
        삭제히스토리_검증(expected,deleteHistories);
    }

    @Test
    public void 질문자와_모든_답변자가_동일한경우_삭제가능() throws Exception {
        Answer a1 = new Answer(1L,javajigi, question, "Answers Contents1");
        Answer a2 = new Answer(2L, javajigi, question, "Answers Contents2");
        질문에_답변추가(a1);
        질문에_답변추가(a2);

        List<DeleteHistory> deleteHistories = question.deleteByUser(javajigi);

        질문삭제여부_검증(question);
        답변삭제여부_검증(a1);
        답변삭제여부_검증(a2);
        List<DeleteHistory> expected = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION,question.getId(),javajigi,LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER,a1.getId(),javajigi,LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER,a2.getId(),javajigi,LocalDateTime.now())
        );
        삭제히스토리_검증(expected,deleteHistories);
    }

    @Test
    public void 다른_사람이_쓴_답변이_있는경우_삭제불가() throws Exception {
        Answer a1 = new Answer(javajigi, question, "Answers Contents1");
        Answer a2 = new Answer(sanjigi, question, "Answers Contents2");
        질문에_답변추가(a1);
        질문에_답변추가(a2);

        ThrowingCallable tryDelete = () -> {
            question.deleteByUser(sanjigi);
        };
        삭제불가_예외발생(tryDelete);
    }

    private void 질문에_답변이없는지_확인() {
        assertThat(question.getAnswers().size()).isEqualTo(0);
    }

    private void 질문에_답변추가(Answer answer){
        question.addAnswer(answer);
    }

    private void 삭제히스토리_검증(List<DeleteHistory> expected, List<DeleteHistory> actual){
        assertThat(expected).isEqualTo(actual);
    }
}
