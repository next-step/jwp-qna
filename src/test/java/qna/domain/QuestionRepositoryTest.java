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
public class QuestionRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Test
	@DisplayName("저장하기 전후의 객체가 서로 동일한 객체인가")
	void save() {
		final Question expected = Fixture.question("writer.id");
		final Question actual = questionRepository.save(expected);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
			() -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
			() -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
		);
		assertThat(actual).isSameAs(expected);
	}

	@Test
	@DisplayName("저장된 객체가 삭제되지 않은 질문 리스트에 포함된 객체인가")
	void findByDeletedFalse() {
		final Question question = saved(Fixture.question("writer.id"));
		assertThat(question.isDeleted()).isFalse();
		final List<Question> questions = questionRepository.findByDeletedFalse();
		assertThat(questions).containsExactly(question);
		questions.forEach(q -> assertThat(q.isDeleted()).isFalse());
	}

	@Test
	@DisplayName("저장된 객체가 삭제되지 않고 id로 검색한 객체와 동일한가")
	void findByIdAndDeletedFalse() {
		final Question expected = saved(Fixture.question("writer.id"));
		assertThat(expected.isDeleted()).isFalse();
		final Optional<Question> maybeActual = questionRepository.findByIdAndDeletedFalse(expected.getId());
		assertThat(maybeActual.isPresent()).isTrue();
		final Question actual = maybeActual.get();
		assertThat(actual).isSameAs(expected);
		assertThat(actual.isDeleted()).isFalse();
	}

	private Question saved(Question question) {
		userRepository.save(question.getWriter());
		return questionRepository.save(question);
	}
}
