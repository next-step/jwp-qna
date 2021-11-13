package qna.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.fixture.AnswerFixture;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.fixture.QuestionFixture;
import qna.domain.User;
import qna.fixture.UserFixture;

import java.util.Arrays;
import java.util.Collections;
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

    @DisplayName("delete 성공")
    @Test
    void delete_성공() throws Exception {
        User user = UserFixture.create(1L, "user");
        Question question = QuestionFixture.create(1L, "title", "contents", user);
        Answer answer = AnswerFixture.create(1L, user, question, "Answers Contents");
        question.addAnswer(answer);

        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
                .thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId()))
                .thenReturn(Collections.singletonList(answer));
        assertThat(question.isDeleted())
                .isFalse();

        qnaService.deleteQuestion(user, question.getId());

        assertThat(question.isDeleted())
                .isTrue();

        verifyDeleteHistories(question, answer);
    }

    @DisplayName("delete 다른 사람이 쓴 글")
    @Test
    void delete_다른_사람이_쓴_글() {
        User user1 = UserFixture.create(1L, "user1");
        User user2 = UserFixture.create(2L, "user2");
        Question question = QuestionFixture.create(1L, "title", "contents", user1);
        Answer answer = AnswerFixture.create(1L, user1, question, "Answers Contents");
        question.addAnswer(answer);

        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
                .thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(user2, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("delete 성공 질문자 답변자 같음")
    @Test
    void delete_성공_질문자_답변자_같음() throws Exception {
        User user = UserFixture.create(1L, "user");
        Question question = QuestionFixture.create(1L, "title", "contents", user);
        Answer answer = AnswerFixture.create(1L, user, question, "Answers Contents");
        question.addAnswer(answer);

        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
                .thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId()))
                .thenReturn(Collections.singletonList(answer));

        qnaService.deleteQuestion(user, question.getId());

        assertThat(question.isDeleted())
                .isTrue();
        assertThat(answer.isDeleted())
                .isTrue();

        verifyDeleteHistories(question, answer);
    }

    @DisplayName("delete 답변 중 다른 사람이 쓴 글")
    @Test
    void delete_답변_중_다른_사람이_쓴_글() {
        User user1 = UserFixture.create(1L, "user1");
        User user2 = UserFixture.create(2L, "user2");

        Question question = QuestionFixture.create(1L, "title", "contents", user1);

        Answer answer1 = AnswerFixture.create(1L, user1, question, "Answers Contents1");
        Answer answer2 = AnswerFixture.create(2L, user2, question, "Answers Contents2");
        question.addAnswer(answer1);
        question.addAnswer(answer2);

        when(questionRepository.findByIdAndDeletedFalse(question.getId()))
                .thenReturn(Optional.of(question));
        when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId()))
                .thenReturn(Arrays.asList(answer1, answer2));

        assertThatThrownBy(() -> qnaService.deleteQuestion(user1, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories(Question question, Answer answer) {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter())
        );

        verify(deleteHistoryService)
                .saveAll(deleteHistories);
    }
}
