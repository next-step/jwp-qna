package qna.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.domain.code.ContentType;

@DisplayName("삭제 이력 테스트")
public class DeleteHistoryTest {

	private User 자바지기;
	private Answer 자바지기_답변;
	private Question 자바지기_질문;
	private DeleteHistory 자바지기_질문_삭제이력;

	@BeforeEach
	void 초기화() {
		자바지기 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
		자바지기_질문 = Question.generate(1L, "title1", "contents1").writeBy(자바지기);
		자바지기_답변 = Answer.generate(1L, 자바지기, 자바지기_질문, "Answers Contents1");
		자바지기_질문_삭제이력 = DeleteHistory.ofQuestion(자바지기_질문.id(), 자바지기_질문.writer());
	}

	@Test
	void 삭제이력_컨텐츠타입_반환() {
		//given

		//when
		ContentType 질문_삭제이력_컨텐츠타입 = 자바지기_질문_삭제이력.contentType();

		//then
		assertThat(질문_삭제이력_컨텐츠타입).isEqualTo(ContentType.QUESTION);
		assertThat(질문_삭제이력_컨텐츠타입).isNotEqualTo(ContentType.ANSWER);
	}

	@Test
	void 질문_삭제이력_생성() {
		//given

		//when
		DeleteHistory 질문_삭제이력 = DeleteHistory.ofQuestion(자바지기_질문.id(), 자바지기_질문.writer());

		//then
		assertThat(질문_삭제이력).isNotNull();
		assertThat(질문_삭제이력.contentType()).isEqualTo(ContentType.QUESTION);
		assertThat(질문_삭제이력.contentType()).isNotEqualTo(ContentType.ANSWER);
	}

	@Test
	void 답변_삭제이력_생성() {
		//given

		//when
		DeleteHistory 답변_삭제이력 = DeleteHistory.ofAnswer(자바지기_답변.id(), 자바지기_답변.writer());

		//then
		assertThat(답변_삭제이력).isNotNull();
		assertThat(답변_삭제이력.contentType()).isEqualTo(ContentType.ANSWER);
		assertThat(답변_삭제이력.contentType()).isNotEqualTo(ContentType.QUESTION);
	}

	@Test
	void 삭제이력_동일성() {
		//given
		DeleteHistory 비교할_자바지기_질문_삭제이력 = DeleteHistory.ofQuestion(자바지기_질문.id(), 자바지기_질문.writer());
		DeleteHistory 비교할_자바지기_답변_삭제이력 = DeleteHistory.ofAnswer(자바지기_답변.id(), 자바지기_답변.writer());

		//when

		//then
		assertAll(
			() -> assertThat(자바지기_질문_삭제이력.equals(비교할_자바지기_질문_삭제이력)).isTrue(),
			() -> assertThat(자바지기_질문_삭제이력.equals(비교할_자바지기_답변_삭제이력)).isFalse()
		);
	}
}
