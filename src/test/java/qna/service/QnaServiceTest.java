package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.domain.answer.Answer;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.exception.CannotDeleteException;

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

    private User user1;
    private User user2;
    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() {
        user1 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        user2 = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        question = new Question(1L, "title1", "contents1").writeBy(user1);
        answer = new Answer(1L, question, user1, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(user1, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verify(deleteHistoryService).saveAll(question);
    }

    @Test
    void delete_다른_사람이_쓴_글() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(user2, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(user1, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verify(deleteHistoryService).saveAll(question);
    }

    @Test
    void delete_답변_중_다른_사람이_쓴_글() {
        Answer otherAnswer = new Answer(2L, question, user2, "Answers Contents1");
        question.addAnswer(otherAnswer);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(user1, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }
}
