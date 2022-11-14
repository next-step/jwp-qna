package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.domain.*;
import qna.exception.CannotDeleteException;
import qna.repository.AnswerRepository;
import qna.repository.QuestionRepository;

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

    private User writer;
    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        writer = UserTest.userSample(1L);
        question = QuestionTest.questionSample(1L, writer);
        answer = AnswerTest.answerSample(1L, writer, question);
        question.addAnswer(answer);
    }

    @Test
    public void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(writer, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        User otherUser = UserTest.userSample(2L);

        assertThatThrownBy(() -> qnaService.deleteQuestion(otherUser, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(writer, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        User otherUser = UserTest.userSample(2L);
        Question otherQuestion = new Question(2L, "title2", "contents");
        Answer answer2 = new Answer(2L, otherUser, otherQuestion, "Answers Contents1");
        this.question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(this.question.getId())).thenReturn(Optional.of(this.question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(writer, this.question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                DeleteHistory.ofQuestion(question),
                DeleteHistory.ofAnswer(answer)
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
