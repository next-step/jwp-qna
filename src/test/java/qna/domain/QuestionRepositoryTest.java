package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
public class QuestionRepositoryTest {

	public static final User JAVAJIGI = User.generate(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = User.generate(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	@Autowired
	private UserRepository users;
	@Autowired
	private QuestionRepository questions;
	private User savedJavajigi;
	private User savedSangiji;
	private Question instanceOfQuestionWrittenByJavajigi;
	private Question instanceOfQuestionWrittenBySanjigi;

	@DisplayName("테스트 초기화")
	@BeforeEach
	void setup() {
		초기_정보_저장();
	}

	private void 초기_정보_저장() {
		사용자정보_저장();
		질문_인스턴스_생성();
	}

	private void 사용자정보_저장() {
		savedJavajigi = users.save(JAVAJIGI);
		savedSangiji = users.save(SANJIGI);
	}

	private void 질문_인스턴스_생성() {
		instanceOfQuestionWrittenByJavajigi = Question.generate("title1", "contents1")
			.writeBy(savedJavajigi);
		instanceOfQuestionWrittenBySanjigi = Question.generate("title2", "contents2")
			.writeBy(savedSangiji);
	}

	@DisplayName("Question 저장 : save()")
	@Test
	void save() {
		//given

		//when
		Question actualQuestionWrittenByJavajigi = questions
			.save(instanceOfQuestionWrittenByJavajigi);
		Question actualQuestionWrittenBySanjigi = questions
			.save(instanceOfQuestionWrittenBySanjigi);

		//then
		assertAll(
			() -> assertThat(actualQuestionWrittenByJavajigi).isNotNull(),
			() -> assertThat(actualQuestionWrittenBySanjigi).isNotNull()
		);
	}

	@DisplayName("Question 조회 : findById()")
	@Test
	void findById() {
		//given
		Question expectedQuestionWrittenByJavajigi = questions
			.save(instanceOfQuestionWrittenByJavajigi);
		Question expectedQuestionWrittenBySanjigi = questions
			.save(instanceOfQuestionWrittenBySanjigi);

		//when
		Question actualQuestionWrittenByJavajigi = questions
			.findById(expectedQuestionWrittenByJavajigi.id()).get();
		Question actualQuestionWrittenBySanjigi = questions
			.findById(expectedQuestionWrittenBySanjigi.id()).get();

		//then
		assertAll(
			() -> assertThat(
				actualQuestionWrittenByJavajigi.equals(expectedQuestionWrittenByJavajigi)).isTrue(),
			() -> assertThat(
				actualQuestionWrittenBySanjigi.equals(expectedQuestionWrittenBySanjigi)).isTrue()
		);
	}

	@DisplayName("Question Soft delete - 조회 : findByIdAndDeletedFalse(), 수정 : delete()")
	@Test
	void softDelete() {
		//given
		Question expectedQuestionWrittenByJavajigi = questions
			.save(instanceOfQuestionWrittenByJavajigi);

		//when
		Optional<Question> questionWrittenByJavajigiBeforeSoftDelete = questions
			.findByIdAndDeletedFalse(expectedQuestionWrittenByJavajigi.id());
		expectedQuestionWrittenByJavajigi.delete(savedJavajigi);
		Optional<Question> questionWrittenByJavajigiAfterSoftDelete = questions
			.findByIdAndDeletedFalse(expectedQuestionWrittenByJavajigi.id());

		//then
		assertAll(
			() -> assertThat(questionWrittenByJavajigiBeforeSoftDelete.isPresent()).isTrue(),
			() -> assertThat(questionWrittenByJavajigiAfterSoftDelete.isPresent()).isFalse()
		);
	}

	@DisplayName("Question 삭제 : delete()")
	@Test
	void delete() {
		//given
		Question expectedQuestionWrittenByJavajigi = questions
			.save(instanceOfQuestionWrittenByJavajigi);

		//when
		Question questionWrittenByJavajigiBeforeDelete = questions
			.findById(expectedQuestionWrittenByJavajigi.id()).get();
		questions.delete(expectedQuestionWrittenByJavajigi);
		Optional<Question> questionWrittenByJavajigiAfterDelete = questions
			.findById(expectedQuestionWrittenByJavajigi.id());

		//then
		assertAll(
			() -> assertThat(questionWrittenByJavajigiBeforeDelete).isNotNull(),
			() -> assertThat(questionWrittenByJavajigiAfterDelete.isPresent()).isFalse()
		);
	}
}
