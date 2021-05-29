package qna.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import qna.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class QnAServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnAService qnAService;

    private User loginUser;
    private User otherUser;

    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() {

        loginUser = new User(1L, "loginUser", "pwd", "name1", "email");
        otherUser = new User(2L, "loginUser", "pwd", "name1", "email");

        question = new Question(1L, "title1", "contents1").writeBy(loginUser);
        answer = new Answer(1L, loginUser, question, "Answers Contents1");

        question.addAnswer(answer);
    }

    @Test
    public void delete_성공() throws Exception {

        when(questionRepository.findById(eq(question.getId())))
            .thenReturn(Optional.of(question));

        when(userRepository.findById(eq(loginUser.getId())))
            .thenReturn(Optional.of(loginUser));

        assertThat(question.isDeleted()).isFalse();
        qnAService.deleteQuestion(loginUser.getId(), question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_찾을_수_없는_질문() {

        long notFoundQuestionId = 9999L;

        when(questionRepository.findById(eq(notFoundQuestionId)))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> qnAService.deleteQuestion(loginUser.getId(), notFoundQuestionId))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("질문을 찾을 수 없습니다.");
    }

    @Test
    public void delete_이미_삭제된_질문() {

        Question deletedQuestion = new Question(2L, "title", "contents").writeBy(loginUser);
        deletedQuestion.delete();

        when(questionRepository.findById(eq(deletedQuestion.getId())))
            .thenReturn(Optional.of(deletedQuestion));

        assertThatThrownBy(() -> qnAService.deleteQuestion(loginUser.getId(), deletedQuestion.getId()))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("이미 삭제된 질문입니다.");
    }

    @Test
    public void delete_다른_사람이_쓴_글() {

        when(questionRepository.findById(eq(question.getId())))
            .thenReturn(Optional.of(question));

        when(userRepository.findById(eq(otherUser.getId())))
            .thenReturn(Optional.of(otherUser));

        assertThatThrownBy(() -> qnAService.deleteQuestion(otherUser.getId(), question.getId()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    public void delete_성공_질문자_답변자_모두_같음() throws Exception {

        Answer answer2 = new Answer(2L, loginUser, question, "Answers Contents1");
        question.addAnswer(answer2);

        when(questionRepository.findById(eq(question.getId())))
            .thenReturn(Optional.of(question));

        when(userRepository.findById(eq(loginUser.getId())))
            .thenReturn(Optional.of(loginUser));

        qnAService.deleteQuestion(loginUser.getId(), question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories(answer2);
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() {

        Answer answer2 = new Answer(2L, otherUser, question, "Answers Contents1");
        question.addAnswer(answer2);

        when(questionRepository.findById(eq(question.getId())))
            .thenReturn(Optional.of(question));

        when(userRepository.findById(eq(loginUser.getId())))
            .thenReturn(Optional.of(loginUser));

        assertThatThrownBy(() -> qnAService.deleteQuestion(loginUser.getId(), question.getId()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    private void verifyDeleteHistories(Answer... additionalAnswers) {

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
        deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));

        if (additionalAnswers != null) {
            for (Answer additionalAnswer : additionalAnswers) {
                deleteHistories.add(
                    new DeleteHistory(ContentType.ANSWER, additionalAnswer.getId(), additionalAnswer.getWriter(), LocalDateTime.now())
                );
            }
        }

        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
