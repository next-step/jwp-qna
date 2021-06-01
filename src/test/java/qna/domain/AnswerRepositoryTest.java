package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private AnswerRepository answers;

	@Autowired
	private UserRepository users;

	@Autowired
	private QuestionRepository questions;

	private static final String expectedContents = "Answers Contents1";
	private User writer;
	private Question question;
	private Answer expected;

	@BeforeEach
	void setUp() {
		writer = users.save(JAVAJIGI);
		question = questions.save(new Question("title1", "contents1").writeBy(writer));
		expected = answers.save(new Answer(writer, question, expectedContents));

		assertAll(() -> {
			assertThat(writer).isNotNull();
			assertThat(question).isNotNull();
			assertThat(expected).isNotNull();
		});
	}

	@Test
	@DisplayName("save 테스트")
	void saveTest() {
		// then
		assertAll(() -> {
			assertThat(expected.getId()).isNotNull();
			assertThat(expected.isSameQuestion(question)).isTrue();
			assertThat(expected.isOwner(writer)).isTrue();
			assertThat(expected.isSameContents(expectedContents)).isTrue();
		});
	}

	@Test
	@DisplayName("findById test")
	void findByIdTest() {
		assertThat(answers.findById(expected.getId()))
			.isPresent()
			.get()
			.isSameAs(expected);
	}

	@Test
	@DisplayName("연관관계 매핑이 된 User를 지연로딩을 통해 불러오는지 테스트")
	void fetchLazyTest() {
		testEntityManager.clear(); // cache clear

		assertThat(answers.findById(expected.getId())) // select Answer
			.isPresent()
			.get()
			.extracting(answer -> answer.getWriter()) // select User
			.isEqualTo(writer);
	}

	@Test
	@DisplayName("contents update 테스트")
	void updateTest() {
		// given
		String expectContents = "update";

		// when
		expected.updateContents(expectContents);

		// then
		assertThat(answers.findByIdAndDeletedFalse(expected.getId()))
			.isPresent()
			.get()
			.extracting(answer -> answer.isSameContents(expectContents))
			.isEqualTo(true);
	}

	@Test
	@DisplayName("연관 관계인 Writer update 테스트")
	void updateWriterTest() {
		// given
		User updateWriter = users.save(SANJIGI);

		// when
		expected.toWriter(updateWriter);

		// then
		assertThat(answers.findByIdAndDeletedFalse(expected.getId()))
			.isPresent()
			.get()
			.extracting(answer -> answer.isOwner(updateWriter))
			.isEqualTo(true);
	}

	@Test
	@DisplayName("연관 관계인 Question update 테스트")
	void updateQuestionTest() {
		// given
		User writer = users.save(SANJIGI);
		Question updateQuestion = questions.save(new Question("title1", "contents1").writeBy(writer));

		// when
		expected.toQuestion(updateQuestion);

		// then
		assertThat(answers.findByIdAndDeletedFalse(expected.getId()))
			.isPresent()
			.get()
			.extracting(answer -> answer.isSameQuestion(updateQuestion))
			.isEqualTo(true);
	}

	@Test
	@DisplayName("존재하지 않는 PK로 findById test")
	void findByIdTestWithNull() {
		assertThat(answers.findById(0L)).isNotPresent();
	}

	@Test
	@DisplayName("PK로 deleted 컬럼이 false인 Answer 확인")
	void findByIdAndDeletedFalseTest() {
		// when
		assertThat(answers.findByIdAndDeletedFalse(expected.getId()))
			.isPresent()
			.get()
			.isSameAs(expected);
	}

	@Test
	@DisplayName("PK로 deleted 컬럼이 true Answer 확인")
	void findByIdAndDeletedFalseTestWithDeletedTrueEntity() {
		// given
		User writer = users.save(SANJIGI);
		Answer answer = new Answer(writer, question, "Answers Contents2");
		answer.delete();
		Answer notHaveAnswer = answers.save(answer);

		// when
		assertThat(answers.findByIdAndDeletedFalse(notHaveAnswer.getId()))
			.isNotPresent();
	}

	@Test
	@DisplayName("DELETE 테스트")
	void deleteTest() {
		// when
		answers.delete(expected);

		// then
		assertThat(answers.findById(expected.getId()))
			.isNotPresent();
	}


}
