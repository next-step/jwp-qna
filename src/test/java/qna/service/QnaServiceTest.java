package qna.service;

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

import qna.repository.QuestionRepository;

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
    }

    @Test
    @DisplayName("로그인한 사용자와 질문글, 답변글 작성자와 모두 동일한 경우 제거 성공")
    public void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    @DisplayName("로그인한 사용자와 다른 사람이 작성한 글을 제거하는 경우 제거 실패")
    public void delete_실패_다른_사람이_쓴_글() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("로그인한 사용자가 작성한 글에 다른 사용자가 단 답변이 존재하는 경우 제거 실패")
    public void delete_답변_중_다른_사람이_쓴_글() {
        new Answer(2L, UserTest.SANJIGI, question, "Answers Contents1");

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("로그인한 사용자가 작성한 글에 다른 사용자가 단 제거된 답변이 존재하는 경우 제거 성공")
    public void delete_답변_중_다른_사람이_쓴_제거된_글() throws CannotDeleteException {
        Answer deletedAnswer = new Answer(2L, UserTest.SANJIGI, question, "Answers Contents1");
        deletedAnswer.delete(UserTest.SANJIGI);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer.isDeleted()).isFalse();
        assertThat(deletedAnswer.isDeleted()).isTrue();
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
