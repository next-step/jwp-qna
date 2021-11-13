package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

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

	@Test
	@DisplayName("질문을 저장하면 ID가 존재해야 한다")
	void saveTest() {
		// given
		User writer1 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		writer1 = users.save(writer1);
		Question question1 = new Question("title1", "contents1").writeBy(writer1);

		// when
		questions.save(question1);

		// then
		assertThat(question1.getId()).isNotNull();
	}

	@Test
	@DisplayName("삭제되지 않은 질문들을 조회할 수 있어야 한다")
	void findByDeletedFalseTest() {

		// given
		User writer1 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User writer2 = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		writer1 = users.save(writer1);
		writer2 = users.save(writer2);
		Question question1 = new Question("title1", "contents1").writeBy(writer1);
		question1.setDeleted(true);
		Question question2 = new Question("title2", "contents2").writeBy(writer2);
		questions.save(question1);
		questions.save(question2);

		// when
		List<Question> result = questions.findByDeletedFalse();

		// then
		assertAll(
			() -> assertThat(result.size()).isEqualTo(1),
			() -> assertThat(result.get(0)).isEqualTo(question2)
		);

	}

	@Test
	@DisplayName("삭제하지 않은 것 중에 아이디로 조회할 수 있어야 한다")
	void findByIdAndDeletedFalse() {

		// given
		User writer1 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User writer2 = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		writer1 = users.save(writer1);
		writer2 = users.save(writer2);
		Question question1 = new Question("title1", "contents1").writeBy(writer1);
		Question deletedQuestion = new Question("title2", "contents2").writeBy(writer2);
		deletedQuestion.setDeleted(true);
		questions.save(question1);
		questions.save(deletedQuestion);

		// when
		Optional<Question> result = questions.findByIdAndDeletedFalse(question1.getId());
		Optional<Question> deletedResult = questions.findByIdAndDeletedFalse(deletedQuestion.getId());

		// then
		assertAll(
			() -> assertThat(result).isPresent(),
			() -> assertThat(result).containsSame(question1),
			() -> assertThat(deletedResult).isEmpty()
		);
	}

}
