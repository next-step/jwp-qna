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
import qna.domain.QuestionTest;
import qna.domain.UserTest;

@ExtendWith(MockitoExtension.class)
class QnAServiceTest {
	@Mock
	private QuestionRepository questionRepository;

	@Mock
	private AnswerRepository answerRepository;

	@Mock
	private DeleteHistoryService deleteHistoryService;

	@InjectMocks
	private QnAService qnAService;

	private Question question;
	private Answer answer;

	@BeforeEach
	public void setUp() throws Exception {
		question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
		answer = new Answer(UserTest.JAVAJIGI, question, "Answers Contents1");
		question.addAnswer(answer);
	}

	@Test
	public void delete_성공() throws Exception {
		when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

		assertThat(question.isDeleted()).isFalse();
		qnAService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

		assertThat(question.isDeleted()).isTrue();
		verifyDeleteHistories();
	}

	@Test
	public void delete_다른_사람이_쓴_글() throws Exception {
		when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

		assertThatThrownBy(() -> qnAService.deleteQuestion(UserTest.SANJIGI, question.getId()))
			.isInstanceOf(CannotDeleteException.class);
	}

	@Test
	public void delete_성공_질문자_답변자_같음() throws Exception {
		when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

		qnAService.deleteQuestion(UserTest.JAVAJIGI, question.getId());

		assertThat(question.isDeleted()).isTrue();
		assertThat(answer.isDeleted()).isTrue();
		verifyDeleteHistories();
	}

	@Test
	public void delete_답변_중_다른_사람이_쓴_글() throws Exception {
		Answer answer2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1");
		question.addAnswer(answer2);

		when(questionRepository.findByIdAndDeletedFalse(question.getId())).thenReturn(Optional.of(question));

		assertThatThrownBy(() -> qnAService.deleteQuestion(UserTest.JAVAJIGI, question.getId()))
			.isInstanceOf(CannotDeleteException.class);
	}

	private void verifyDeleteHistories() {
		List<DeleteHistory> deleteHistories = Arrays.asList(
			new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()),
			new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now())
		);
		verify(deleteHistoryService).saveAll(deleteHistories);
	}
}
