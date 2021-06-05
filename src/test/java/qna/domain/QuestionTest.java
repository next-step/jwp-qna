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
	QuestionRepository questionRepository;

	Question Q1;
	Question Q2;

	@BeforeEach
	void setUp() {
		Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
		Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByTitleContainingOrderByIdDesc)")
	void select_start_with_order_by_id_desc() {
		questionRepository.save(Q1);
		questionRepository.save(Q2);

		assertThat(questionRepository.findByTitleContainingOrderByIdDesc("title").get(0)).isEqualTo(
			questionRepository.findById(2L).get());
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByDeletedFalse)")
	void use_written_method_findByDeletedFalse() {
		Q1.setDeleted(true);
		questionRepository.save(Q1);
		assertThat(questionRepository.findByDeletedFalse().size()).isEqualTo(0);

		Q1.setDeleted(false);
		assertThat(questionRepository.findByDeletedFalse().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByIdAndDeletedFalse)")
	void use_written_method_findByIdAndDeletedFalse() {
		Question saveQ1 = questionRepository.save(Q1);
		assertThat(questionRepository.findByIdAndDeletedFalse(saveQ1.getId())).isEqualTo(
			questionRepository.findById(saveQ1.getId()));

		saveQ1.setDeleted(true);
		assertThat(questionRepository.findByIdAndDeletedFalse(saveQ1.getId()).isPresent()).isFalse();
	}
}
