package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private QuestionRepository questions;

	@Autowired
	private UserRepository users;

	private User writer;
	private Question expected;

	@BeforeEach
	void setUp() {
		writer = users.save(UserTest.JAVAJIGI);
		expected = questions.save(new Question("title1", "contents1").writeBy(writer));

		assertAll(() -> {
			assertThat(expected).isNotNull();
			assertThat(writer).isNotNull();
		});
	}


	@Test
	@DisplayName("save 테스트")
	void saveTest() {
		assertAll(() -> {
			assertThat(expected.getId()).isNotNull();
			assertThat(expected.isSameTitle("title1")).isTrue();
			assertThat(expected.isSameContents("contents1")).isTrue();
			assertThat(expected.isOwner(writer)).isTrue();
		});

	}

	@Test
	@DisplayName("update 테스트")
	void updateTest() {
		// given
		String expectedTitle = "update";

		// when
		expected.updateTitle(expectedTitle);

		// then
		assertThat(questions.findByIdAndDeletedFalse(expected.getId()))
			.isPresent()
			.get()
			.extracting(value -> value.isSameTitle(expectedTitle))
			.isEqualTo(true);
	}

	@Test
	@DisplayName("연관관계 writer 업데이트 테스트")
	void updateWriterTest() {
		// given
		User updateWriter = users.save(UserTest.SANJIGI);

		// when
		expected.writeBy(updateWriter);

		// then
		assertThat(questions.findById(expected.getId()))
			.isPresent()
			.get()
			.extracting(question -> question.isOwner(updateWriter))
			.isEqualTo(true);
	}

	@Test
	@DisplayName("findById 테스트")
	void findByIdTest() {
		// when
		assertThat(questions.findById(expected.getId()))
			.isPresent()
			.get()
			.isSameAs(expected); // then
	}

	@Test
	@DisplayName("delete 컬럼이 false인 모든 question 조회 테스트")
	void findByDeletedFalseTest() {
		// given
		Question notHaveQuestion = questions.save(new Question("title1", "contents1").writeBy(writer)); // default false
		notHaveQuestion.delete();

		// when
		assertThat(questions.findByDeletedFalse())
			.isNotEmpty()
			.contains(expected)
			.doesNotContain(notHaveQuestion); // then
	}

	@Test
	@DisplayName("delete 컬럼이 false인 question Id 조회 테스트")
	void findByIdAndDeletedFalseTest() {
		// when
		assertThat(questions.findByIdAndDeletedFalse(expected.getId()))
			.isPresent()
			.get()
			.isSameAs(expected); // then
	}

	@Test
	@DisplayName("delete 컬럼이 true인 question Id 조회 테스트")
	void findByIdAndDeletedFalseTestWithDeleteTure() {
		// given
		Question notHaveQuestion = questions.save(new Question("title1", "contents1").writeBy(writer)); // default false
		notHaveQuestion.delete();

		// when
		assertThat(questions.findByIdAndDeletedFalse(notHaveQuestion.getId()))
			.isNotPresent(); // then
	}

	@Test
	@DisplayName("연관관계 매핑이 된 writer를 지연 로딩을 통해 불러오는지 테스트")
	void fetchLazyTest() {
		testEntityManager.clear(); // cache clear

		assertThat(questions.findById(expected.getId())) // select question
			.isPresent()
			.get()
			.extracting(question -> question.getWriter()) // select user
			.isEqualTo(writer);
	}

	@Test
	@DisplayName("삭제 테스트")
	void deleteTest() {
		// when
		questions.delete(expected);

		// then
		assertThat(questions.findById(expected.getId()))
			.isNotPresent();
	}
}
