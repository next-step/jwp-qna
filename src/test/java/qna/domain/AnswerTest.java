package qna.domain;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	@PersistenceContext
	EntityManager entityManager;

	@Test
	void 객체가_동일하다() {

		// given
		entityManager.persist(A1);
		final Answer answer = entityManager.find(Answer.class, 1L);

		// when
		final Answer answer1 = entityManager.find(Answer.class, 1L);

		// then
		assertThat(answer).isSameAs(answer1);
		assertThat(answer).isEqualTo(answer1);
	}

	@Test
	void 객체가_동일하지않다() {

		// given
		entityManager.persist(A1);
		final Answer answer = entityManager.find(Answer.class, 1L);

		// when
		entityManager.flush();
		entityManager.clear();
		final Answer answer1 = entityManager.find(Answer.class, 1L);

		// then
		assertThat(answer).isNotSameAs(answer1);
		assertThat(answer).isNotEqualTo(answer1);
	}
}
