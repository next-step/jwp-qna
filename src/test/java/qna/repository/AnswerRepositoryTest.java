package qna.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class AnswerRepositoryTest {

	@Autowired
	private AnswerRepository answerRepository;

	@PersistenceContext
	public EntityManager em;

	private User user = new User( "tom1234", "password", "tom", "tom1232@slipp.net");
	private Question question = new Question("title1", "contents1").writeBy(user);

	@BeforeEach
	public void setup(){
		em.persist(user);
		em.persist(question);
	}

	@Test
	void save() {
		Answer answer = new Answer(user, question, "Answer entity unit test");
		Answer actual = answerRepository.save(answer);
		assertThat(answer).isEqualTo(actual);
	}

	@Test
	void findById() {
		Answer savedAnswer = answerRepository.save(
			new Answer(user, question, "Answer entity unit test"));
		Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());
		assertThat(findAnswer.get()).isEqualTo(savedAnswer);
	}

	@Test
	void findByIdAndDeletedFalse() {
		Answer savedAnswer = answerRepository.save(
			new Answer(user, question, "Answer entity unit test"));
		Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());
		assertThat(findAnswer.get()).isEqualTo(savedAnswer);
	}

	@Test
	void findByIdAndDeletedTrue() {
		Answer savedAnswer = answerRepository.save(
			new Answer(user, question, "Answer entity unit test"));
		Optional<Answer> findNoAnswer = answerRepository.findByIdAndDeletedTrue(savedAnswer.getId());
		assertThat(findNoAnswer.isPresent()).isFalse();    /* 삭제되지 않았기 때문에 검색 안됨	*/

		savedAnswer.delete();
		answerRepository.save(savedAnswer);
		Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedTrue(savedAnswer.getId());
		assertThat(findAnswer.isPresent()).isTrue();
	}

	@Test
	void findByWriterIdAndQuestionId() {
		Answer savedAnswer = answerRepository.save(
			new Answer(user, question, "Answer entity unit test"));
		List<Answer> findAnswers = answerRepository.findByWriterIdAndQuestionId(UserTest.JAVAJIGI.getId(),
			QuestionTest.Q1.getId());
		assertThat(findAnswers.contains(savedAnswer));
	}

	@Test
	void findByContentsStartingWith() {
		String content="Answer entity unit test";
		Answer savedAnswer = answerRepository.save(
			new Answer(user, question, content));
		List<Answer> findAnswers = answerRepository.findByContentsStartingWith(content);
		assertThat(findAnswers.contains(savedAnswer));
	}

	@Test
	void findByCreatedAtBetween() {
		String content="Answer entity unit test";
		Answer savedAnswer = answerRepository.save(
			new Answer(user, question, content));
		List<Answer> findAnswers = answerRepository.findByCreatedAtBetween(LocalDateTime.now().minusMinutes(30),LocalDateTime.now().plusMinutes(30));
		assertThat(findAnswers.contains(savedAnswer));
	}
}