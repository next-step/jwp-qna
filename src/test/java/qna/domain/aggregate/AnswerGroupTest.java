package qna.domain.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.entity.Answer;
import qna.domain.entity.Question;
import qna.domain.entity.User;

@DisplayName("답변_그룹_테스트")
public class AnswerGroupTest {

	private User 자바지기;
	private Question 자바지기의_질문;
	private Answer 자바지기의_답변;

	@BeforeEach
	void 초기화() {
		자바지기 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
		자바지기의_질문 = Question.generate(1L, "질문 제목", "질문 내용").writeBy(자바지기);
		자바지기의_답변 = Answer.generate(1L, 자바지기, 자바지기의_질문, "답변 내용");
	}

	@Test
	void 답변_그룹_생성_테스트() {
		//given

		//when
		AnswerGroup 답변그룹 = AnswerGroup.generate();

		//then
		assertThat(Objects.isNull(답변그룹)).isFalse();
	}

	@Test
	void 답변들_반환_테스트() {
		//given
		AnswerGroup 답변그룹 = AnswerGroup.generate();

		//when

		//then
		assertThat(답변그룹.answers()).isInstanceOf(ArrayList.class);
	}

	@Test
	void 답변들_사이즈_반환_테스트() {
		//given
		AnswerGroup 답변그룹 = AnswerGroup.generate();
		답변그룹.add(자바지기의_답변);

		//when

		//then
		assertThat(답변그룹.size()).isEqualTo(1);
	}

	@Test
	void 답변들_비어있음_테스트() {
		//given
		AnswerGroup 답변그룹 = AnswerGroup.generate();

		//when

		//then
		assertThat(답변그룹.isEmpty()).isTrue();
	}

	@Test
	void 답변들_추가_테스트() {
		//given
		AnswerGroup 답변그룹 = AnswerGroup.generate();

		//when
		답변그룹.add(자바지기의_답변);

		//then
		assertThat(답변그룹.isEmpty()).isFalse();
		assertThat(답변그룹.size()).isEqualTo(1);
	}

	@Test
	void 답변들_삭제_테스트() {
		//given
		AnswerGroup 답변그룹 = AnswerGroup.generate();
		답변그룹.add(자바지기의_답변);

		//when
		DeleteHistoryGroup 답변들_삭제_이력 = 답변그룹.deleteAll(자바지기);

		//then
		assertThat(답변들_삭제_이력.size()).isEqualTo(1);
	}

	@Test
	void 답변들_포함여부_테스트() {
		//given
		AnswerGroup 답변그룹 = AnswerGroup.generate();
		답변그룹.add(자바지기의_답변);
		AnswerGroup 비교할_답변그룹 = AnswerGroup.generate();
		비교할_답변그룹.add(자바지기의_답변);

		//when
		boolean 비교할_답변그룹_포함여부 = 답변그룹.containsAll(비교할_답변그룹);
		boolean 답변그룹_포함여부 = 비교할_답변그룹.containsAll(답변그룹);

		//then
		assertThat(비교할_답변그룹_포함여부).isTrue();
		assertThat(답변그룹_포함여부).isTrue();
	}

}
