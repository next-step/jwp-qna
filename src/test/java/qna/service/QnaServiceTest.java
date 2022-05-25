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
    @DisplayName("로그인한 사용자와 질문 작성자가 같은 경우 삭제 가능")
    public void delete_question_same_writer() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question1.getId())).thenReturn(Optional.of(question1));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question1.getId())).thenReturn(Arrays.asList(answer));

        assertThat(question1.isDeleted()).isFalse();
        qnaService.deleteQuestion(user1, question1.getId());

        assertThat(question1.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    @DisplayName("로그인한 사용자가 질문 작성자가 아닌 경우 질문 삭제 불가")
    public void delete_question_diff_writer() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question1.getId())).thenReturn(Optional.of(question1));

        assertThatThrownBy(() -> qnaService.deleteQuestion(user2, question1.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문에 답변이 있을 때, 질문 작성자와 모든 답변 작성자가 같다면 작성자 본인의 질문 삭제 가능")
    public void delete_question_with_answer_same_writer() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question1.getId())).thenReturn(Optional.of(question1));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question1.getId())).thenReturn(Arrays.asList(answer));

        qnaService.deleteQuestion(user1, question1.getId());

        assertThat(question1.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    @DisplayName("질문에 답변이 있을 때, 질문 작성자가 아닌 다른 사람의 답변이 있다면 작성자 본인의 질문 삭제 실패")
    public void delete_question_with_answer_diff_writer() throws Exception {
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
