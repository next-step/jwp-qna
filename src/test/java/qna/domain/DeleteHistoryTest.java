package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryTest {

	@Autowired
	EntityManager em;

	@Test
	public void 엔티티_생성(){
		User user = new User("jerry92k", "12345678","jerrykim","jerry@gmail.com");
		em.persist(user);
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION,QuestionTest.Q1.getId(), user, LocalDateTime
			.now());
		em.persist(deleteHistory);
		DeleteHistory findDleteHistory = em.createQuery("select d from DeleteHistory d where d.id=:id",DeleteHistory.class)
			.setParameter("id",deleteHistory.getId())
			.getSingleResult();

		assertThat(findDleteHistory).isEqualTo(deleteHistory);
	}
}