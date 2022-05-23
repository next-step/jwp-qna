package qna.service;

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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private User user1;
    private User user2;
    private Question question1;
    private Question question2;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        user1 = new User(10L, "javajigi", "password", "name", "javajigi@slipp.net");
        user2 = new User(11L, "sanjigi", "password", "name", "sanjigi@slipp.net");

        question1 = new Question(10L, user1, "title1", "contents1");
        question2 = new Question(11L, user2, "title2", "contents2");

        answer = new Answer(10L, user1, question1, "contents1");
    }

    @Test
    public void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question1.getId())).thenReturn(Optional.of(question1));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question1.getId())).thenReturn(Arrays.asList(answer));

        assertThat(question1.isDeleted()).isFalse();
        qnaService.deleteQuestion(user1, question1.getId());

        assertThat(question1.isDeleted()).isTrue();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question1.getId())).thenReturn(Optional.of(question1));

        assertThatThrownBy(() -> qnaService.deleteQuestion(user2, question1.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question1.getId())).thenReturn(Optional.of(question1));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question1.getId())).thenReturn(Arrays.asList(answer));

        qnaService.deleteQuestion(user1, question1.getId());

        assertThat(question1.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        Answer answer2 = new Answer(user2, question1, "contents1");
        question1.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question1.getId())).thenReturn(Optional.of(question1));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question1.getId())).thenReturn(Arrays.asList(answer, answer2));

        assertThatThrownBy(() -> qnaService.deleteQuestion(user1, question1.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question1.getId(), question1.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
