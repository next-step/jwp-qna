package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
	@Autowired
	private AnswerRepository answers;

	@Test
	@DisplayName("jpa 데이터 인서트")
	void save() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
		Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
		Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

		answers.save(A1);
		answers.save(A2);

		assertThat(answers.count()).isEqualTo(2);
	}

	@Test
	@DisplayName("jpa 데이터 인서트 후 동일성 확인")
	void is_same_object() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
		Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");

		Answer saveA1 = answers.save(A1);

		assertThat(saveA1).isEqualTo(answers.findById(saveA1.getId()).get());
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByIdAndDeletedFalse)")
	void use_written_method_findByIdAndDeletedFalse() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
		Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
		Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

		Answer saveA1 = answers.save(A1);
		A2.setDeleted(true);
		Answer saveA2 = answers.save(A2);

		assertThat(saveA1).isEqualTo(answers.findByIdAndDeletedFalse(saveA1.getId()).get());
		assertThat(answers.findByIdAndDeletedFalse(saveA2.getId()).isPresent()).isFalse();
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByQuestionIdAndDeletedFalse)")
	void use_written_method_findByQuestionIdAndDeletedFalse() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
		Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
		Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

		Answer saveA1 = answers.save(A1);
		A2.setDeleted(true);
		Answer saveA2 = answers.save(A2);

		assertThat(answers.findByQuestionIdAndDeletedFalse(Q1.getId()).size()).isEqualTo(1);
		assertThat(saveA1).isEqualTo(answers.findByQuestionIdAndDeletedFalse(Q1.getId()).get(0));
		assertThat(answers.findByQuestionIdAndDeletedFalse(Q1.getId()).get(0)).isEqualTo(
			answers.findById(saveA1.getId()).get());
	}
}
