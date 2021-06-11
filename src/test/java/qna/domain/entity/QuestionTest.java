package qna.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.domain.aggregate.DeleteHistoryGroup;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

@DisplayName("질문 테스트")
public class QuestionTest {

	private User 자바지기;
	private User 산지기;
	private Question 자바지기_질문;

	@BeforeEach
	void 초기화() {
		자바지기 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
		산지기 = User.generate(2L, "sanjigi", "password2", "name2", "sanjigi@slipp.net");
		자바지기_질문 = Question.generate(1L, "title1", "contents1").writeBy(자바지기);
	}

	@Test
	void 생성_아이디없음() {
		//given

		//when
		Question 산지기_질문 = Question.generate("산지기 질문 제목", "산지기 질문 내용");

		//then
		assertThat(산지기_질문).isNotNull();
	}

	@Test
	void 생성_아이디있음() {
		//given

		//when
		Question 산지기_질문 = Question.generate(2L, "산지기 질문 제목", "산지기 질문 내용");

		//then
		assertThat(산지기_질문).isNotNull();
	}

	@Test
	void 생성_제목없음_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> Question.generate(2L, null, "산지기 질문 내용"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_제목_공백문자열_예외발생() {
		//given

		//when
		String 공백문자열 = "";

		//then
		assertThatThrownBy(() -> Question.generate(2L, 공백문자열, "산지기 질문 내용"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_제목_100_byte_초과_예외발생() {
		//given

		//when
		String byte_초과_제목 = "가갸거겨고교구규그기나냐너녀노뇨누뉴느니다댜더뎌도됴두듀드디라랴러려";

		//then
		assertThatThrownBy(() -> Question.generate(2L, byte_초과_제목, "산지기 질문 내용"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 동일성() {
		//given
		Question 비교할_자바지기_질문 = Question.generate(1L, "title1", "contents1").writeBy(자바지기);
		Question 비교할_산지기_질문 = Question.generate(2L, "title2", "contents2").writeBy(산지기);

		//when

		//then
		assertThat(자바지기_질문.equals(비교할_자바지기_질문)).isTrue();
		assertThat(자바지기_질문.equals(비교할_산지기_질문)).isFalse();
	}

	@Test
	void 작성자_확인() {
		//given

		//when

		//then
		assertThat(자바지기_질문.isOwner(자바지기)).isTrue();
		assertThat(자바지기_질문.isOwner(산지기)).isFalse();
	}

	@Test
	void 작성자_변경() {
		//given

		//when
		자바지기_질문.writeBy(산지기);

		//then
		assertThat(자바지기_질문.isOwner(자바지기)).isFalse();
		assertThat(자바지기_질문.isOwner(산지기)).isTrue();
	}

	@Test
	void 답변_추가() {
		//given
		Answer 자바지기_답변 = Answer.generate(자바지기, 자바지기_질문, "Answers Contents1");

		//when
		자바지기_질문.addAnswer(자바지기_답변);

		//then
		assertAll(
			() -> assertThat(자바지기_질문.answers().size()).isEqualTo(1),
			() -> assertThat(자바지기_질문.answers().get(0).equals(자바지기_답변)).isTrue(),
			() -> assertThat(자바지기_답변.question().equals(자바지기_질문)).isTrue()
		);
	}

	@Test
	void 답변_제거() {
		//given
		Answer 자바지기_답변 = Answer.generate(자바지기, 자바지기_질문, "Answers Contents1");
		자바지기_질문.addAnswer(자바지기_답변);

		//when
		자바지기_질문.removeAnswer(자바지기_답변);

		//then
		assertThat(자바지기_질문.answers().size()).isEqualTo(0);
	}

	@Test
	void 삭제_여부() {
		//given

		//when
		boolean 변경_전_삭제여부 = 자바지기_질문.isDeleted();
		자바지기_질문.delete(자바지기);
		boolean 변경_후_삭제여부 = 자바지기_질문.isDeleted();

		//then
		assertThat(변경_전_삭제여부).isFalse();
		assertThat(변경_후_삭제여부).isTrue();
	}

	@Test
	void 삭제() {
		//given

		//when
		DeleteHistoryGroup 질문_삭제_이력_그룹 = 자바지기_질문.delete(자바지기);

		//then
		assertThat(질문_삭제_이력_그룹).isNotNull();
		assertThat(질문_삭제_이력_그룹.size()).isEqualTo(1);
	}

	@Test
	void 연관된_답변도_함께_삭제() {
		//given
		Answer.generate(1L, 자바지기, 자바지기_질문, "Answers Contents1");

		//when
		DeleteHistoryGroup 질문_삭제_이력_그룹 = 자바지기_질문.delete(자바지기);

		//then
		assertThat(질문_삭제_이력_그룹).isNotNull();
		assertThat(질문_삭제_이력_그룹.size()).isEqualTo(2);
	}

	@Test
	void 작성자가_다른_질문_삭제_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> 자바지기_질문.delete(산지기)).isInstanceOf(CannotDeleteException.class);
	}

	@Test
	void 작성자가_다른_답변도_함께_삭제_예외발생() {
		//given
		Answer.generate(1L, 산지기, 자바지기_질문, "Answers Contents1");

		//when

		//then
		assertThatThrownBy(() -> 자바지기_질문.delete(자바지기)).isInstanceOf(CannotDeleteException.class);
	}
}
