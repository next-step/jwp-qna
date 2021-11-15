package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class QuestionsTest {
	@Test
	void 여러개의질문을_생성하는지_확인() {
		Question question1 = new Question("공지사항","공지사항컨텐츠");
		Question question2 = new Question("자유시장","자유시장컨텐츠");
		Questions questions = new Questions(Arrays.asList(question1, question2));
		assertThat(questions).isEqualTo(new Questions(Arrays.asList(question1, question2)));
	}
}
