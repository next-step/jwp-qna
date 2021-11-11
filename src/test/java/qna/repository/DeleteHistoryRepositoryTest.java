package qna.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.AnswerTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.QuestionTest;
import qna.domain.User;

@DataJpaTest
class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@PersistenceContext
	public EntityManager em;

	private User user1 = new User( "tom1234", "password", "tom", "tom1232@slipp.net");
	private User user2 = new User( "ford5678", "password2", "ford", "ford1232@slipp.net");

	@BeforeEach
	public void setup(){
		em.persist(user1);
		em.persist(user2);
	}

	@Test
	public void save() {
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), user1,LocalDateTime.now());
		DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);
		assertThat(deleteHistory).isEqualTo(actual);
	}

	@Test
	public void findById(){
		DeleteHistory savedHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), user1,LocalDateTime.now()));
		Optional<DeleteHistory> findDeletedHistory = deleteHistoryRepository.findById(savedHistory.getId());
		assertThat(findDeletedHistory.get()).isEqualTo(savedHistory);
	}

	@Test
	public void findByAll(){
		DeleteHistory savedHistory1 = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), user1,LocalDateTime.now()));
		DeleteHistory savedHistory2 = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(), user1,LocalDateTime.now()));
		List<DeleteHistory> findDeletedHistories = deleteHistoryRepository.findAll();
		assertThat(findDeletedHistories.size()).isEqualTo(2);
		assertThat(findDeletedHistories.contains(savedHistory1)).isTrue();
		assertThat(findDeletedHistories.contains(savedHistory2)).isTrue();
	}

	@Test
	public void findByContentIdAndContentType(){
		DeleteHistory savedHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), user2,LocalDateTime.now()));
		List<DeleteHistory> findDeletedHistories = deleteHistoryRepository.findByContentIdAndContentType(QuestionTest.Q1.getId(),ContentType.QUESTION);

		List<DeleteHistory> filtedHistories = findDeletedHistories.stream().filter(it->savedHistory.equals(it)).collect(Collectors.toList());
		assertThat(filtedHistories.size()).isEqualTo(1);
		assertThat(filtedHistories.get(0).getDeleter()).isEqualTo(user2);
	}
}