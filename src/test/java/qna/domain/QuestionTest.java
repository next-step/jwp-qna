package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
	@Autowired
	QuestionRepository questions;

	private Question Q1;
	private Question Q2;
	private User JAVAJIGI;
	private User SANJIGI;

	@BeforeEach
	void setUp() {
		JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
		Q2 = new Question("title2", "contents2").writeBy(SANJIGI);
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByTitleContainingOrderByIdDesc)")
	void select_start_with_order_by_id_desc() {
		questions.save(Q1);
		questions.save(Q2);

		assertThat(questions.findByTitleContainingOrderByIdDesc("title").get(0)).isEqualTo(
			questions.findById(2L).get());
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByDeletedFalse)")
	void use_written_method_findByDeletedFalse() {
		Q1.setDeleted(true);
		questions.save(Q1);
		assertThat(questions.findByDeletedFalse().size()).isEqualTo(0);

		Q1.setDeleted(false);
		assertThat(questions.findByDeletedFalse().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByIdAndDeletedFalse)")
	void use_written_method_findByIdAndDeletedFalse() {
		Question saveQ1 = questions.save(Q1);
		assertThat(questions.findByIdAndDeletedFalse(saveQ1.getId())).isEqualTo(
			questions.findById(saveQ1.getId()));

		saveQ1.setDeleted(true);
		assertThat(questions.findByIdAndDeletedFalse(saveQ1.getId()).isPresent()).isFalse();
	}
}
