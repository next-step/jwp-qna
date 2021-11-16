package qna.domain;

import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import qna.QuestionNotFoundException;
import qna.TypeNotFoundException;
import qna.UnAuthorizedException;

public class DeleteHistoryTest {

	@Test
	void ContentType_Null이면_예외() {
		Assertions.assertThatThrownBy(() -> {
				new DeleteHistory(null, Q1, JAVAJIGI, LocalDateTime.now());
			}).isInstanceOf(TypeNotFoundException.class)
			.hasMessageContaining("");
	}

	@Test
	void contentId가_Null이면_예외() {
		Assertions.assertThatThrownBy(() -> {
			new DeleteHistory(ContentType.QUESTION, null, JAVAJIGI, LocalDateTime.now());
		}).isInstanceOf(QuestionNotFoundException.class);
	}

	@Test
	void deletedById가_Null이면_예외() {
		Assertions.assertThatThrownBy(() -> {
			new DeleteHistory(ContentType.QUESTION, Q1, null, LocalDateTime.now());
		}).isInstanceOf(UnAuthorizedException.class);
	}
}
