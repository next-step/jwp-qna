package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import qna.repository.QuestionRepository;

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

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
    }

    @Test
    void delete_성공() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        assertThat(question.isDeleted()).isFalse();
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    void delete_다른_사람이_쓴_글() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.SANJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void delete_성공_질문자_답변자_같음() {
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        verifyDeleteHistories();
    }

    @Test
    void delete_답변_중_다른_사람이_쓴_글() {
        new Answer(2L, UserTest.SANJIGI, question, "Answers Contents1");

        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId()))
                .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteHistories() {
        List<DeleteHistory> deleteHistories = Arrays.asList(
                DeleteHistory.ofAnswer(answer.getId(), answer.getWriter()),
                DeleteHistory.ofQuestion(question.getId(), question.getWriter())
        );
        verify(deleteHistoryService).saveAll(new DeleteHistories(deleteHistories));
    }

    @Test
    void 답변_없는_질문_delete_성공() {
        //given
        Question question = TestQuestionFactory.create(UserTest.JAVAJIGI);
        questionRepository.save(question);
        List<DeleteHistory> deleteHistories = Arrays.asList(DeleteHistory.ofQuestion(question.getId(), question.getWriter()));

        //when
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        assertThat(question.isDeleted()).isFalse();
        assertThat(question.answersCount()).isEqualTo(0);
        qnaService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

        //then
        assertThat(question.isDeleted()).isTrue();
        verify(deleteHistoryService).saveAll(new DeleteHistories(deleteHistories));
    }

    @Test
    void 질문자와_답변자_달라_예외_발생하면_질문_삭제여부는_거짓() {
        //given
        User writer = TestUserFactory.create("sanjigi");
        User fakeWriter = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer1 = TestAnswerFactory.create(writer, question);
        Answer answer2 = TestAnswerFactory.create(fakeWriter, question);
        questionRepository.save(question);

        //when
        when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
        assertThat(question.isDeleted()).isFalse();
        assertThat(question.answersCount()).isEqualTo(2);
        assertThatThrownBy(() -> qnaService.deleteQuestion(writer, question.getId()))
                .isInstanceOf(CannotDeleteException.class);

        //then
        assertThat(question.isDeleted()).isFalse();
        assertThat(answer1.isDeleted()).isFalse();
    }
}
