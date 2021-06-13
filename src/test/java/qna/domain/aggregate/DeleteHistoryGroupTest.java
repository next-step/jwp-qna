package qna.domain.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.entity.Answer;
import qna.domain.entity.DeleteHistory;
import qna.domain.entity.Question;
import qna.domain.entity.User;

@DisplayName("삭제_이력_그룹_테스트")
public class DeleteHistoryGroupTest {

	private User 자바지기;
	private Answer 자바지기_첫번째_답변;
	private Question 자바지기의_질문;

	@BeforeEach
	void 초기화() {
		자바지기 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
		자바지기의_질문 = Question.generate(1L, "title1", "contents1").writeBy(자바지기);
		자바지기_첫번째_답변 = Answer.generate(1L, 자바지기, 자바지기의_질문, "Answers Contents1");
	}

	@Test
	void 삭제이력그룹_생성() {
		//given

		//when
		DeleteHistoryGroup 삭제이력그룹 = DeleteHistoryGroup.generate();

		//then
		assertThat(삭제이력그룹).isNotNull();
	}

	@Test
	void 삭제이력그룹_사이즈() {
		//given
		DeleteHistoryGroup 삭제이력그룹 = 자바지기의_질문.delete(자바지기);

		//when
		int 삭제이력그룹_사이즈 = 삭제이력그룹.size();

		//then
		assertThat(삭제이력그룹_사이즈).isEqualTo(2);
	}

	@Test
	void 삭제이력그룹_이력들_반환() {
		//given
		DeleteHistoryGroup 삭제이력그룹 = 자바지기의_질문.delete(자바지기);

		//when
		List<DeleteHistory> 이력들 = 삭제이력그룹.deleteHistories();

		//then
		assertThat(이력들.size()).isEqualTo(2);
	}

	@Test
	void 삭제이력그룹_이력_추가() {
		//given
		DeleteHistoryGroup 삭제이력그룹 = DeleteHistoryGroup.generate();
		DeleteHistory 자바지기_답변_삭제_이력 = 자바지기_첫번째_답변.delete(자바지기);

		//when
		삭제이력그룹.add(자바지기_답변_삭제_이력);

		//then
		assertThat(삭제이력그룹.size()).isEqualTo(1);
	}

	@Test
	void 삭제이력그룹_이력들_추가() {
		//given
		DeleteHistoryGroup 삭제이력그룹 = DeleteHistoryGroup.generate();
		DeleteHistoryGroup 다른_삭제이력그룹 = 자바지기의_질문.delete(자바지기);

		//when
		다른_삭제이력그룹.addAll(삭제이력그룹);

		//then
		assertThat(다른_삭제이력그룹.size()).isEqualTo(2);
	}
}
