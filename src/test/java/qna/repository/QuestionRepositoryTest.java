package qna.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class QuestionRepositoryTest {

	@Autowired
	QuestionRepository questionRepository;

	@PersistenceContext
	public EntityManager em;

	private User user1 = new User( "tom1234", "password", "tom", "tom1232@slipp.net");

	@BeforeEach
	public void setup(){
		em.persist(user1);
	}
	
	@Test
	public void save(){
		Question question = new Question("title1", "contents1").writeBy(user1);
		Question actual = questionRepository.save(question);
		assertThat(question).isEqualTo(actual);
	}

	@Test
	public void findByDeletedFalse(){
		Question savedQuestion = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
		List<Question> findQuestions = questionRepository.findByDeletedFalse();
		assertThat(findQuestions.contains(savedQuestion)).isTrue();

		savedQuestion.delete();
		questionRepository.save(savedQuestion);
		findQuestions = questionRepository.findByDeletedFalse();
		assertThat(findQuestions.contains(savedQuestion)).isFalse();
	}

	@Test
	public void findByIdAndDeletedFalse(){
		Question savedQuestion = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
		Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());
		assertThat(findQuestion.isPresent()).isTrue();

		savedQuestion.delete();
		questionRepository.save(savedQuestion);
		findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());
		assertThat(findQuestion.isPresent()).isFalse();
	}
}