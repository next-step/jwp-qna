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
import qna.domain.TestDummy;
import qna.domain.User;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {

    public static final User JAVAJIGI = withId(TestDummy.USER_JAVAJIGI, 1L);
    public static final User SANJIGI = withId(TestDummy.USER_SANJIGI, 2L);

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
        question = new Question(1L, "title1", "contents1").writeBy(JAVAJIGI);
        answer = new Answer(1L, JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    public void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(
            Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(
            Arrays.asList(answer));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(
            Optional.of(question));

        assertThatThrownBy(
            () -> qnaService.deleteQuestion(SANJIGI, question.getId()))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(
            Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(
            Arrays.asList(answer));

        qnaService.deleteQuestion(JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() {
        Answer answer2 = new Answer(2L, SANJIGI, TestDummy.QUESTION1,
            "Answers Contents1");
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(
            Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(
            Arrays.asList(answer, answer2));

        assertThatThrownBy(
            () -> qnaService.deleteQuestion(JAVAJIGI, question.getId()))
            .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
            new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(),
                LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(),
                LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }

    private static User withId(User user, Long id) {
        return new User(id, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }
}
