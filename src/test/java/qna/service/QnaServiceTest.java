package qna.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

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

	private Question question;
	private Answer answer;
	private Question Q1;
	private User JAVAJIGI;
	private User SANJIGI;

	@BeforeEach
	public void setUp() throws Exception {
		JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		question = new Question(1L, "title1", "contents1").writeBy(JAVAJIGI);
		answer = new Answer(1L, JAVAJIGI, question, "Answers Contents1");
		question.addAnswer(answer);
		Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
	}

	@Test
	public void delete_성공() throws Exception {
		when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
		when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(Arrays.asList(answer));

		assertThat(question.isDeleted()).isFalse();
		qnaService.deleteQuestion(JAVAJIGI, question.getId());

		assertThat(question.isDeleted()).isTrue();
		verifyDeleteHistories();
	}

	@Test
	public void delete_다른_사람이_쓴_글() throws Exception {
		when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

		assertThatThrownBy(() -> qnaService.deleteQuestion(SANJIGI, question.getId()))
			.isInstanceOf(CannotDeleteException.class);
	}

	@Test
	public void delete_성공_질문자_답변자_같음() throws Exception {
		when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
		when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(Arrays.asList(answer));

		qnaService.deleteQuestion(JAVAJIGI, question.getId());

		assertThat(question.isDeleted()).isTrue();
		assertThat(answer.isDeleted()).isTrue();
		verifyDeleteHistories();
	}

	@Test
	public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
		Answer answer2 = new Answer(2L, SANJIGI, Q1, "Answers Contents1");
		question.addAnswer(answer2);

		when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));
		when(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).thenReturn(
			Arrays.asList(answer, answer2));

		assertThatThrownBy(() -> qnaService.deleteQuestion(JAVAJIGI, question.getId()))
			.isInstanceOf(CannotDeleteException.class);
	}

	private void verifyDeleteHistories() {
		List<DeleteHistory> deleteHistories = Arrays.asList(
			new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriterId(), LocalDateTime.now()),
			new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriterId(), LocalDateTime.now())
		);
		verify(deleteHistoryService).saveAll(deleteHistories);
	}
}
