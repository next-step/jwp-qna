package qna.domain;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionRepositoryTest extends QnATest {

	@Test
	@DisplayName("데이터가 정상적으로 저장되는지 확인")
	void save() {
		// given
		Question question = createQuestion(createUser(), TITLE_1, CONTENTS_1);

		// when
		Question findQuestion = questionRepository.findById(question.getId()).get();

		// then
		assertThat(findQuestion.equalsTitleAndContentsAndNotDeleted(question)).isTrue();
	}

	@Test
	@DisplayName("전체 데이터수 - (삭제여부 true) = 전체 건수 검증")
	void given_questions_when_changeDeleteToTrue_then_excludeDeleteIsTrue() {

		// given
		User user = createUser();
		createQuestion(user, TITLE_1, CONTENTS_1);
		createQuestion(user, TITLE_2, CONTENTS_2);

		// when
		List<Question> questions = questionRepository.findAll();
		Question question = questions.get(0);
		question.setDeleted(true);

		// then
		List<Question> expectedQuestion = questionRepository.findByDeletedFalse();
		assertThat(expectedQuestion.size()).isEqualTo(1);
		assertThat(expectedQuestion.get(0)).isNotEqualTo(question);
	}

	@Test
	void 컨텐츠에서_답변_여러개_등록_확인() {

		// given
		User user = createUser();
		Question question = new Question(TITLE_1, CONTENTS_1);
		Answer firstAnswer = new Answer(user, question, CONTENTS_1);
		Answer secondAnswer = new Answer(user, question, CONTENTS_2);
		question.addAnswer(firstAnswer);
		question.addAnswer(secondAnswer);

		// when
		Question saveQuestion = questionRepository.save(question);

		// then
		assertThat(saveQuestion).isEqualTo(question);

	}

	@Test
	void 컨텐츠에서_답변_여러개_등록을_리스트로_확인() {

		// given
		User user = createUser();
		Question question = new Question(TITLE_1, CONTENTS_1);
		Answer firstAnswer = new Answer(user, question, CONTENTS_1);
		Answer secondAnswer = new Answer(user, question, CONTENTS_2);
		question.addAnswers(asList(firstAnswer, secondAnswer));

		// when
		Question saveQuestion = questionRepository.save(question);

		// then
		assertThat(saveQuestion).isEqualTo(question);

	}

	@Test
	void 타이틀이름으로_조회한_데이터의_답글_확인() {

		// given
		User user = createUser();
		Question question = createQuestion(user, TITLE_1, CONTENTS_1);
		Answer answer = new Answer(user, question, CONTENTS_1);
		question.addAnswer(answer);
		Question saveQuestion = questionRepository.save(question);

		// when
		Question expectedAnswer = questionRepository.findById(saveQuestion.getId()).get();
		Answers answers = expectedAnswer.getAnswers();

		// then
		Assertions.assertThat(answers).isEqualTo(saveQuestion.getAnswers());
	}

}
