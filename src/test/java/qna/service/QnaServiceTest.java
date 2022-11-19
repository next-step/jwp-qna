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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    private User userA;
    private User userB;

    private Question questionFromA;

    private Answer answerFromA;

    @BeforeEach
    public void setUp() throws Exception {
        userA = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        userB = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        questionFromA = new Question(1L, "title1", "contents1").writeBy(userA);
        answerFromA = new Answer(1L, userA, questionFromA, "Answers Contents1");
        questionFromA.addAnswer(answerFromA);
    }

    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우, 해당 질문은 삭제 가능 해야 한다")
    @Test
    public void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(questionFromA.getId())).thenReturn(Optional.of(questionFromA));

        assertThat(questionFromA.isDeleted()).isFalse();
        qnaService.deleteQuestion(questionFromA.getWriter(), questionFromA.getId());

        assertThat(questionFromA.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @DisplayName("로그인 사용자와 질문한 사람이 다른 경우, 해당 질문은 삭제 불가능 해야 한다")
    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(questionFromA.getId())).thenReturn(Optional.of(questionFromA));

        assertThatThrownBy(() -> qnaService.deleteQuestion(userB, questionFromA.getId()))
            .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("본인 외에 다른 사람이 쓴 답변이 존재하지 않는 경우, 질문과 답변 모두 삭제 가능 해야 한다")
    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(questionFromA.getId())).thenReturn(Optional.of(questionFromA));

        qnaService.deleteQuestion(userA, questionFromA.getId());

        assertThat(questionFromA.isDeleted()).isTrue();
        assertThat(answerFromA.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @DisplayName("다른 사람이 쓴 답변이 존재하는 경우, 해당 질문은 삭제 불가능 해야 한다")
    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        questionFromA.addAnswer(new Answer(2L, userB, questionFromA, "Answers Contents1"));

        when(questionRepository.findByIdAndDeletedFalse(questionFromA.getId())).thenReturn(Optional.of(questionFromA));

        assertThatThrownBy(() -> qnaService.deleteQuestion(userA, questionFromA.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, questionFromA.getId(), questionFromA.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answerFromA.getId(), answerFromA.getWriter(), LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
