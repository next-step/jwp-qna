package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.AnswerTest.*;
import static qna.domain.QuestionTest.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	AnswerRepository answerRepository;

	@PersistenceContext
	EntityManager entityManager;

	@Test
	void 입력된정보와_저장된정보가_동일한가() {

		// when
		Answer expected = answerRepository.save(A1);
		// then
		assertThat(expected.getWriterId()).isEqualTo(A1.getWriterId());
		assertThat(expected.getQuestionId()).isEqualTo(A1.getQuestionId());
		assertThat(expected.getContents()).isEqualTo(A1.getContents());
	}

	@Test
	void 삭제되지_않은_대상건중_한건을_true변경시_제외한_대상건만_조회() {

		// given
		List<Answer> answers = answerRepository.saveAll(Arrays.asList(A1, A2));
		Answer answer1 = answers.get(0);
		answer1.setDeleted(true);

		// when
		List<Answer> expectedAnswer = answerRepository.findByQuestionIdAndDeletedFalse(Q1.getId());
		// then
		assertThat(expectedAnswer.size()).isEqualTo(1);
		assertThat(expectedAnswer.get(0).getId()).isEqualTo(A2.getId());
		assertThat(A2.isDeleted()).isFalse();
	}

	@Test
	void 여러건을_저장할때_리스트에_데이터를_담는경우_동일한_객체라도_모두_저장된다() {
		List<Answer> answers = Arrays.asList(A1, A2, A1, A2, A1, A2, A1, A2);
		List<Answer> expectedAnswer = answerRepository.saveAll(answers);
		Assertions.assertThat(expectedAnswer.size()).isEqualTo(8);
	}

	@Test
	@DisplayName("기대값을 3건을 예상했지만 결과는 2건이다.")
	void 여러건을_저장할때_동일한_객체라면_동일한객체는_저장되지않는다() {

		Answer save = answerRepository.save(A1);
		System.out.println("save = " + save);
		Answer save1 = answerRepository.save(A2);
		System.out.println("save1 = " + save1);
		Answer save2 = answerRepository.save(A1);
		System.out.println("save2 = " + save2);

		List<Answer> expected = answerRepository.findAll();
		Assertions.assertThat(expected.size()).isEqualTo(2);
	}

	@AfterEach
	public void teardown() {
		this.answerRepository.deleteAll();
		this.entityManager
			.createNativeQuery("ALTER TABLE answer ALTER COLUMN `id` RESTART WITH 1")
			.executeUpdate();
	}
}
