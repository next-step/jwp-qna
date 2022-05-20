package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {
	private final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	private final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

	@Autowired
	QuestionRepository questionRepository;

	@Test
	@DisplayName("Question 생성")
	void save() {
		Question expected = new Question("title1", "contents1").writeBy(JAVAJIGI);
		Question actual = questionRepository.save(expected);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
		);
	}

	@Test
	@DisplayName("Question 삭제")
	void delete() {
		Question expected = questionRepository.save(new Question("title1", "contents1").writeBy(JAVAJIGI));
		questionRepository.delete(expected);

		Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(expected.getId());

		assertThat(actual.isPresent()).isFalse();
	}

	@Test
	@DisplayName("삭제되지 않은 Question 조회")
	void findByDeletedFalse() {
		Question expected1 = questionRepository.save(new Question("title1", "contents1").writeBy(JAVAJIGI));
		Question expected2 = questionRepository.save(new Question("title2", "contents2").writeBy(JAVAJIGI));
		Question expected3 = questionRepository.save(new Question("title3", "contents3").writeBy(JAVAJIGI));

		questionRepository.delete(expected3);

		List<Question> actual = questionRepository.findByDeletedFalse();

		assertThat(actual)
			.contains(expected1, expected2)
			.doesNotContain();
	}
}
