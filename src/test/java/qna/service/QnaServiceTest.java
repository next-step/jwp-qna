package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.Contents;
import qna.domain.DateTimeStrategy;
import qna.domain.DeleteHistories;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.QuestionTest;
import qna.domain.Title;
import qna.domain.UserRepository;
import qna.domain.UserTest;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    @Mock
    private UserRepository userRepository;

    @Mock
    private DateTimeStrategy dateTimeStrategy;

    @InjectMocks
    private QnaService qnaService;

    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        question = new Question(1L, new Title("title1"), new Contents("contents1")).writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, new Contents("Answers Contents1"));
        question.addAnswer(answer);
        dateTimeStrategy = () -> LocalDateTime.now();
    }

    @Test
    public void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(userRepository.findById(UserTest.JAVAJIGI.getId())).thenReturn(Optional.of(UserTest.JAVAJIGI));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI.getId(), question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI.getId(), question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        when(userRepository.findById(UserTest.JAVAJIGI.getId())).thenReturn(Optional.of(UserTest.JAVAJIGI));

        qnaService.deleteQuestion(UserTest.JAVAJIGI.getId(), question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        Answer answer2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.JAVAJIGI.getId(), question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(Arrays.asList(
            new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriteBy(), dateTimeStrategy.getNowDateTime()),
            new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriteBy(), dateTimeStrategy.getNowDateTime())
        ));
        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
