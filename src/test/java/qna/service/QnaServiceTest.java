package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @Autowired
    private UserRepository userRepository;

    @InjectMocks
    private QnaService qnaService;

    private Question question;
    private Answer answer;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() throws Exception {
        user1 = new User(1L,"javajigi", "password", "name", "javajigi@slipp.net");
        user2 = new User(2L, "leejun", "password", "name", "leejun@slipp.net");
        userRepository.save(user1);
        userRepository.save(user2);

        question = new Question(1L, "title1", "contents1").writeBy(user1);
        answer = new Answer(1L, user1, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    @DisplayName("삭제 성공")
    public void delete() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(user1, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    @DisplayName("다른 사람이 쓴글 삭제")
    public void delete_another_writer() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        assertThatThrownBy(() -> qnaService.deleteQuestion(user2, question.getId()))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문자와 답변자 같으면 삭제")
    public void delete_equl_writer() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        qnaService.deleteQuestion(user1, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    @DisplayName("답글 중 다른 사람이 쓴글 삭제")
    public void delete_another_writer_comment() {
        Answer answer2 = new Answer(2L, user1, question, "Answers Contents1");
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
            .thenReturn(Optional.of(question));
        qnaService.deleteQuestion(user1, question.getId());
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
            new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(),
                LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(),
                LocalDateTime.now())
        );
        verify(deleteHistoryService).saveAll(any());
    }
}
