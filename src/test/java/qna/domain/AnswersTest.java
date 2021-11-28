package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswersTest {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	private Question question1;
	private Question question2;
	private Answer answer1;
	private Answer answer2;
	private Answer answer3;
	private Answer answer4;

	@BeforeEach
	void registerQuestionAndAnswer() {
		// TODO 중복코드 제거 방법 생각하기.
		question1 = questionRepository.save(QuestionTest.Q1);
		question2 = questionRepository.save(QuestionTest.Q2);
		answer1 = answerRepository.save(new Answer(1L, UserTest.JAVAJIGI, question1, "Answers Contents1"));
		answer2 = answerRepository.save(new Answer(2L, UserTest.SANJIGI, question1, "Answers Contents2"));
		answer3 = answerRepository.save(new Answer(3L, UserTest.SANJIGI, question2, "Answers Contents3"));
		answer4 = answerRepository.save(new Answer(4L, UserTest.SANJIGI, question2, "Answers Contents4"));
	}

	@Test
	@DisplayName("질문 ID를 통해 답변 목록 가져오기")
	void getAnswers() {
		List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());
		assertThat(answers.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("질문에 작성된 답변들이 모두 같은 사용자가 작성한 것인지 확인")
	void isAnswersOwner() {
		List<Answer> answers1 = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());
		boolean isOwner1 = answers1.stream().allMatch(answer -> answer.isOwner(question1.getWriter()));
		assertThat(isOwner1).isFalse();

		List<Answer> answers2 = answerRepository.findByQuestionIdAndDeletedFalse(question2.getId());
		boolean isOwner2 = answers2.stream().allMatch(answer -> answer.isOwner(question2.getWriter()));
		assertThat(isOwner2).isTrue();
	}
}
