package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.*;

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

    User questionUser;
    Question question;
    User answerUser;
    Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);
        answerUser = new User("user2", "user2Pass", "User2", "user2@gmail.com");
        answer = new Answer(questionUser, question, "Answers Contents1");
    }

    @Test
    public void delete_성공() throws Exception {

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(questionUser, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(answerUser, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(questionUser, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        Answer anotherAnswer = new Answer(answerUser, question, "Answers Contents1");

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(questionUser, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), questionUser, LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), questionUser, LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
