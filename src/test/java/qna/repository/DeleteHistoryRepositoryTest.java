package qna.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.AnswerTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@Test
	void save() {
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.SANJIGI.getId(),LocalDateTime.now());
		DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);
		assertThat(deleteHistory).isEqualTo(actual);
	}

	@Test
	public void findById(){
		DeleteHistory savedHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.SANJIGI.getId(),LocalDateTime.now()));
		Optional<DeleteHistory> findDeletedHistory = deleteHistoryRepository.findById(savedHistory.getId());
		assertThat(findDeletedHistory.get()).isEqualTo(savedHistory);
	}

	@Test
	public void findByAll(){
		DeleteHistory savedHistory1 = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.SANJIGI.getId(),LocalDateTime.now()));
		DeleteHistory savedHistory2 = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(), UserTest.JAVAJIGI.getId(),LocalDateTime.now()));
		List<DeleteHistory> findDeletedHistories = deleteHistoryRepository.findAll();
		assertThat(findDeletedHistories.size()).isEqualTo(2);
		assertThat(findDeletedHistories.contains(savedHistory1)).isTrue();
		assertThat(findDeletedHistories.contains(savedHistory2)).isTrue();
	}

	@Test
	public void findByDeletedById(){
		Long deletedById= UserTest.SANJIGI.getId();
		DeleteHistory savedHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), deletedById,LocalDateTime.now()));
		List<DeleteHistory> findDeletedHistories = deleteHistoryRepository.findByDeletedById(deletedById);
		assertThat(findDeletedHistories.contains(savedHistory)).isTrue();
	}
}