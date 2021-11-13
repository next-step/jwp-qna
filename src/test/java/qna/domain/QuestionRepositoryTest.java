package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

	@Autowired
	QuestionRepository questions;

	@Autowired
	UserRepository users;

	User user1;
	User user2;
	Question question1;
	Question question2;
	Question deletedQuestion;

	@BeforeEach
	public void setup() {
		user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
		user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
		user1 = users.save(user1);
		user2 = users.save(user2);

		question1 = new Question("title1", "contents1").writeBy(user1);
		question2 = new Question("title2", "contents2").writeBy(user2);
		deletedQuestion = new Question("title3", "contents3").writeBy(user1);
		deletedQuestion.setDeleted(true);
		question1 = questions.save(question1);
		question2 = questions.save(question2);
		deletedQuestion = questions.save(deletedQuestion);
	}

	@Test
	@DisplayName("질문을 저장하면 ID가 존재해야 한다")
	void saveTest() {
		// given
		Question question1 = new Question("title1", "contents1").writeBy(user1);

		// when
		questions.save(question1);

		// then
		assertThat(question1.getId()).isNotNull();
	}

	@Test
	@DisplayName("삭제되지 않은 질문들을 조회할 수 있어야 한다")
	void findByDeletedFalseTest() {

		// when
		List<Question> result = questions.findByDeletedFalse();

		// then
		assertAll(
			() -> assertThat(result.size()).isEqualTo(2),
			() -> assertThat(result).contains(question1, question2)
		);

	}

	@Test
	@DisplayName("삭제하지 않은 것 중에 아이디로 조회할 수 있어야 한다")
	void findByIdAndDeletedFalse() {

		// when
		Optional<Question> result = questions.findByIdAndDeletedFalse(question1.getId());

		// then
		assertAll(
			() -> assertThat(result).isPresent(),
			() -> assertThat(result).containsSame(question1)
		);
	}

}
