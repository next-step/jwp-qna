package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import qna.CannotDeleteException;
import qna.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class QnAServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnAService qnAService;

    private Question question;
    private Answer answer;
    private User javajigi;
    private User sanjigi;

    @BeforeEach
    public void setUp() {
        javajigi = new User("javajigi", "1234", "javajigi", "a@email.com");
        sanjigi = new User("sanjigi", "1234", "sanjigi", "b@email.com");
        question = new Question("title1", "contents1").writeBy(javajigi);
        answer = new Answer(javajigi, question, "Answers Contents1");

        ReflectionTestUtils.setField(question, "id", 1L);
        ReflectionTestUtils.setField(javajigi, "id", 1L);
        ReflectionTestUtils.setField(answer, "id", 1L);

        question.addAnswer(answer);
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

    }

    @Test
    public void delete_성공() throws Exception {
        when(answerRepository.findByQuestion(question)).thenReturn(Arrays.asList(answer));

        assertThat(question.isDeleted()).isFalse();

        qnAService.deleteQuestion(javajigi, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() {
        //when, then
        assertThatThrownBy(() -> qnAService.deleteQuestion(sanjigi, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(answerRepository.findByQuestion(question)).thenReturn(Arrays.asList(answer));

        qnAService.deleteQuestion(javajigi, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() {
        Answer answer2 = new Answer(sanjigi, question, "Answers Contents1");
        when(answerRepository.findByQuestion(question)).thenReturn(Arrays.asList(answer, answer2));

        assertThatThrownBy(() -> qnAService.deleteQuestion(javajigi, question.getId()))
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
