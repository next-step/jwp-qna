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

import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@PersistenceContext
	public EntityManager em;

	private User user1 = new User( "tom1234", "password", "tom", "tom1232@slipp.net");
	private User user2 = new User( "ford5678", "password2", "ford", "ford1232@slipp.net");
	private Question question;
	private Answer answer;

	@BeforeEach
	public void setUp() throws CannotDeleteException {
		user1 = new User( "tom1234", "password", "tom", "tom1232@slipp.net");
		user2 = new User( "ford5678", "password2", "ford", "ford1232@slipp.net");
		em.persist(user1);
		em.persist(user2);

		question = new Question(1L, "title1", "contents1").writeBy(user1);
		answer = new Answer(1L, user1, question, "Answers Contents1");
		question.addAnswer(answer);
		question.delete(user1);
	}

	@Test
	public void save() {
		DeleteHistory deleteHistory = DeleteHistory.makeDeletedHistory(question, user1);
		DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);
		assertThat(deleteHistory).isEqualTo(actual);
	}

	@Test
	public void findById(){
		DeleteHistory savedHistory = deleteHistoryRepository.save(DeleteHistory.makeDeletedHistory(question, user1));
		Optional<DeleteHistory> findDeletedHistory = deleteHistoryRepository.findById(savedHistory.getId());
		assertThat(findDeletedHistory.get()).isEqualTo(savedHistory);
	}

	@Test
	public void findByAll(){
		DeleteHistory savedHistory1 = deleteHistoryRepository.save(DeleteHistory.makeDeletedHistory(question, user1));
		DeleteHistory savedHistory2 = deleteHistoryRepository.save(DeleteHistory.makeDeletedHistory(answer, user1));
		List<DeleteHistory> findDeletedHistories = deleteHistoryRepository.findAll();
		assertThat(findDeletedHistories.size()).isEqualTo(2);
		assertThat(findDeletedHistories.contains(savedHistory1)).isTrue();
		assertThat(findDeletedHistories.contains(savedHistory2)).isTrue();
	}

	@Test
	public void findByContentIdAndContentType() {
		DeleteHistory savedHistory = deleteHistoryRepository.save(DeleteHistory.makeDeletedHistory(question, user2));
		List<DeleteHistory> findDeletedHistories = deleteHistoryRepository.findByContentIdAndContentType(question.getId(),ContentType.QUESTION);

		List<DeleteHistory> filtedHistories = findDeletedHistories.stream().filter(it->savedHistory.equals(it)).collect(Collectors.toList());
		assertThat(filtedHistories.size()).isEqualTo(1);
		assertThat(filtedHistories.get(0).getDeleter()).isEqualTo(user2);
	}
}