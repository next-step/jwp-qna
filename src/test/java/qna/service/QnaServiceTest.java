package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.UserTest;
import qna.exception.CannotDeleteException;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    private Question question;
    private Answer answer;
    private Question noAnswerQuestion;

    @BeforeEach
    public void setUp() throws Exception {
        question = new Question("title1", "contents1", UserTest.JAVAJIGI);
        answer = new Answer(UserTest.JAVAJIGI, question, "Answers Contents1");
        noAnswerQuestion = new Question("title", "contents", UserTest.JAVAJIGI);
    }

    @Test
    public void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.of(question));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI, 1L);

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
            .thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI, question.getId()))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
            .thenReturn(Optional.of(question));

        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {

        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
            .thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI, question.getId()))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_답변이없는_질문_글() throws CannotDeleteException {
        when(questionRepository.findByIdAndDeletedFalse(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.of(noAnswerQuestion));

        assertThat(noAnswerQuestion.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI, 1L);

        assertThat(noAnswerQuestion.isDeleted()).isTrue();
        verifyDeleteHistoriesNoAnswer();
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
            new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()),
            new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }

    private void verifyDeleteHistoriesNoAnswer() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
            new DeleteHistory(ContentType.QUESTION, noAnswerQuestion.getId(), question.getWriter())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
