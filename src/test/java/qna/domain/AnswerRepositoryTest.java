package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	private UserRepository users;
	@Autowired
	private AnswerRepository answers;
	@Autowired
	private QuestionRepository questions;
	private Answer answer1;
	private Answer answer2;
	private User savedJavajigi;
	private User savedSangiji;
	private Question savedQuestion1;
	private Question savedQuestion2;

	@DisplayName("테스트 초기화")
	@BeforeEach
	void setup() {
		초기_정보_저장();
	}

	private void 초기_정보_저장() {
		각_답변별_작성자정보_저장();
		각_답변별_질문정보_저장();
		답변_인스턴스_생성();
		연관_관계_매핑();
	}

	private void 연관_관계_매핑() {
		answer1.addQuestion(savedQuestion1);
		answer2.addQuestion(savedQuestion2);
		answer1.writtenBy(savedJavajigi);
		answer2.writtenBy(savedSangiji);
	}

	private void 답변_인스턴스_생성() {
		answer1 = new Answer(UserTest.JAVAJIGI, savedQuestion1, "Answers Contents1");
		answer2 = new Answer(UserTest.SANJIGI, savedQuestion2, "Answers Contents2");
	}

	private void 각_답변별_질문정보_저장() {
		Question tempQuestion1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
		tempQuestion1.writeBy(savedJavajigi);
		savedQuestion1 = questions.save(tempQuestion1);

		Question tempQuestion2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);
		tempQuestion2.writeBy(savedSangiji);
		savedQuestion2 = questions.save(tempQuestion2);
	}

	private void 각_답변별_작성자정보_저장() {
		savedJavajigi = users.save(UserTest.JAVAJIGI);
		savedSangiji = users.save(UserTest.SANJIGI);
	}

	@DisplayName("Answer 저장 : save()")
	@Test
	void save() {
		//given

		//when
		Answer actual = answers.save(answer1);
		Answer actual2 = answers.save(answer2);

		//then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual2).isNotNull()
		);
	}

	@DisplayName("Answer 조회 : findById()")
	@Test
	void findById() {
		//given
		Answer expected = answers.save(answer1);
		Answer expected2 = answers.save(answer2);

		//when
		Answer actual = answers.findById(expected.id()).get();
		Answer actual2 = answers.findById(expected2.id()).get();

		//then
		assertAll(
			() -> assertThat(actual.equals(expected)).isTrue(),
			() -> assertThat(actual2.equals(expected2)).isTrue()
		);
	}

	@DisplayName("Answer Soft delete - 조회 : findByIdAndDeletedFalse(), 수정 : setDeleted()")
	@Test
	void setAnswerId() {
		//given
		Answer expected = answers.save(answer1);

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
		Answer expected = answers.save(answer1);

		//when
		expected.writtenBy(savedSangiji);
		Optional<Answer> afterChangeWriter = answers.findById(expected.id());

		//then
		assertAll(
			() -> assertThat(afterChangeWriter.get().writer().equals(savedSangiji)).isTrue(),
			() -> assertThat(afterChangeWriter.get().writer().equals(savedJavajigi)).isFalse()
		);
	}

	@DisplayName("Answer 질문 변경 - 조회 : findById(), 수정 : setQuestion()")
	@Test
	void setQuestion() {
		//given
		Answer expected = answers.save(answer1);

		//when
		expected.addQuestion(savedQuestion2);
		Optional<Answer> afterChangeQuestion = answers.findById(expected.id());

		//then
		assertAll(
			() -> assertThat(afterChangeQuestion.get().question().equals(savedQuestion2)).isTrue(),
			() -> assertThat(afterChangeQuestion.get().question().equals(savedQuestion1)).isFalse()
		);
	}

	@DisplayName("Answer 삭제 : delete()")
	@Test
	void delete() {
		//given
		Answer expected = answers.save(answer1);
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
