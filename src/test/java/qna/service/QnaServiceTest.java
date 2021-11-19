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

    private Question question;
    private Answer answer;
    private User questionWriter;
    private User anotherWriter;

    @BeforeEach
    void setUp() {
        questionWriter = new User("questionWriter", "password", "lsh", "lsh@mail.com");
        question = new Question("title1", "contents1", questionWriter);
        answer = new Answer(questionWriter, question, "Answers Contents1");
        anotherWriter = new User("anotherWriter", "password", "lsh", "lsh@mail.com");
    }

    @Test
    void delete_성공() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionAndDeletedFalse(question)).thenReturn(Arrays.asList(answer));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(questionWriter, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    void delete_다른_사람이_쓴_글() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(anotherWriter, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void delete_성공_질문자_답변자_같음() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionAndDeletedFalse(question)).thenReturn(Arrays.asList(answer));

        qnaService.deleteQuestion(questionWriter, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    void delete_답변_중_다른_사람이_쓴_글() {
        Answer answer2 = new Answer(2L, anotherWriter, question, "Answers Contents1");

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionAndDeletedFalse(question)).thenReturn(Arrays.asList(answer, answer2));

        assertThatThrownBy(() -> qnaService.deleteQuestion(questionWriter, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now())

        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
