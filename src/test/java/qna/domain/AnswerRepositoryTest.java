package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	private AnswerRepository answers;

	@Autowired
	private UserRepository users;

	@Autowired
	private QuestionRepository questions;

	private User writer;
	private Question question;

	@BeforeEach
	void setUp() {
		writer = new User("javajigi", "password", "name", "javajigi@slipp.net");
		question = new Question("title1", "contents1").writeBy(writer);
		users.save(writer);
		questions.save(question);
	}

	@Test
	@DisplayName("Answer 저장 테스트")
	void save() {
		Answer answer = new Answer(writer, question, "내용");
		Answer actual = answers.save(answer);
		assertThat(answers.findByIdAndDeletedFalse(actual.getId()).isPresent()).isTrue();
	}

	@Test
	@DisplayName("Answer id로 Answer 조회 테스트")
	void findById() {
		Answer answer = new Answer(writer, question, "내용");
		Answer saved = answers.save(answer);
		Answer actual = answers.findByIdAndDeletedFalse(saved.getId()).get();
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
			() -> assertThat(actual.isDeleted()).isEqualTo(false),
			() -> assertThat(actual.getCreateAt()).isNotNull(),
			() -> assertThat(actual.getWriter()).isEqualTo(answer.getWriter()),
			() -> assertThat(actual.getWriter().getName()).isEqualTo("name"),
			() -> assertThat(actual.getWriter().getPassword()).isEqualTo("password"),
			() -> assertThat(actual.getWriter().getUserId()).isEqualTo("javajigi"),
			() -> assertThat(actual.getWriter().getEmail()).isEqualTo("javajigi@slipp.net"),
			() -> assertThat(actual.getQuestion().getContents()).isEqualTo("contents1"),
			() -> assertThat(actual.getQuestion().getTitle()).isEqualTo("title1")
		);
	}

	@Test
	@DisplayName("Answer 수정 테스트")
	void update() {
		Answer answer = new Answer(writer, question, "내용");
		Answer saved = answers.save(answer);
		Answer actual = answers.findByIdAndDeletedFalse(saved.getId()).get();
		Date updateAt = new Date();

		actual.setContents("수정했어요");
		actual.setUpdateAt(updateAt);
		answers.flush();
		assertThat(saved.getContents()).isEqualTo("수정했어요");
		assertThat(saved.getUpdateAt()).isEqualTo(updateAt);
	}

	@Test
	@DisplayName("Answer 삭제 테스트")
	void delete() {
		Answer answer = new Answer(writer, question, "내용");
		Answer saved = answers.save(answer);
		long savedId = saved.getId();
		answers.delete(saved);
		answers.flush();
		assertThat(answers.findByIdAndDeletedFalse(savedId).isPresent()).isFalse();
	}


	@Test
	@DisplayName("Answer Sort 삭제 테스트")
	void deleteSort() throws CannotDeleteException {
		Answer answer = new Answer(writer, question, "내용");
		Answer saved = answers.save(answer);
		answer.delete(writer);
		answers.flush();
		assertThat(answer.isDeleted()).isTrue();
	}

}
