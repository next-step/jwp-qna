package qna.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
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
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

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
    public void setUp() {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    public void delete_성공() throws Exception {
        // given
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        // when
        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        // then
        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() {
        // given
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        // when & then
        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        // given
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        // when
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        // then
        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() {
        // given
        Answer answer2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1");
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        // when & then
        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_답변_없는_질문_삭제_성공() throws Exception {
        // given
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        // when
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        // then
        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistoriesWhenNoAnswer(question);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                DeleteHistory.ofQuestion(question.getId(), question.getWriter()),
                DeleteHistory.ofAnswer(answer.getId(), answer.getWriter())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }

    private void verifyDeleteHistoriesWhenNoAnswer(Question question) {
        DeleteHistory deleteHistory =
            DeleteHistory.ofQuestion(question.getId(), question.getWriter());
        verify(deleteHistoryService).saveAll(Collections.singletonList(deleteHistory));
    }
}
