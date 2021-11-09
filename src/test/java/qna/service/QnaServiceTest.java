package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

@ExtendWith(MockitoExtension.class)
@DisplayName("Qna 서비스")
class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = Question.of(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer = Answer.of(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    void delete_성공() throws Exception {
        //given
        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
            .thenReturn(Optional.of(question));
        assertThat(question.isDeleted()).isFalse();

        //when
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        //then
        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    void delete_다른_사람이_쓴_글() {
        //given
        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
            .thenReturn(Optional.of(question));

        //when
        ThrowingCallable deleteQuestionCall = () -> qnaService
            .deleteQuestion(UserTest.SANJIGI, question.getId());

        //then
        assertThatThrownBy(deleteQuestionCall)
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void delete_성공_질문자_답변자_같음() throws Exception {
        //given
        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
            .thenReturn(Optional.of(question));

        //when
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        //then
        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    void delete_답변_중_다른_사람이_쓴_글() {
        //given
        Answer answerWrittenOtherPerson = Answer
            .of(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1");
        question.addAnswer(answerWrittenOtherPerson);

        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
            .thenReturn(Optional.of(question));

        //when
        ThrowingCallable deleteQuestionCall = () -> qnaService
            .deleteQuestion(UserTest.JAVAJIGI, question.getId());

        //then
        assertThatThrownBy(deleteQuestionCall)
            .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
            DeleteHistory.ofQuestion(question.getId(), question.getWriter()),
            DeleteHistory.ofAnswer(answer.getId(), answer.getWriter())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
