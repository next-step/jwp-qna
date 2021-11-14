package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
class DeleteHistoryTest {

	@Autowired
	EntityManager em;


	private User user;
	private Question question;

	@BeforeEach
	public void setUp() throws CannotDeleteException {
		user = new User( "tom1234", "password", "tom", "tom1232@slipp.net");
		em.persist(user);
		question = new Question(1L, "title1", "contents1").writeBy(user);
		question.delete(user);
	}


	@Test
	public void 엔티티_생성(){
		DeleteHistory deleteHistory = DeleteHistory.makeDeletedHistory(question, user);
		em.persist(deleteHistory);
		DeleteHistory findDleteHistory = em.createQuery("select d from DeleteHistory d where d.id=:id",DeleteHistory.class)
			.setParameter("id",deleteHistory.getId())
			.getSingleResult();

		assertThat(findDleteHistory).isEqualTo(deleteHistory);
	}
}