package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {

	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1,
		"Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q2,
		"Answers Contents2");

	@Autowired
	private UserRepository users;
	@Autowired
	private AnswerRepository answers;
	@Autowired
	private QuestionRepository questions;
	private User savedJavajigi;
	private User savedSangiji;
	private Question savedQuestion1;
	private Question savedQuestion2;

	@DisplayName("테스트 초기화")
	@BeforeEach
	void setup() {
		모든_레포지토리_데이터_삭제();
		초기_정보_저장();
	}

	private void 초기_정보_저장() {
		각_답변별_작성자정보_저장();
		각_답변별_질문정보_저장();
	}

	private void 모든_레포지토리_데이터_삭제() {
		users.deleteAll();
		questions.deleteAll();
		answers.deleteAll();
	}

	private void 각_답변별_질문정보_저장() {
		if (Objects.isNull(savedQuestion1)) {
			savedQuestion1 = questions.save(QuestionTest.Q1.writeBy(savedJavajigi));
			A1.toQuestion(savedQuestion1);
		}
		if (Objects.isNull(savedQuestion2)) {
			savedQuestion2 = questions.save(QuestionTest.Q2.writeBy(savedSangiji));
			A2.toQuestion(savedQuestion2);
		}
	}

	private void 각_답변별_작성자정보_저장() {
		if (Objects.isNull(savedJavajigi)) {
			savedJavajigi = users.save(UserTest.JAVAJIGI);
			A1.writtenBy(savedJavajigi);
		}
		if (Objects.isNull(savedSangiji)) {
			savedSangiji = users.save(UserTest.SANJIGI);
			A2.writtenBy(savedSangiji);
		}
	}

	@DisplayName("Answer 저장 : save()")
	@Test
	void save() {
		//given

		//when
		Answer actual = answers.save(A1);
		Answer actual2 = answers.save(A2);

		//then
		assertAll(
			() -> assertThat(actual.id()).isNotNull(),
			() -> assertThat(actual.isOwner(A1.writer())).isTrue(),
			() -> assertThat(actual.question()).isEqualTo(A1.question()),
			() -> assertThat(actual.writer()).isEqualTo(A1.writer()),
			() -> assertThat(actual.contents()).isEqualTo(A1.contents()),
			() -> assertThat(actual2.id()).isNotNull(),
			() -> assertThat(actual2.isOwner(A2.writer())).isTrue(),
			() -> assertThat(actual2.question()).isEqualTo(A2.question()),
			() -> assertThat(actual2.writer()).isEqualTo(A2.writer()),
			() -> assertThat(actual2.contents()).isEqualTo(A2.contents())
		);
	}

	@DisplayName("Answer 조회 : findById()")
	@Test
	void findById() {
		//given
		Answer expected = answers.save(A1);
		Answer expected2 = answers.save(A2);

		//when
		Answer actual = answers.findById(expected.id()).get();
		Answer actual2 = answers.findById(expected2.id()).get();

		//then
		assertAll(
			() -> assertThat(actual.id()).isNotNull(),
			() -> assertThat(actual.isOwner(A1.writer())).isTrue(),
			() -> assertThat(actual.question()).isEqualTo(A1.question()),
			() -> assertThat(actual.writer()).isEqualTo(A1.writer()),
			() -> assertThat(actual.contents()).isEqualTo(A1.contents()),
			() -> assertThat(actual2.id()).isNotNull(),
			() -> assertThat(actual2.isOwner(A2.writer())).isTrue(),
			() -> assertThat(actual2.question()).isEqualTo(A2.question()),
			() -> assertThat(actual2.writer()).isEqualTo(A2.writer()),
			() -> assertThat(actual2.contents()).isEqualTo(A2.contents())
		);
	}

	@DisplayName("Answer Soft delete - 조회 : findByIdAndDeletedFalse(), 수정 : setDeleted()")
	@Test
	void setAnswerId() {
		//given
		Answer expected = answers.save(A1);

		//when
		Optional<Answer> beforeSoftDelete = answers.findByIdAndDeletedFalse(expected.id());
		expected.delete();
		Optional<Answer> afterSoftDelete = answers.findByIdAndDeletedFalse(expected.id());

		//then
		assertAll(
			() -> assertThat(beforeSoftDelete.isPresent()).isTrue(),
			() -> assertThat(afterSoftDelete.isPresent()).isFalse()
		);
	}

	@DisplayName("Answer 작성자 변경 - 조회 : findById(), 수정 : setWriter()")
	@Test
	void setWriter() {
		//given
		Answer expected = answers.save(A1);

		//when
		Optional<Answer> beforeChangeWriter = answers.findById(expected.id());
		expected.writtenBy(savedSangiji);
		Optional<Answer> afterChangeWriter = answers.findById(expected.id());

		//then
		assertAll(
			() -> assertThat(beforeChangeWriter.get().writer().equals(savedSangiji)).isTrue(),
			() -> assertThat(beforeChangeWriter.get().writer().equals(savedJavajigi)).isFalse(),
			() -> assertThat(afterChangeWriter.get().writer().equals(savedSangiji)).isTrue(),
			() -> assertThat(afterChangeWriter.get().writer().equals(savedJavajigi)).isFalse()
		);
	}

	@DisplayName("Answer 질문 변경 - 조회 : findById(), 수정 : setQuestion()")
	@Test
	void setQuestion() {
		//given
		Answer expected = answers.save(A1);

		//when
		Optional<Answer> beforeChangeQuestion = answers.findById(expected.id());
		expected.toQuestion(savedQuestion2);
		Optional<Answer> afterChangeQuestion = answers.findById(expected.id());

		//then
		assertAll(
			() -> assertThat(beforeChangeQuestion.get().question().equals(savedQuestion2)).isTrue(),
			() -> assertThat(beforeChangeQuestion.get().question().equals(savedQuestion1))
				.isFalse(),
			() -> assertThat(afterChangeQuestion.get().question().equals(savedQuestion2)).isTrue(),
			() -> assertThat(afterChangeQuestion.get().question().equals(savedQuestion1)).isFalse()
		);
	}

	@DisplayName("Answer 삭제 : delete()")
	@Test
	void delete() {
		//given
		Answer expected = answers.save(A1);
		Answer beforeDeleteAnswer = answers.findById(expected.id()).get();

		//when
		answers.delete(expected);
		answers.flush();
		Optional<Answer> afterDeleteAnswerOptional = answers.findById(expected.id());

		//then
		assertAll(
			() -> assertThat(beforeDeleteAnswer).isNotNull(),
			() -> assertThat(afterDeleteAnswerOptional.isPresent()).isFalse()
		);
	}
}
