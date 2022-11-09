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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.answer.Answer;
import qna.domain.question.Question;
import qna.domain.question.QuestionTest;
import qna.domain.question.title.Title;
import qna.domain.repository.QuestionRepository;
import qna.domain.repository.UserRepository;
import qna.domain.user.UserTest;
import qna.domain.user.userid.UserId;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() {
        question = new Question(1L, new Title("title1"), "contents1").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    public void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(userRepository.findByUserId(new UserId(UserTest.JAVAJIGI.getUserId())))
                .thenReturn(Optional.of(UserTest.JAVAJIGI));
        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI.getUserId(), question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(userRepository.findByUserId(new UserId(UserTest.SANJIGI.getUserId())))
                .thenReturn(Optional.of(UserTest.SANJIGI));
        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI.getUserId(), question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(userRepository.findByUserId(new UserId(UserTest.JAVAJIGI.getUserId())))
                .thenReturn(Optional.of(UserTest.JAVAJIGI));
        qnaService.deleteQuestion(UserTest.JAVAJIGI.getUserId(), question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() {
        Answer answer2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1");
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(userRepository.findByUserId(new UserId(UserTest.JAVAJIGI.getUserId())))
                .thenReturn(Optional.of(UserTest.JAVAJIGI));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.JAVAJIGI.getUserId(), question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(),
                        question.getCreatedAt()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), answer.getCreatedAt())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
