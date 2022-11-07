package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.*;
import qna.repository.AnswerRepository;
import qna.repository.QuestionRepository;

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
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    void delete_다른_사람이_쓴_글() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    void delete_답변_중_다른_사람이_쓴_글() {
        Answer answer2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1");
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                DeleteHistory.createQuestion(question.getId(), question.getWriter(), LocalDateTime.now()),
                DeleteHistory.createAnswer(answer.getId(), answer.getWriter(), LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(new DeleteHistories(deleteHistories));
    }
}
