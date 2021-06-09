package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.ArrayList;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class AnswerGroupTest {

	@Test
	void 답변_그룹_생성_테스트() {
		//given

		//when
		AnswerGroup answerGroup = AnswerGroup.generate();

		//then
		assertAll(
			() -> assertThat(Objects.isNull(answerGroup)).isFalse(),
			() -> assertThat(answerGroup.answers()).isInstanceOf(ArrayList.class),
			() -> assertThat(answerGroup.size()).isEqualTo(0)
		);
	}

}
