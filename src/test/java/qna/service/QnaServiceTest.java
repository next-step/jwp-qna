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
import qna.repository.AnswerRepository;
import qna.repository.QuestionRepository;
import qna.repository.QuestionRepositoryTest;
import qna.repository.UserRepositoryTest;

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
    public void setUp() {
        question = Question.builder("title1")
                .id(1L)
                .contents("contents1")
                .build()
                .writeBy(UserRepositoryTest.JAVAJIGI);
        answer = Answer.builder(UserRepositoryTest.JAVAJIGI, question)
                .id(1L)
                .contents("Answers Contents1")
                .build();
        question.addAnswer(answer);
    }

    @Test
    public void deleteSuccess() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(Arrays.asList(answer));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserRepositoryTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void deleteQuestionOtherWriter() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserRepositoryTest.SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void deleteQuestionSameWriter() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(Arrays.asList(answer));

        qnaService.deleteQuestion(UserRepositoryTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void deleteHavingContentsOfOther() {
        Answer answer2 = Answer.builder(UserRepositoryTest.SANJIGI, QuestionRepositoryTest.Q1)
                .id(2L)
                .contents("Answers Contents1")
                .build();
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(
                Arrays.asList(answer, answer2));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserRepositoryTest.JAVAJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                DeleteHistory.builder()
                        .contentType(ContentType.QUESTION)
                        .contentId(question.getId())
                        .deletedBy(question.getWriter())
                        .createDate(LocalDateTime.now())
                        .build(),
                DeleteHistory.builder()
                        .contentType(ContentType.ANSWER)
                        .contentId(answer.getId())
                        .deletedBy(answer.getWriter())
                        .createDate(LocalDateTime.now())
                        .build()
        );
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
