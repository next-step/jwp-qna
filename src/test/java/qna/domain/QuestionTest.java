package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class QuestionTest {

	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	@Autowired
	QuestionRepository questionRepository;

	@BeforeAll
	static void setUp(@Autowired UserRepository userRepository) {
		userRepository.save(UserTest.JAVAJIGI);
		userRepository.save(UserTest.SANJIGI);
	}

	@Test
	void 질문_저장_테스트() {
		// given // when
		Question question = questionRepository.save(Q1);

		// then
		assertAll(
			() -> assertThat(question.getId()).isEqualTo(Q1.getId()),
			() -> assertThat(question.getTitle()).isEqualTo(Q1.getTitle())
		);
	}

	@Test
	void 질문_조회_테스트() {
		// given
		Question expectQuestion = questionRepository.save(Q2);

		// when
		Optional<Question> question = questionRepository.findById(expectQuestion.getId());

		// then
		assertAll(
			() -> assertThat(question.isPresent()).isTrue(),
			() -> assertThat(expectQuestion).isEqualTo(question.get())
		);
	}

	@Test
	void 질문_수정_테스트() {
		// given
		Question question = questionRepository.save(new Question("t1", "con1").writeBy(UserTest.SANJIGI));

		// when
		question.setWriter(UserTest.JAVAJIGI);
		Question expectQuestion = questionRepository.save(question);

		// then
		assertThat(expectQuestion.getWriter()).isEqualTo(UserTest.JAVAJIGI);
	}

	@Test
	void 질문_삭제_테스트() {
		// given
		Question q1 = new Question("t1", "con1").writeBy(UserTest.JAVAJIGI);
		Question q2 = new Question("t2", "con2").writeBy(UserTest.JAVAJIGI);
		Question q3 = new Question("t3", "con3").writeBy(UserTest.SANJIGI);
		Question q4 = new Question("t4", "con4").writeBy(UserTest.JAVAJIGI);
		List<Question> expectList = Arrays.asList(q1, q2, q3, q4);
		questionRepository.saveAll(expectList);

		// when
		List<Question> questionList = questionRepository.findByDeletedFalse();

		// then
		assertAll(
			() -> assertThat(questionList).contains(q1),
			() -> assertThat(questionList).containsAll(expectList)
		);
	}

	@Test
	void 질문_제목_null_예외발생_테스트() {
		// given
		Question q1 = new Question(null, null).writeBy(UserTest.SANJIGI);

		// when // then
		assertThatThrownBy(() -> {
			questionRepository.save(q1);
		}).isInstanceOf(DataIntegrityViolationException.class);
	}
}
