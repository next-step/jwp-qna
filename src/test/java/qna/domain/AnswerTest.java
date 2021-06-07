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

	@Autowired
	private UserRepository users;

	@Autowired
	private QuestionRepository questions;

	@Test
	@DisplayName("답변을 조회하고 연관된 질문 및 작성자를 조회")
	void select_not_deleted_question_with_writer() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User saveJavajigi = users.save(JAVAJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Question saveQ1 = questions.save(Q1);
		Answer saveA1 = answers.save(new Answer(saveJavajigi, saveQ1, "Answers Contents1"));

		assertThat(saveA1.getWriter().getUserId()).isEqualTo("javajigi");
		assertThat(saveA1.getQuestion().getTitle()).isEqualTo("title1");
	}

	@Test
	@DisplayName("jpa 데이터 인서트")
	void save() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		User saveJavajigi = users.save(JAVAJIGI);
		User saveSanjigi = users.save(SANJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Question saveQ1 = questions.save(Q1);

		Answer saveA1 = answers.save(new Answer(saveJavajigi, saveQ1, "Answers Contents1"));
		Answer saveA2 = answers.save(new Answer(saveSanjigi, saveQ1, "Answers Contents2"));

		assertThat(answers.count()).isEqualTo(2);
	}

	@Test
	@DisplayName("jpa 데이터 인서트 후 동일성 확인")
	void is_same_object() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User saveJavajigi = users.save(JAVAJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Question saveQ1 = questions.save(Q1);
		Answer A1 = new Answer(saveJavajigi, saveQ1, "Answers Contents1");
		Answer saveA1 = answers.save(A1);

		assertThat(saveA1).isEqualTo(answers.findById(saveA1.getId()).get());
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByIdAndDeletedFalse)")
	void use_written_method_findByIdAndDeletedFalse() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User saveJavajigi = users.save(JAVAJIGI);
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		User saveSanjigi = users.save(SANJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Question saveQ1 = questions.save(Q1);
		Answer A1 = new Answer(saveJavajigi, saveQ1, "Answers Contents1");
		Answer saveA1 = answers.save(A1);
		Answer A2 = new Answer(saveSanjigi, saveQ1, "Answers Contents2");
		A2.setDeleted(true);
		Answer saveA2 = answers.save(A2);

		assertThat(saveA1).isEqualTo(answers.findByIdAndDeletedFalse(saveA1.getId()).get());
		assertThat(answers.findByIdAndDeletedFalse(saveA2.getId()).isPresent()).isFalse();
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByQuestionIdAndDeletedFalse)")
	void use_written_method_findByQuestionIdAndDeletedFalse() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User saveJavajigi = users.save(JAVAJIGI);
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		User saveSanjigi = users.save(SANJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Question saveQ1 = questions.save(Q1);
		Answer A1 = new Answer(saveJavajigi, saveQ1, "Answers Contents1");
		Answer saveA1 = answers.save(A1);
		Answer A2 = new Answer(saveSanjigi, saveQ1, "Answers Contents2");
		A2.setDeleted(true);
		Answer saveA2 = answers.save(A2);

		assertThat(answers.findByQuestionIdAndDeletedFalse(Q1.getId()).size()).isEqualTo(1);
		assertThat(saveA1).isEqualTo(answers.findByQuestionIdAndDeletedFalse(Q1.getId()).get(0));
		assertThat(answers.findByQuestionIdAndDeletedFalse(Q1.getId()).get(0)).isEqualTo(
			answers.findById(saveA1.getId()).get());
	}
}
