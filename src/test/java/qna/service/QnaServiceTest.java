package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

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

    private User questionWriter;
    private User otherUser;

    private Question question;

    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        questionWriter = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        otherUser = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

        question = new Question(1L, "title1", "contents1")
            .writeBy(questionWriter);

        answer = new Answer(1L, questionWriter, question, "Answers Contents1");
    }

    @Test
    public void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionAndDeletedFalse(question)).thenReturn(Arrays.asList(answer));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(question.getWriter(), question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(otherUser, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionAndDeletedFalse(question)).thenReturn(Arrays.asList(answer));

        qnaService.deleteQuestion(questionWriter, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        final Answer answerFromOtheruser = new Answer(2L, otherUser, question, "Answers Contents1");

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionAndDeletedFalse(question)).thenReturn(Arrays.asList(answer, answerFromOtheruser));

        assertThatThrownBy(() -> qnaService.deleteQuestion(questionWriter, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
