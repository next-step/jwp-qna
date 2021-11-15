package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.fixture.AnswerFixture;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	@DisplayName("저장하기 전후의 객체가 서로 동일한 객체인가")
	void save() {
		final Answer expected = AnswerFixture.질문자와_답변자가_다른_답변("writer.id");
		final Answer actual = answerRepository.save(expected);
		assertAll(
			() -> assertThat(actual).isSameAs(expected),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getWriter()).isSameAs(expected.getWriter()),
			() -> assertThat(actual.getQuestion()).isSameAs(expected.getQuestion()),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
			() -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
		);
	}

	@Test
	@DisplayName("저장된 객체가 질문의 답변 중 삭제 되지 않은 리스트에 포함된 객체인가")
	void findByQuestionIdAndDeletedFalse() {
		final Answer expected = saved(AnswerFixture.질문자와_답변자가_다른_답변("writer.id"));
		assertThat(expected.isDeleted()).isFalse();

		final List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(expected.getQuestion());
		assertThat(answers).containsExactly(expected);
		answers.forEach(actual -> assertAll(
			() -> assertThat(actual.getQuestion()).isSameAs(expected.getQuestion()),
			() -> assertThat(actual.isDeleted()).isFalse()
		));
	}

	@Test
	@DisplayName("저장된 객체가 삭제되지 않고 id로 검색한 객체와 동일한가")
	void findByIdAndDeletedFalse() {
		final Answer expected = saved(AnswerFixture.질문자와_답변자가_다른_답변("writer.id"));
		assertThat(expected.isDeleted()).isFalse();

		final Optional<Answer> maybeActual = answerRepository.findByIdAndDeletedFalse(expected.getId());
		assertThat(maybeActual.isPresent()).isTrue();
		final Answer actual = maybeActual.get();
		assertAll(
			() -> assertThat(actual).isSameAs(expected),
			() -> assertThat(actual.isDeleted()).isFalse()
		);
	}

	private Answer saved(Answer answer) {
		userRepository.saveAll(Arrays.asList(
			answer.getQuestion().getWriter(),
			answer.getWriter()
		));
		questionRepository.save(answer.getQuestion());
		return answerRepository.save(answer);
	}
}
