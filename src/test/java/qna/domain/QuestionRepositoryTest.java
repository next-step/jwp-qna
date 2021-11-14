package qna.domain;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.AfterTransaction;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class QuestionRepositoryTest {

	public static final String TITLE_1 = "title1";
	public static final String TITLE_2 = "title2";
	public static final String CONTENTS_1 = "contents1";
	public static final String CONTENTS_2 = "contents2";
	public static final String ANSWER_1 = "답글1";
	public static final String ANSWER_2 = "답글2";
	public static final String ANSWER_3 = "답글3";

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		User javajigi = userRepository.save(JAVAJIGI);
		User sanjigi = userRepository.save(SANJIGI);

		Question question1 = new Question(null, TITLE_1, CONTENTS_1).writeBy(javajigi);
		question1.addAnswer(new Answer(null, javajigi, question1, ANSWER_1));
		question1.addAnswer(new Answer(null, javajigi, question1, ANSWER_2));

		Question question2 = new Question(null, TITLE_2, CONTENTS_2).writeBy(sanjigi);
		question2.addAnswer(new Answer(null, sanjigi, question2, ANSWER_3));
		questionRepository.saveAll(asList(question1,question2));
	}

	@Test
	@DisplayName("데이터가 정상적으로 저장되는지 확인")
	void save() {
		// when
		List<Question> questions = questionRepository.findAll();

		// then
		assertThat(questions.size()).isEqualTo(2);
		assertAll(
			() -> assertThat(questions.get(0).getTitle().equals(TITLE_1)).isTrue(),
			() -> assertThat(questions.get(0).getContents().equals(CONTENTS_1)).isTrue(),
			() -> assertThat(questions.get(0).getAnswers().size() == 2).isTrue()
		);
	}

	@Test
	@DisplayName("전체 데이터수 - (삭제여부 true) = 전체 건수 검증")
	void given_questions_when_changeDeleteToTrue_then_excludeDeleteIsTrue() {

		// when
		List<Question> questions = questionRepository.findAll();
		Question question = questions.get(0);
		question.setDeleted(true);

		List<Question> expectedQuestion = questionRepository.findByDeletedFalse();

		// then
		assertThat(expectedQuestion.size()).isEqualTo(1);
		assertThat(expectedQuestion.get(0)).isNotEqualTo(question);
	}

	@Test
	void 타이틀이름으로_조회한_데이터의_답글수_확인() {

		// when
		Question question = questionRepository.findByTitle(TITLE_1).get();
		List<Answer> answers = question.getAnswers();

		// then
		assertThat(answers.size()).isEqualTo(2);
	}
}
