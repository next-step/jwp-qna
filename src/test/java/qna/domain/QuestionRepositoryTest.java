package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuestionRepositoryTest {

	public static final Question Q1 = new Question("title1", "contents1")
		.writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	@Autowired
	private UserRepository users;
	@Autowired
	private QuestionRepository questions;
	private User savedJavajigi;
	private User savedSangiji;

	@DisplayName("테스트 초기화")
	@BeforeEach
	void setup() {
		모든_레포지토리_데이터_삭제();
		초기_정보_저장();
	}

	private void 초기_정보_저장() {
		각_답변별_작성자정보_저장();
	}

	private void 모든_레포지토리_데이터_삭제() {
		users.deleteAll();
		questions.deleteAll();
	}

	private void 각_답변별_작성자정보_저장() {
		savedJavajigi = users.save(Q1.writer());
		Q1.writeBy(savedJavajigi);
		savedSangiji = users.save(Q2.writer());
		Q2.writeBy(savedSangiji);
	}

	@DisplayName("Question 저장 : save()")
	@Test
	@Order(1)
	void save() {
		//given

		//when
		Question actual = questions.save(Q1);
		Question actual2 = questions.save(Q2);

		//then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual2).isNotNull()
		);
	}

	@DisplayName("Question 조회 : findById()")
	@Test
	@Order(2)
	void findById() {
		//given
		Question expected = questions.save(Q1);
		Question expected2 = questions.save(Q2);

		//when
		Question actual = questions.findById(expected.id()).get();
		Question actual2 = questions.findById(expected2.id()).get();

		//then
		assertAll(
			() -> assertThat(actual.equals(expected)).isTrue(),
			() -> assertThat(actual2.equals(expected2)).isTrue()
		);
	}

	@DisplayName("Question Soft delete - 조회 : findByIdAndDeletedFalse(), 수정 : setDeleted()")
	@Test
	@Order(3)
	void setQuestionId() {
		//given
		Question expected = questions.save(Q1);

		//when
		Optional<Question> beforeSoftDelete = questions.findByIdAndDeletedFalse(expected.id());
		expected.delete();
		Optional<Question> afterSoftDelete = questions.findByIdAndDeletedFalse(expected.id());

		//then
		assertAll(
			() -> assertThat(beforeSoftDelete.isPresent()).isTrue(),
			() -> assertThat(afterSoftDelete.isPresent()).isFalse()
		);
	}

	@DisplayName("Question 삭제 : delete()")
	@Test
	@Order(4)
	void delete() {
		//given
		Question expected = questions.save(Q1);
		Question beforeDeleteQuestion = questions.findById(expected.id()).get();

		//when
		questions.delete(expected);
		Optional<Question> afterDeleteQuestionOptional = questions.findById(expected.id());

		//then
		assertAll(
			() -> assertThat(beforeDeleteQuestion).isNotNull(),
			() -> assertThat(afterDeleteQuestionOptional.isPresent()).isFalse()
		);
	}
}
