package qna.service;

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

    private User JAVAJIGI;
    private User SANJIGI;
    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        question = new Question(1L, JAVAJIGI, "title1", "contents1");
        answer = new Answer(1L, JAVAJIGI, question, "Answers Contents1");
    }

    @Test
    @DisplayName("로그인한 사용자가 질문 작성자 본인이면 질문 삭제 성공")
    public void delete_question_same_writer() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        assertThat(question.isDeleted()).isFalse();

        qnaService.deleteQuestion(JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    @DisplayName("로그인한 사용자가 질문 작성자 본인이 아니면 질문 삭제 실패")
    public void delete_question_diff_writer() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문에 답변이 있을 때, 로그인한 사용자 본인이 작성한 질문에 본인이 작성한 답변만 있다면 질문 삭제 성공")
    public void delete_question_with_answer_same_writer() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        assertThat(question.isDeleted()).isFalse();
        assertThat(answer.isDeleted()).isFalse();

        qnaService.deleteQuestion(JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    @DisplayName("질문에 답변이 있을 때, 로그인한 사용자 본인이 작성한 질문이더라도 다른 사람의 답변이 있다면 질문 삭제 실패")
    public void delete_question_with_answer_diff_writer() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        new Answer(2L, SANJIGI, question, "Answers Contents1");

        assertThatThrownBy(() -> qnaService.deleteQuestion(JAVAJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
