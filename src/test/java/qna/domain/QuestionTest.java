package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class QuestionTest {
	@Autowired
	private AnswerRepository answers;

	@Autowired
	QuestionRepository questions;

	@Autowired
	UserRepository users;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	@DisplayName("양방향 연관관계 확인")
	void convenience_method_test() {
		User saveJavajigi = saveJavajigi();
		Question saveQ1 = saveQ1(saveJavajigi());
		Answer answer = answers.save(new Answer(saveJavajigi, saveQ1, "Answers Contents1"));

		testEntityManager.clear();

		assertThat(questions.findAll().size()).isEqualTo(1);
		assertThat(questions.findAll().get(0).isContaioned(answer)).isTrue();
	}

	@Test
	@DisplayName("연관관계 제거")
	void delete_writer() {
		Question saveQ1 = saveQ1(saveJavajigi());

		Question expected = questions.findById(saveQ1.getId()).get();
		expected.setWriter(null);

		assertThat(expected.getWriter()).isNull();
	}

	@Test
	@DisplayName("작성자 업데이트 확인")
	void update_writer() {
		Question saveQ1 = saveQ1(saveJavajigi());
		saveQ1.setWriter(saveSanjigi());

		Question expected = questions.findById(saveQ1.getId()).get();

		assertThat(expected.getWriter().getUserId()).isEqualTo("sanjigi");
	}

	@Test
	@DisplayName("user 를 만들지 않고 question 을 넣으면 에러 발생")
	void fk_error() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		Question Q1 = new Question("title1", "contents1").writtenBy(JAVAJIGI);

		assertThatThrownBy(() -> questions.save(Q1)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("질문을 조회하고 해당 질문의 작성자를 조회함")
	void select_not_deleted_question_with_writer() {
		Question saveQ1 = saveQ1(saveJavajigi());

		Question question = questions.findByIdAndDeletedFalse(saveQ1.getId()).get();

		assertThat(question.getWriter().getUserId()).isEqualTo("javajigi");
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByTitleContainingOrderByIdDesc)")
	void select_start_with_order_by_id_desc() {
		Question saveQ1 = saveQ1(saveJavajigi());
		Question saveQ2 = saveQ2(saveSanjigi());

		assertThat(questions.findByTitleContainingOrderByIdDesc("title").get(0)).isEqualTo(
			questions.findById(saveQ2.getId()).get());
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByDeletedFalse)")
	void use_written_method_findByDeletedFalse() {
		User saveJavajigi = saveJavajigi();
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Q1.delete(true);
		questions.save(Q1);
		assertThat(questions.findByDeletedFalse().size()).isEqualTo(0);

		Q1.delete(false);
		assertThat(questions.findByDeletedFalse().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByIdAndDeletedFalse)")
	void use_written_method_findByIdAndDeletedFalse() {
		Question saveQ1 = saveQ1(saveJavajigi());

		assertThat(questions.findByIdAndDeletedFalse(saveQ1.getId())).isEqualTo(questions.findById(saveQ1.getId()));

		saveQ1.delete(true);
		assertThat(questions.findByIdAndDeletedFalse(saveQ1.getId()).isPresent()).isFalse();
	}

	private User saveJavajigi() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		return users.save(JAVAJIGI);
	}

	private User saveSanjigi() {
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		return users.save(SANJIGI);
	}

	private Question saveQ1(User user) {
		Question Q1 = new Question("title1", "contents1").writtenBy(user);
		return questions.save(Q1);
	}

	private Question saveQ2(User user) {
		Question Q2 = new Question("title2", "contents2").writtenBy(user);
		return questions.save(Q2);
	}

	private Answer saveAnswer1(User user, Question question) {
		return answers.save(new Answer(user, question, "Answers Contents1"));
	}

	private Answer saveAnswer2(User user, Question question) {
		return answers.save(new Answer(user, question, "Answers Contents2"));
	}
}
