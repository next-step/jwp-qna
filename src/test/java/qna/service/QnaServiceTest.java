package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.*;
import qna.fixture.QuestionTestFixture;
import qna.fixture.UserTestFixture;

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

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, UserTestFixture.SANJIGI, "title1", "contents1").writeBy(UserTestFixture.JAVAJIGI);
        answer = new Answer(1L, UserTestFixture.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTestFixture.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    void delete_다른_사람이_쓴_글() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTestFixture.SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(UserTestFixture.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    void delete_답변_중_다른_사람이_쓴_글() {
        Answer answer2 = new Answer(2L, UserTestFixture.SANJIGI, QuestionTestFixture.Q1, "Answers Contents1");
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTestFixture.JAVAJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void delete_성공_답변_없는_질문() throws CannotDeleteException {
        User writer = UserTestFixture.JAVAJIGI;
        Question question = new Question(1L, writer, "title", "contents");
        questionRepository.save(question);
        DeleteHistories deleteHistories = new DeleteHistories(Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), writer, LocalDateTime.now())
        ));

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(writer, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verify(deleteHistoryService).saveAll(deleteHistories);
    }

    @Test
    void delete_질문자_답변자_다르면_삭제상태_false() {
        User writer = UserTestFixture.JAVAJIGI;
        User otherUser = UserTestFixture.SANJIGI;
        Question question = new Question(1L, writer, "title", "contents");
        Answer answer1 = new Answer(1L, writer, question, "answer1");
        Answer answer2 = new Answer(2L, otherUser, question, "answer2");
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        questionRepository.save(question);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        assertThat(question.isDeleted()).isFalse();
        assertThatThrownBy(() -> qnaService.deleteQuestion(writer, question.getId()))
                .isInstanceOf(CannotDeleteException.class);

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer1.isDeleted()).isFalse();
        assertThat(answer2.isDeleted()).isFalse();
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriteBy(), LocalDateTime.now()),
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriteBy(), LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(new DeleteHistories(deleteHistories));
    }
}
