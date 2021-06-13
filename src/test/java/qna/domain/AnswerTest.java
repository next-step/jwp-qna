package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Test
	@DisplayName(value = "answer entity 가 DB에 저장되면 id 값, 저장 날짜가 할당된다")
	void save() {
		Answer persist = insertAnswer();
		assertAll(
			() -> assertThat(persist.getId()).isNotNull(),
			() -> assertThat(persist.getCreatedAt()).isNotNull(),
			() -> assertThat(persist.getUpdatedAt()).isNotNull()
		);
	}

	@Test
	@DisplayName(value = "저장된 entity 를 select 하여 반환한다")
	void select() {
	    Answer insert = insertAnswer();
		Answer selectById = answerRepository.findByIdAndDeletedFalse(insert.getId()).get();
		assertThat(selectById).isNotNull();
	}

	@Test
	@DisplayName(value = "update를 수행하면 update_at 이 컬럼이 갱신된다")
	void updateAtHasChanged() {
		Answer origin = insertAnswer();
		LocalDateTime originUpdatedAt = origin.getUpdatedAt();
	    origin.writeContents("답변 추가 입니다");
	    Answer updated = answerRepository.saveAndFlush(origin);
	    assertThat(updated.getUpdatedAt()).isNotEqualTo(originUpdatedAt);
	}

	private Answer insertAnswer() {
		User user = userRepository.saveAndFlush(UserTest.JAVAJIGI);
		Question question = questionRepository.saveAndFlush(QuestionTest.Q1.writeBy(user));

		Answer answer = new Answer(user, question, "답변입니다!!");
		return answerRepository.saveAndFlush(answer);
	}

	@Test
	@DisplayName(value = "answer 의 작성자와 삭제를 요청한 로그인 유저가 다를 경우 CannotDeleteException을 일으킨다")
	void cannotDeleteException() {
		User answerWriter = new User(100L, "question writer", "password", "dodo", "dodo@mail.com");
		User loginUser = new User(101L, "login user", "password", "navi", "navi@mail.com");
		Question question = new Question("title", "questions contents");

		Answer answer = new Answer(answerWriter, question, "answer contents");
		assertThrows(CannotDeleteException.class, ()-> answer.isOwnerOrThrows(loginUser));
	}

}
