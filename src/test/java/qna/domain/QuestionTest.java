package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
	@Autowired
	QuestionRepository questions;

	@Autowired
	UserRepository users;

	@Test
	@DisplayName("연관관계 제거")
	void delete_writer() {
		User JAVAJIGI = makeJavajigi();
		User saveJavajigi = users.save(JAVAJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Question saveQ1 = questions.save(Q1);

		Question expected = questions.findById(saveQ1.getId()).get();
		expected.setWriter(null);

		assertThat(expected.getWriter()).isNull();
	}

	@Test
	@DisplayName("작성자 업데이트 확인")
	void update_writer() {
		User JAVAJIGI = makeJavajigi();
		User saveJavajigi = users.save(JAVAJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Question saveQ1 = questions.save(Q1);

		User SANJIGI = makeSanjigi();
		User saveSanjigi = users.save(SANJIGI);
		saveQ1.setWriter(saveSanjigi);

		Question expected = questions.findById(saveQ1.getId()).get();

		assertThat(expected.getWriter().getUserId()).isEqualTo("sanjigi");
	}

	@Test
	@DisplayName("user 를 만들지 않고 question 을 넣으면 에러 발생")
	void fk_error() {
		User JAVAJIGI = makeJavajigi();
		Question Q1 = new Question("title1", "contents1").writtenBy(JAVAJIGI);

		assertThatThrownBy(() -> questions.save(Q1)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("질문을 조회하고 해당 질문의 작성자를 조회함")
	void select_not_deleted_question_with_writer() {
		User JAVAJIGI = makeJavajigi();
		User saveJavajigi = users.save(JAVAJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Question saveQ1 = questions.save(Q1);

		Question question = questions.findByIdAndDeletedFalse(saveQ1.getId()).get();

		assertThat(question.getWriter().getUserId()).isEqualTo("javajigi");
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByTitleContainingOrderByIdDesc)")
	void select_start_with_order_by_id_desc() {
		User JAVAJIGI = makeJavajigi();
		User saveJavajigi = users.save(JAVAJIGI);
		User SANJIGI = makeSanjigi();
		User saveSanjigi = users.save(SANJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Question Q2 = new Question("title2", "contents2").writtenBy(saveSanjigi);

		Question saveQ1 = questions.save(Q1);
		Question saveQ2 = questions.save(Q2);

		assertThat(questions.findByTitleContainingOrderByIdDesc("title").get(0)).isEqualTo(
			questions.findById(saveQ2.getId()).get());
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByDeletedFalse)")
	void use_written_method_findByDeletedFalse() {
		questions.deleteAll();
		questions.flush();
		users.deleteAll();
		users.flush();

		User JAVAJIGI = makeJavajigi();
		User saveJavajigi = users.save(JAVAJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Q1.setDeleted(true);
		questions.save(Q1);
		assertThat(questions.findByDeletedFalse().size()).isEqualTo(0);

		Q1.setDeleted(false);
		assertThat(questions.findByDeletedFalse().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByIdAndDeletedFalse)")
	void use_written_method_findByIdAndDeletedFalse() {
		User JAVAJIGI = makeJavajigi();
		User saveJavajigi = users.save(JAVAJIGI);
		Question Q1 = new Question("title1", "contents1").writtenBy(saveJavajigi);
		Question saveQ1 = questions.save(Q1);

		assertThat(questions.findByIdAndDeletedFalse(saveQ1.getId())).isEqualTo(questions.findById(saveQ1.getId()));

		saveQ1.setDeleted(true);
		assertThat(questions.findByIdAndDeletedFalse(saveQ1.getId()).isPresent()).isFalse();
	}

	private User makeJavajigi() {
		return new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	}

	private User makeSanjigi() {
		return new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
	}
}
