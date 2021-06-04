package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.entity.*;
import qna.domain.repository.QuestionRepository;
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

    private User owner;
    private User other;
    private Question myQuestion;
    private Answer myAnswer1;
    private Answer myAnswer2;

    @BeforeEach
    public void setUp() throws Exception {
        owner = UserTest.USER_JAVAJIGI;
        other = UserTest.USER_SANJIGI;
        myQuestion = new Question(1L, "title1", "contents1").writeBy(owner);

        myAnswer1 = new Answer(1L, owner, "Answers Contents1").toQuestion(myQuestion);
        myAnswer2 = new Answer(2L, owner, "Answers Contents2").toQuestion(myQuestion);
    }

    @Test
    public void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(myQuestion.getId())).thenReturn(Optional.of(myQuestion));

        assertThat(myQuestion.isDeleted()).isFalse();
        qnaService.deleteQuestion(owner, myQuestion.getId());

        assertThat(myQuestion.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(myQuestion.getId())).thenReturn(Optional.of(myQuestion));

        assertThatThrownBy(() -> qnaService.deleteQuestion(other, myQuestion.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(myQuestion.getId())).thenReturn(Optional.of(myQuestion));

        qnaService.deleteQuestion(owner, myQuestion.getId());

        assertThat(myQuestion.isDeleted()).isTrue();
        assertThat(myAnswer1.isDeleted()).isTrue();
        assertThat(myAnswer2.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        Answer otherAnswer = new Answer(3L, other, "Answers Contents1");
        otherAnswer.toQuestion(myQuestion);

        when(questionRepository.findByIdAndDeletedFalse(myQuestion.getId())).thenReturn(Optional.of(myQuestion));

        assertThatThrownBy(() -> qnaService.deleteQuestion(owner, myQuestion.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = myQuestion.getDeleteHistories();

        verify(deleteHistoryService).saveAll(deleteHistories);
    }
}
