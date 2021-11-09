package qna.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Question;
import qna.domain.UserTest;

@DataJpaTest
class QuestionRepositoryTest {

	@Autowired
	QuestionRepository questionRepository;

	@Test
	public void save(){
		Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
		Question actual = questionRepository.save(question);
		assertThat(question).isEqualTo(actual);
	}

	@Test
	public void findByDeletedFalse(){
		Question savedQuestion = questionRepository.save(new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI));
		List<Question> findQuestions = questionRepository.findByDeletedFalse();
		assertThat(findQuestions.contains(savedQuestion)).isTrue();

		savedQuestion.setDeleted(true);
		questionRepository.save(savedQuestion);
		findQuestions = questionRepository.findByDeletedFalse();
		assertThat(findQuestions.contains(savedQuestion)).isFalse();
	}

	@Test
	public void findByIdAndDeletedFalse(){
		Question savedQuestion = questionRepository.save(new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI));
		Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());
		assertThat(findQuestion.isPresent()).isTrue();

		savedQuestion.setDeleted(true);
		questionRepository.save(savedQuestion);
		findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());
		assertThat(findQuestion.isPresent()).isFalse();
	}
}