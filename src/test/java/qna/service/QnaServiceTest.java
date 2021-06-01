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

    @BeforeEach
    public void setUp() {
        question = new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
        answer = new Answer(UserRepositoryTest.JAVAJIGI, question, "Answers Contents1");

        ReflectionTestUtils.setField(question, "id", 1L);
        ReflectionTestUtils.setField(UserRepositoryTest.JAVAJIGI, "id", 1L);
        ReflectionTestUtils.setField(answer, "id", 1L);

        question.addAnswer(answer);
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

    }

    @Test
    public void delete_성공() throws Exception {
        //given
        when(answerRepository.findByQuestion(question)).thenReturn(Arrays.asList(answer));

        //when, then
        assertThat(question.isDeleted()).isFalse();

        //when
        qnAService.deleteQuestion(UserRepositoryTest.JAVAJIGI, question.getId());

        //then
        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() {
        //when, then
        assertThatThrownBy(() -> qnAService.deleteQuestion(UserRepositoryTest.SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        //given
        when(answerRepository.findByQuestion(question)).thenReturn(Arrays.asList(answer));

        //when
        qnAService.deleteQuestion(UserRepositoryTest.JAVAJIGI, question.getId());

        //then
        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() {
        //given
        Answer answer2 = new Answer(UserRepositoryTest.SANJIGI, question, "Answers Contents1");
        when(answerRepository.findByQuestion(question)).thenReturn(Arrays.asList(answer, answer2));

        //when, then
        assertThatThrownBy(() -> qnAService.deleteQuestion(UserRepositoryTest.JAVAJIGI, question.getId()))
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
