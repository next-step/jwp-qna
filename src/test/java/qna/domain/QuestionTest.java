package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.util.assertions.QnaAssertions.답변삭제여부_검증;
import static qna.util.assertions.QnaAssertions.삭제불가_예외발생;
import static qna.util.assertions.QnaAssertions.질문삭제여부_검증;

import java.lang.reflect.Field;
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
    void setUp() throws Exception{
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        sanjigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        question = createQuestionWithId(new Question("title1", "contents1").writeBy(javajigi),1L);
    }

    private Question createQuestionWithId(Question question,Long id) throws Exception{
        Question newQuestion = new Question(question.getTitle(),question.getContents()).writeBy(question.getWriter());
        Class<Question> questionClass = Question.class;
        Field field = questionClass.getDeclaredField("id");
        field.setAccessible(true);
        field.set(newQuestion, id);
        return newQuestion;
    }

    @Test
    void 로그인유저와_질문자가_동일한경우_삭제가능() throws CannotDeleteException{
        List<DeleteHistory> deleteHistories = 로그인유저와_질문자가_동일한경우_삭제시도();

        질문삭제여부_검증(question);
        List<DeleteHistory> expected = Arrays.asList(
                DeleteHistoryFactory.createQuestionDeleteHistory(question));
        삭제히스토리_검증(expected,deleteHistories);
    }

    @Test
    void 로그인유저와_질문자가_다른경우_삭제불가(){
        ThrowingCallable tryDelete = () -> {
            로그인유저와_질문자가_다른경우_삭제시도();
        };
        삭제불가_예외발생(tryDelete);
    }

    @Test
    public void 질문에_달린_답변이_없는경우_삭제가능() throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = 로그인유저와_질문자가_동일한경우_삭제시도();

        질문에_답변이없는지_확인();
        질문삭제여부_검증(question);
        List<DeleteHistory> expected = Arrays.asList(
                DeleteHistoryFactory.createQuestionDeleteHistory(question)
        );
        삭제히스토리_검증(expected,deleteHistories);
    }

    @Test
    public void 질문자와_모든_답변자가_동일한경우_삭제가능() throws Exception {
        Answer a1 = 답변_생성(1L,javajigi, question, "Answers Contents1");
        Answer a2 = 답변_생성(2L, javajigi, question, "Answers Contents2");
        질문에_답변추가(a1);
        질문에_답변추가(a2);

        List<DeleteHistory> deleteHistories = 로그인유저와_질문자가_동일한경우_삭제시도();

        질문삭제여부_검증(question);
        답변삭제여부_검증(a1);
        답변삭제여부_검증(a2);
        List<DeleteHistory> expected = Arrays.asList(
                DeleteHistoryFactory.createQuestionDeleteHistory(question),
                DeleteHistoryFactory.createAnswerDeleteHistory(a1),
                DeleteHistoryFactory.createAnswerDeleteHistory(a2)
        );
        삭제히스토리_검증(expected,deleteHistories);
    }

    @Test
    public void 다른_사람이_쓴_답변이_있는경우_삭제불가() throws Exception {
        Answer a1 = 답변_생성(1L,javajigi, question, "Answers Contents1");
        Answer a2 = 답변_생성(2L, sanjigi, question, "Answers Contents2");
        질문에_답변추가(a1);
        질문에_답변추가(a2);

        ThrowingCallable tryDelete = () -> {
            로그인유저와_질문자가_동일한경우_삭제시도();
        };
        삭제불가_예외발생(tryDelete);
    }



    private List<DeleteHistory> 로그인유저와_질문자가_동일한경우_삭제시도() throws CannotDeleteException{
        return question.deleteByUser(javajigi);
    }

    private List<DeleteHistory> 로그인유저와_질문자가_다른경우_삭제시도() throws CannotDeleteException{
        return question.deleteByUser(sanjigi);
    }

    private void 질문에_답변이없는지_확인() {
        assertThat(question.getAnswers().size()).isEqualTo(0);
    }

    private Answer 답변_생성(Long id, User user, Question question, String contents) throws Exception{
        Answer answer = new Answer(user, question, contents);
        Class<Answer> answerClass = Answer.class;
        Field field = answerClass.getDeclaredField("id");
        field.setAccessible(true);
        field.set(answer, id);
        return answer;
    }

    private void 질문에_답변추가(Answer answer){
        question.addAnswer(answer);
    }

    private void 삭제히스토리_검증(List<DeleteHistory> expected, List<DeleteHistory> actual){
        assertThat(expected).isEqualTo(actual);
    }
}
