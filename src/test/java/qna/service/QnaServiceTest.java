package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.DataJpaTestIncludeAuditing;
import qna.exception.CannotDeleteException;
import qna.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DataJpaTestIncludeAuditing
class QnaServiceTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    private QnaService qnaService;

    @Autowired
    private UserRepository userRepository;

    private User javajigi;

    private User sanjigi;

    private Question question;

    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        qnaService = new QnaService(questionRepository,answerRepository,deleteHistoryService);

        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        sanjigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(javajigi);
        userRepository.save(sanjigi);

        question = new Question("title1", "contents1").writeBy(javajigi);
        questionRepository.save(question);

        answer = new Answer(javajigi, question, "Answers Contents1");
        question.addAnswer(answer);
        answerRepository.save(answer);
    }

    @Test
    public void delete_성공() throws Exception {
        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(javajigi, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_다른_사람이_쓴_글() throws Exception {
        assertThatThrownBy(() -> qnaService.deleteQuestion(sanjigi, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_성공_질문자_답변자_같음() throws Exception {
        qnaService.deleteQuestion(javajigi, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
        Answer answer2 = new Answer( sanjigi, question , "Answers Contents2");
        answerRepository.save(answer2);
        question.addAnswer(answer2);

        assertThatThrownBy(() -> qnaService.deleteQuestion(javajigi, question.getId()))
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
