package qna.service;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.DataJpaTestIncludeAuditing;
import static qna.assertions.QnaAssertions.*;
import qna.exception.CannotDeleteException;
import qna.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DataJpaTestIncludeAuditing
class QnaServiceTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    private QnaService qnaService;

    @Autowired
    private UserRepository userRepository;

    private User javajigi;

    private User sanjigi;

    private Question question;

    @BeforeEach
    public void setUp() throws Exception {
        qnaService = new QnaService(questionRepository,deleteHistoryService);

        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        sanjigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(javajigi);
        userRepository.save(sanjigi);

        question = new Question("title1", "contents1").writeBy(javajigi);
        questionRepository.save(question);
    }

    @Test
    public void 질문자와_로그인유저가_동일한경우_삭제가능() throws Exception {
        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(javajigi, question.getId());
        질문삭제여부_검증(question);
        삭제히스토리_검증(Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now())
       ));
    }

    @Test
    public void 질문자와_로그인유저가_다른경우_삭제불가(){
        ThrowingCallable deleteQuestion = () -> qnaService.deleteQuestion(sanjigi, question.getId());
        삭제불가_예외발생(deleteQuestion);
    }

    @Test
    public void 질문에_달린_답변이_없는경우_삭제가능() throws Exception {
        Question q = new Question("title2", "contents2").writeBy(javajigi);
        questionRepository.save(q);

        qnaService.deleteQuestion(javajigi, q.getId());

        질문삭제여부_검증(q);
        삭제히스토리_검증(Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, q.getId(), q.getWriter(), LocalDateTime.now())
        ));
    }

    @Test
    public void 질문자와_모든_답변자가_동일한경우_삭제가능() throws Exception {
        Answer answer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        질문에_답변추가(answer);

        qnaService.deleteQuestion(javajigi, question.getId());

        질문삭제여부_검증(question);
        삭제히스토리_검증(Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())
        ));
    }

    @Test
    public void 다른_사람이_쓴_답변이_있는경우_삭제불가() throws Exception {
        Answer answer = answerRepository.save(new Answer( sanjigi, question , "Answers Contents2"));
        question.addAnswer(answer);

        ThrowingCallable deleteQuestion = () -> qnaService.deleteQuestion(javajigi, question.getId());

        삭제불가_예외발생(deleteQuestion);
    }

    private void 질문에_답변추가(Answer answer){
        question.addAnswer(answer);
        answerRepository.save(answer);
    }

    private void 삭제히스토리_검증(List<DeleteHistory> deleteHistories){
        verify(deleteHistoryService).saveAll(deleteHistories);
    }

}
