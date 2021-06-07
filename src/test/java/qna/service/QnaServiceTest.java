package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

	public static final User JAVAJIGI = new User(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	@Mock
	private QuestionRepository questionRepository;

	@Mock
	private AnswerRepository answerRepository;

	@Mock
	private DeleteHistoryService deleteHistoryService;

	@InjectMocks
	private QnaService qnaService;

	private Question questionWrittenByJavajigi;
	private Answer answerWrittenByJavajigi;

	@BeforeEach
	public void setUp() throws Exception {
		questionWrittenByJavajigi = new Question(1L, "title1", "contents1").writeBy(JAVAJIGI);
		answerWrittenByJavajigi = new Answer(1L, JAVAJIGI, questionWrittenByJavajigi,
			"Answers Contents1");
		questionWrittenByJavajigi.addAnswer(answerWrittenByJavajigi);
	}

	@DisplayName("delete_성공")
	@Test
	public void deleteSuccess() throws Exception {
		when(questionRepository.findByIdAndDeletedFalse(questionWrittenByJavajigi.id()))
			.thenReturn(Optional.of(questionWrittenByJavajigi));
		when(answerRepository.findByQuestionAndDeletedFalse(questionWrittenByJavajigi))
			.thenReturn(Arrays.asList(answerWrittenByJavajigi));

		assertThat(questionWrittenByJavajigi.isDeleted()).isFalse();
		qnaService.deleteQuestion(JAVAJIGI, questionWrittenByJavajigi.id());

		assertThat(questionWrittenByJavajigi.isDeleted()).isTrue();
		verifyDeleteHistories();
	}

	@DisplayName("delete_다른_사람이_쓴_글")
	@Test
	public void deletePostWrittenByTheOthers() throws Exception {
		when(questionRepository.findByIdAndDeletedFalse(questionWrittenByJavajigi.id()))
			.thenReturn(Optional.of(questionWrittenByJavajigi));

		assertThatThrownBy(() -> qnaService.deleteQuestion(SANJIGI, questionWrittenByJavajigi.id()))
			.isInstanceOf(CannotDeleteException.class);
	}

	@DisplayName("delete_성공_질문자_답변자_같음")
	@Test
	public void deleteSamePostByQuestionerAndAnswerer() throws Exception {
		when(questionRepository.findByIdAndDeletedFalse(questionWrittenByJavajigi.id()))
			.thenReturn(Optional.of(questionWrittenByJavajigi));
		when(answerRepository.findByQuestionAndDeletedFalse(questionWrittenByJavajigi))
			.thenReturn(Arrays.asList(answerWrittenByJavajigi));

		qnaService.deleteQuestion(JAVAJIGI, questionWrittenByJavajigi.id());

		assertThat(questionWrittenByJavajigi.isDeleted()).isTrue();
		assertThat(answerWrittenByJavajigi.isDeleted()).isTrue();
		verifyDeleteHistories();
	}

	@DisplayName("delete_답변_중_다른_사람이_쓴_글")
	@Test
	public void deletePostingWrittenByTheOthers() throws Exception {
		Answer answer2 = new Answer(2L, SANJIGI, questionWrittenByJavajigi, "Answers Contents1");
		questionWrittenByJavajigi.addAnswer(answer2);

		when(questionRepository.findByIdAndDeletedFalse(questionWrittenByJavajigi.id()))
			.thenReturn(Optional.of(questionWrittenByJavajigi));
		when(answerRepository.findByQuestionAndDeletedFalse(questionWrittenByJavajigi)).thenReturn(
			Arrays.asList(answerWrittenByJavajigi, answer2));

		assertThatThrownBy(
			() -> qnaService.deleteQuestion(JAVAJIGI, questionWrittenByJavajigi.id()))
			.isInstanceOf(CannotDeleteException.class);
	}

	private void verifyDeleteHistories() {
		List<DeleteHistory> deleteHistories = Arrays.asList(
			new DeleteHistory(ContentType.QUESTION, questionWrittenByJavajigi.id(),
				questionWrittenByJavajigi
					.writer(),
				LocalDateTime.now()),
			new DeleteHistory(ContentType.ANSWER, answerWrittenByJavajigi.id(),
				answerWrittenByJavajigi
					.writer(), LocalDateTime.now())
		);
		verify(deleteHistoryService).saveAll(deleteHistories);
	}
}
