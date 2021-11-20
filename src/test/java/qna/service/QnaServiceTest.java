package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import qna.common.exception.CannotDeleteException;
import qna.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import qna.domain.qna.Answer;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.qna.Contents;
import qna.domain.qna.Question;
import qna.domain.qna.AnswerRepository;
import qna.domain.qna.QuestionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    public void setUp() {
        question = new Question(QuestionPostTest.QUESTION_POST1).writeBy(UserTest.JAVAJIGI);
        answer = new Answer(UserTest.JAVAJIGI, question, Contents.of("Answers Contents1"));
        question.addAnswer(answer);
    }

    @Test
    public void delete_성공() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(
            Optional.of(question));

        assertThat(question.isDeleted()).isFalse();

        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(
            Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI, question.getId()))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(
            Optional.of(question));

        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() {
        Answer answer2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1,
            Contents.of("Answers Contents1"));
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(
            Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId()))
            .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
            DeleteHistory.OfAnswer(answer, UserTest.JAVAJIGI),
            DeleteHistory.OfQuestion(question, UserTest.JAVAJIGI));

        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
