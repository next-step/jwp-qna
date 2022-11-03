package qna.domain.question;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestConstructor;

import qna.domain.generator.QuestionGenerator;
import qna.domain.generator.UserGenerator;
import qna.domain.user.User;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import({QuestionGenerator.class, UserGenerator.class})
@DisplayName("Question Repository 테스트")
class QuestionRepositoryTest {

	private final QuestionRepository questionRepository;
	private final QuestionGenerator questionGenerator;
	private final UserGenerator userGenerator;
	private final EntityManager entityManager;

	public QuestionRepositoryTest(
		QuestionRepository questionRepository,
		QuestionGenerator questionGenerator,
		UserGenerator userGenerator,
		EntityManager entityManager
	) {
		this.questionRepository = questionRepository;
		this.questionGenerator = questionGenerator;
		this.userGenerator = userGenerator;
		this.entityManager = entityManager;
	}

	@BeforeEach
	void setUp() {
		questionRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("질문 저장 테스트")
	void saveTest() {
		User writer = userGenerator.savedUser();
		Question question = questionRepository.save(questionGenerator.savedQuestion(writer));

		assertAll(
			() -> assertThat(question).isNotNull(),
			() -> assertThat(question.getId()).isNotNull(),
			() -> assertThat(question.isOwner(writer)).isTrue(),
			() -> assertThat(question.isDeleted()).isFalse()
		);
	}

	@Test
	@DisplayName("질문 저장 후 조회 테스트 - id")
	void findByIdTest() {
		// given
		User writer = userGenerator.savedUser();
		Question question = questionRepository.save(questionGenerator.savedQuestion(writer));
		entityManager.clear();

		// when
		Question actual = questionRepository.findById(question.getId())
			.orElseThrow(() -> new IllegalArgumentException("entity is not found"));

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
			() -> assertThat(actual.isDeleted()).isEqualTo(question.isDeleted()),
			() -> assertThat(actual.getWriter()).isEqualTo(question.getWriter()),
			() -> assertThat(actual.getWriter())
				.as("엔티티 매니저 clear() 했기 때문에 LazyLoading 에 의해 User 는 proxy 객체")
				.isInstanceOf(HibernateProxy.class)
		);
	}

	@Test
	@DisplayName("질문 리스트 조회 테스트")
	void findAllTest() {
		// given
		User writer = userGenerator.savedUser();
		questionGenerator.savedQuestion(writer);
		questionGenerator.savedQuestion(writer);
		entityManager.clear();

		// when
		List<Question> questions = questionRepository.findByDeletedFalse();

		// then
		assertThat(questions)
			.hasSize(2)
			.allSatisfy(question ->
				assertAll(
					() -> assertThat(question).isNotNull(),
					() -> assertThat(question.getId()).isNotNull(),
					() -> assertThat(question.isOwner(writer)).isTrue(),
					() -> assertThat(question.isDeleted()).isFalse(),
					() -> assertThat(question.getWriter())
						.as("엔티티 매니저 clear() 했기 때문에 LazyLoading 에 의해 User 는 proxy 객체")
						.isInstanceOf(HibernateProxy.class)
				));
	}

	@Test
	@DisplayName("100 자가 넘는 title 저장 시 예외 발생 테스트")
	void invalidTitleMaxLengthTest() {
		// given
		StringBuilder title = new StringBuilder("a");
		for (int i = 0; i < 100; i++) {
			title.append("a");
		}
		Question questions = new Question(title.toString(), "contents1").writeBy(userGenerator.savedUser());

		// when, then
		assertThatThrownBy(() -> questionRepository.save(questions))
			.isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	@DisplayName("삭제 여부 true 로 변경 후 조회 테스트")
	void deleteTest() {
		// given
		User writer = userGenerator.savedUser();
		Question question = questionGenerator.savedQuestion(writer);

		// when
		question.setDeleted(true);
		entityManager.flush();

		// then
		assertThat(question.isDeleted()).isTrue();
	}
}
