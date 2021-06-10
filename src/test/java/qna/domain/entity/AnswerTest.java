package qna.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

@DisplayName("답변_테스트")
public class AnswerTest {

	private User 자바지기;
	private User 산지기;
	private Answer 자바지기_첫번째_답변;
	private Question 자바지기의_질문;

	@BeforeEach
	void 초기화() {
		자바지기 = User.generate(1L, "javajigi", "password1", "name1",
			"javajigi@slipp.net");
		산지기 = User.generate(2L, "sanjigi", "password2", "name2",
			"sanjigi@slipp.net");
		자바지기의_질문 = Question.generate(1L, "title1", "contents1").writeBy(자바지기);
		자바지기_첫번째_답변 = Answer.generate(1L, 자바지기, 자바지기의_질문,
			"Answers Contents1");
	}

	@Test
	void 답변_생성_아이디없음() {
		//given
		Answer 자바지기_두번째_답변 = Answer.generate(자바지기, 자바지기의_질문, "Answers Contents2");

		//when

		//then
		assertThat(자바지기_두번째_답변).isNotNull();
	}

	@Test
	void 답변_생성_아이디있음() {
		//given

		//when

		//then
		assertThat(자바지기_첫번째_답변).isNotNull();
	}

	@Test
	void 답변_생성_질문없음_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> Answer.generate(자바지기, null, "Answers Contents2"))
			.isInstanceOf(NotFoundException.class);
	}

	@Test
	void 답변_생성_작성자없음_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> Answer.generate(null, 자바지기의_질문, "Answers Contents2"))
			.isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	void 답변_동일성() {
		//given
		Answer 비교할_답변 = Answer.generate(1L, 자바지기, 자바지기의_질문, "Answers Contents1");
		Answer 산지기_답변 = Answer.generate(2L, 산지기, 자바지기의_질문, "Answers Contents2");

		//when

		//then
		assertAll(
			() -> assertThat(자바지기_첫번째_답변.equals(비교할_답변)).isTrue(),
			() -> assertThat(자바지기_첫번째_답변.equals(산지기_답변)).isFalse()
		);
	}

	@Test
	void 답변한_질문_변경() {
		//given
		Question 산지기의_질문 = Question.generate(2L, "title2", "contents2").writeBy(산지기);

		//when
		자바지기_첫번째_답변.changeQuestion(산지기의_질문);

		//then
		assertAll(
			() -> assertThat(자바지기_첫번째_답변.question().equals(산지기의_질문)).isTrue(),
			() -> assertThat(산지기의_질문.answers().size()).isEqualTo(1),
			() -> assertThat(산지기의_질문.answers().get(0).equals(자바지기_첫번째_답변)).isTrue()
		);
	}

	@Test
	void 작성자_여부() {
		//given

		//when

		//then
		assertAll(
			() -> assertThat(자바지기_첫번째_답변.isOwner(자바지기)).isTrue(),
			() -> assertThat(자바지기_첫번째_답변.isOwner(산지기)).isFalse()
		);
	}

	@Test
	void 답변_삭제() {
		//given

		//when
		DeleteHistory 답변_삭제_이력 = 자바지기_첫번째_답변.delete(자바지기);

		//then
		assertThat(자바지기_첫번째_답변.isDeleted()).isTrue();
		assertThat(답변_삭제_이력).isNotNull();
	}

	@Test
	void 답변_삭제_여부() {
		//given
		Answer 산지기_답변 = Answer.generate(2L, 산지기, 자바지기의_질문, "Answers Contents2");

		//when
		자바지기_첫번째_답변.delete(자바지기);

		//then
		assertThat(자바지기_첫번째_답변.isDeleted()).isTrue();
		assertThat(산지기_답변.isDeleted()).isFalse();
	}
}
