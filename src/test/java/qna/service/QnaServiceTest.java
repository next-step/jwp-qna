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

    private User javajigi;
    private User sanjigi;
    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() {
        javajigi = User.crate("javajigi", "password", "user1", "javajigi@slipp.net");
        sanjigi = User.crate("sanjigi", "password", "user2", "sanjigi@slipp.net");
        question = Question.create("title1", "contents1").writeBy(javajigi);
        answer = Answer.create(question, javajigi, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    void delete_성공() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(javajigi, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verify(deleteHistoryService).saveAll(question);
    }

    @Test
    void delete_다른_사람이_쓴_글() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(sanjigi, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void delete_성공_질문자_답변자_같음() throws Exception {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(javajigi, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verify(deleteHistoryService).saveAll(question);
    }

    @Test
    void delete_답변_중_다른_사람이_쓴_글() {
        Answer otherAnswer = Answer.create(question, sanjigi, "Answers Contents1");
        question.addAnswer(otherAnswer);

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(javajigi, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }
}
