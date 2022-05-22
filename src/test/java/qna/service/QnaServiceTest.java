package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
import qna.domain.User;
import qna.repository.AnswerRepository;
import qna.repository.QuestionRepository;

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

    Question question;
    Answer answer;
    User javajigi;
    User sanjigi;
    LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        javajigi = User.builder("javajigi", "password", "name")
                .id(1L)
                .email("javajigi@slipp.net")
                .build();
        sanjigi = User.builder("sanjigi", "password", "name")
                .id(2L)
                .email("sanjigi@slipp.net")
                .build();
        question = Question.builder("title1")
                .id(1L)
                .contents("contents1")
                .build()
                .writeBy(javajigi);
        answer = Answer.builder(javajigi, question)
                .id(1L)
                .contents("Answers Contents1")
                .build();
        question.addAnswer(answer);
    }

    @DisplayName("작성자와 로그인 사용자가 같고 답변이 없는 질문 삭제 테스트")
    @Test
    void deleteSuccessNoneAnswer() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(Collections.emptyList());
        assertThat(question.isDeleted()).isFalse();
        LocalDateTime now = LocalDateTime.now();
        qnaService.deleteQuestion(javajigi, question.getId(), now);
        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistoriesAtDeleteSuccessNoneAnswer(now);
    }

    @DisplayName("다른 작성자의 질문 삭제시 예외 테스트")
    @Test
    public void deleteQuestionOtherWriter() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(sanjigi, question.getId(), now))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @DisplayName("로그인 유저와 질문, 답변이 같은 작성자인경우 질문, 답변글 삭제 테스트")
    @Test
    void deleteQuestionSameWriter() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(
                Collections.singletonList(answer));
        LocalDateTime now = LocalDateTime.now();
        qnaService.deleteQuestion(javajigi, question.getId(), now);
        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistoriesAtDeleteQuestionSameWriter(now);
    }

    @DisplayName("다른 사람의 답변이 있는 질문 삭제시 예외 테스트")
    @Test
    void deleteHavingContentsOfOther() {
        Answer answer = Answer.builder(sanjigi, question)
                .contents("Answers Contents1")
                .build();
        question.addAnswer(answer);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(this.question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(
                Arrays.asList(this.answer, answer));
        LocalDateTime now = LocalDateTime.now();
        assertThatThrownBy(() -> qnaService.deleteQuestion(javajigi, question.getId(), now))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    void verifyDeleteHistoriesAtDeleteQuestionSameWriter(LocalDateTime now) {
        List<DeleteHistory> deleteHistories = Collections.singletonList(
                DeleteHistory.builder()
                        .contentType(ContentType.ANSWER)
                        .contentId(answer.getId())
                        .deletedBy(answer.getWriter())
                        .createDate(now)
                        .build()
        );
        DeleteHistory deleteHistory = DeleteHistory.builder()
                .contentType(ContentType.QUESTION)
                .contentId(question.getId())
                .deletedBy(question.getWriter())
                .createDate(now)
                .build();
        verify(deleteHistoryService).save(deleteHistory);
        verify(deleteHistoryService).saveAll(deleteHistories);
    }

    void verifyDeleteHistoriesAtDeleteSuccessNoneAnswer(LocalDateTime now) {
        DeleteHistory deleteHistory = DeleteHistory.builder()
                .contentType(ContentType.QUESTION)
                .contentId(question.getId())
                .deletedBy(question.getWriter())
                .createDate(now)
                .build();
        verify(deleteHistoryService).save(deleteHistory);
    }
}
