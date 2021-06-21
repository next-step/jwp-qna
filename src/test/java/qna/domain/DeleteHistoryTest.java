package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
	@Autowired
	private AnswerRepository answers;

	@Autowired
	private DeleteHistoryRepository deleteHistories;

	@Autowired
	QuestionRepository questions;

	@Autowired
	UserRepository users;

	@PersistenceContext
	private EntityManager entityManager;

	@BeforeEach
	void setUp() {
		List<String> tableNames = Arrays.asList("answer", "delete_history", "question", "user");

		entityManager.flush();
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

		for (String tableName : tableNames) {
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
			entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1")
				.executeUpdate();
		}

		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
	}

	@Test
	@DisplayName("질문 삭제 시 답변도 같이 삭제")
	void question_deleted_then_answer_deleted() throws Exception {
		User saveJavajigi = saveJavajigi();
		Question saveQ1 = saveQ1(saveJavajigi);
		Answer saveAnswer1 = saveAnswer1(saveJavajigi, saveQ1);
		saveQ1.addAnswer(saveAnswer1);
		questions.flush();

		assertThat(saveQ1.delete(saveJavajigi).size()).isEqualTo(2);
	}

	@Test
	@DisplayName("삭제를 조회하고 삭제한 사람을 조회한다.")
	void select_not_deleted_question_with_writer() {
		User sanjigi = saveJavajigi();
		DeleteHistory saveDH1 = saveDeleteHistory1(sanjigi);

		assertThat(saveDH1.isDeletedBy(sanjigi)).isTrue();
	}

	@Test
	@DisplayName("jpa between 조회")
	void select_between() {
		DeleteHistory saveDH1 = saveDeleteHistory1(saveJavajigi());
		DeleteHistory saveDH2 = saveDeleteHistory2(saveSanjigi());

		assertThat(
			deleteHistories.findByCreateDateBetween(LocalDateTime.of(2021, 6, 2, 22, 10),
				LocalDateTime.of(2021, 6, 2, 22, 40)).size()).isEqualTo(
			1);
		assertThat(
			deleteHistories.findByCreateDateBetween(LocalDateTime.of(2021, 6, 2, 22, 10),
				LocalDateTime.of(2021, 6, 2, 23, 40)).size()).isEqualTo(
			2);
	}

	@Test
	@DisplayName("jpa less than 조회")
	void select_less_than() {
		DeleteHistory saveDH1 = saveDeleteHistory1(saveJavajigi());
		DeleteHistory saveDH2 = saveDeleteHistory2(saveSanjigi());

		assertThat(
			deleteHistories.findByIdLessThan(saveDH2.getId()).get(0)).isEqualTo(saveDH1
		);
	}

	private User saveJavajigi() {
		User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		return users.save(JAVAJIGI);
	}

	private User saveSanjigi() {
		User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		return users.save(SANJIGI);
	}

	private DeleteHistory saveDeleteHistory1(User user) {
		DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, users.save(user),
			LocalDateTime.of(2021, 6, 2, 22, 30));
		return deleteHistories.save(DH1);
	}

	private DeleteHistory saveDeleteHistory2(User user) {
		DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, users.save(user),
			LocalDateTime.of(2021, 6, 2, 23, 10));
		return deleteHistories.save(DH1);
	}

	private Question saveQ1(User user) {
		Question Q1 = new Question("title1", "contents1").writtenBy(user);
		return questions.save(Q1);
	}

	private Question saveQ2(User user) {
		Question Q2 = new Question("title2", "contents2").writtenBy(user);
		return questions.save(Q2);
	}

	private Answer saveAnswer1(User user, Question question) {
		return answers.save(new Answer(user, question, "Answers Contents1"));
	}
}
