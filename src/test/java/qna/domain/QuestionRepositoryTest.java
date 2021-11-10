package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

	@Autowired
	QuestionRepository questions;

	@Test
	@DisplayName("질문을 저장하면 ID가 존재해야 한다")
	public void saveTest(){
		// given
		Question question1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

		// when
		questions.save(question1);

		// then
		assertThat(question1.getId()).isNotNull();
	}

	@Test
	@DisplayName("삭제되지 않은 질문들을 조회할 수 있어야 한다")
	public void findByDeletedFalseTest(){
		// given
		Question question1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
		question1.setDeleted(true);
		Question question2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
		questions.save(question1);
		questions.save(question2);

		// when
		List<Question> result = questions.findByDeletedFalse();

		// then
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0)).isEqualTo(question2);
	}

	@Test
	@DisplayName("삭제하지 않은 것 중에 아이디로 조회할 수 있어야 한다")
	public void findByIdAndDeletedFalse(){
		// given
		Question question1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
		Question deletedQuestion = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
		deletedQuestion.setDeleted(true);
		questions.save(question1);
		questions.save(deletedQuestion);

		// when
		Optional<Question> result = questions.findByIdAndDeletedFalse(question1.getId());
		Optional<Question> deletedResult = questions.findByIdAndDeletedFalse(deletedQuestion.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get()).isEqualTo(question1);
		assertThat(deletedResult.isPresent()).isFalse();
	}

}
