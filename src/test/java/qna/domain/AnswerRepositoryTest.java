package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	AnswerRepository answers;

	@Test
	@DisplayName("답변을 생성하면, ID가 존재해야 한다")
	public void saveTest(){
		// given
		Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

		// when
		Answer result = answers.save(answer);

		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result).isEqualTo(answer);
		assertThat(result == answer).isTrue();
	}

	@Test
	@DisplayName("답변을 업데이트하면, 업데이트한 내용이 확인되어야 한다")
	public void updateTest(){
		// given
		Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		String updateContents = "업데이트 된 내용입니다";
		answers.save(answer);
		answer.setContents(updateContents);

		// when
		answers.save(answer);

		// then
		assertThat(answer.getContents()).isEqualTo(updateContents);
	}

	@Test
	@DisplayName("답변을 삭제하면, 답변을 찾을 수 없어야 한다")
	public void deleteTest(){
		// given
		Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		answers.save(answer);
		answers.delete(answer);

		// when
		Optional<Answer> answerOptional = answers.findById(answer.getId());

		// then
		assertThat(answerOptional.isPresent()).isFalse();
	}

	@Test
	@DisplayName("답변을 아이디로 찾을 수 있어야 한다")
	public void findByQuestionIdAndDeletedFalseTest(){
		// given
		Answer answer1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer answer2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
		answers.save(answer1);
		answers.save(answer2);

		// when
		Optional<Answer> founded = answers.findByIdAndDeletedFalse(answer1.getId());

		// then
		assertThat(founded.isPresent()).isTrue();
	}

	@Test
	@DisplayName("답변을 질문아이디로 찾을 수 있어야 한다")
	public void findByQuestionIdAndDeletedFalse(){
		// given
		Answer answer1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer answer2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
		answer1.setQuestionId(1L);
		answer2.setQuestionId(2L);
		answers.save(answer1);
		answers.save(answer2);

		// when
		List<Answer> founded = answers.findByQuestionIdAndDeletedFalse(1L);

		// then
		assertThat(founded.size()).isEqualTo(1);
	}
}
