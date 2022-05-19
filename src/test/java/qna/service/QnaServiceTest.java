package qna.service;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public void setUp() throws Exception {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    public void delete_성공() throws Exception {
        // given
        when(questionRepository.findByIdAndDeletedFalse(question.id())).thenReturn(Optional.of(question));

        // when
        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.id());

        // then
        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        // given & when
        when(questionRepository.findByIdAndDeletedFalse(question.id())).thenReturn(Optional.of(question));

        // then
        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI, question.id()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        // given
        when(questionRepository.findByIdAndDeletedFalse(question.id())).thenReturn(Optional.of(question));

        // when
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.id());

        // then
        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        // given & when
        Answer answer2 = new Answer(2L, UserTest.SANJIGI, QuestionRepositoryTest.Q1, "Answers Contents1");
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.id())).thenReturn(Optional.of(question));

        // then
        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.JAVAJIGI, question.id()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변 없는 질문에 대해 삭제가 성공한다.")
    void question_delete_01() throws CannotDeleteException {
        // given
        Question question = new Question(1L, "title", "content").writeBy(UserTest.JAVAJIGI);
        when(questionRepository.findByIdAndDeletedFalse(question.id())).thenReturn(Optional.of(question));

        // when
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.id());

        // then
        assertTrue(question.isDeleted());
        verifyDeleteHistoriesWhenNoAnswer(question);
    }

    private void verifyDeleteHistoriesWhenNoAnswer(Question question) {
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(question.id(), question.writer());
        verify(deleteHistoryService).saveAll(Collections.singletonList(deleteHistory));
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
            DeleteHistory.ofQuestion(question.id(), question.writer()),
            DeleteHistory.ofAnswer(answer.id(), answer.writer())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
