package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.exception.CannotDeleteException;
import qna.answer.Answer;
import qna.deletehistory.DeleteHistory;
import qna.deletehistory.DeleteHistoryRepository;
import qna.domain.ContentType;
import qna.question.Question;
import qna.question.QuestionTest;
import qna.user.UserTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static qna.user.UserTest.JAVAJIGI;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {
    @Mock
    private DeleteHistoryRepository deleteHistoryRepository;

    @InjectMocks
    private QnaService qnaService;

    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        question = new Question("title1", "contents1", JAVAJIGI);
        answer = new Answer(JAVAJIGI, question, "Answers Contents1");
    }

    @Test
    public void delete_성공() throws Exception {
        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question);

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI, question))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question);

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        Answer answer2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1");
        question.addAnswer(answer2);

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.JAVAJIGI, question))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getUser()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getUser())
        );
        verify(deleteHistoryRepository).saveAll(deleteHistories);
    }
}
