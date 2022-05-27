package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static qna.assertions.QnaAssertions.삭제불가_예외발생;
import static qna.assertions.QnaAssertions.질문삭제여부_검증;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.DataJpaTestIncludeAuditing;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

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
    void 질문조회(){
        Question foundQuestion = qnaService.findQuestionById(question.getId());
        assertThat(question).isSameAs(foundQuestion);
    }

    @Test
    public void 질문삭제_성공() throws Exception {
        qnaService.deleteQuestion(javajigi, question.getId());

        Question afterDeleted = questionRepository.findById(question.getId()).get();
        질문삭제여부_검증(afterDeleted);
        삭제히스토리_검증(Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now())
       ));
    }

    @Test
    public void 질문삭제_실패(){
        Answer answer = answerRepository.save(new Answer( sanjigi, question , "Answers Contents2"));
        질문에_답변추가(answer);

        ThrowingCallable deleteQuestion = () -> qnaService.deleteQuestion(sanjigi, question.getId());

        삭제불가_예외발생(deleteQuestion);
        Question questionAfterTryDelete = questionRepository.findById(question.getId()).get();
        Answer answerAfterTryDelete = answerRepository.findById(answer.getId()).get();

        assertThat(questionAfterTryDelete.isDeleted()).isFalse();
        assertThat(answerAfterTryDelete.isDeleted()).isFalse();
    }

    private void 질문에_답변추가(Answer answer){
        question.addAnswer(answer);
        answerRepository.save(answer);
    }

    private void 삭제히스토리_검증(List<DeleteHistory> deleteHistories){
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
