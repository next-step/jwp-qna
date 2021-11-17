package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
class AnswerRepositoryTest {
	@Autowired
	AnswerRepository answerRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	QuestionRepository questionRepository;

	User user;
	User otherUser;
	Question question;
	Question otherQuestion;
	Answer answer;
	Answer otherAnswer;

	@BeforeEach
	public void setUp() {
		user = userRepository.save(UserTest.JAVAJIGI);
		question = questionRepository.save(QuestionTest.Q1.writeBy(user));
		answer = new Answer(user, question, "answer contents");

		otherUser = userRepository.save(UserTest.SANJIGI);
		otherQuestion = questionRepository.save(QuestionTest.Q2.writeBy(otherUser));
		otherAnswer = new Answer(otherUser, otherQuestion, "otherAnswer contents");

	}

	@Test
	@DisplayName("Answer 생성 테스트")
	public void AnswerRepositoryCreateTest() {
		//given
		//when
		Answer savedAnswer = answerRepository.save(answer);
		Answer findAnswer = answerRepository.findById(savedAnswer.getId()).get();
		//then
		assertThat(savedAnswer.getContents()).isEqualTo(findAnswer.getContents());
	}

	@Test
	@DisplayName("Answer 동일성 테스트")
	public void AnswerRepositoryEqualsTest() {
		//given
		//when
		Answer savedAnswer = answerRepository.save(answer);
		Answer findAnswer = answerRepository.findById(savedAnswer.getId()).get();

		//then
		assertThat(savedAnswer).isEqualTo(findAnswer);
		assertThat(savedAnswer).isSameAs(findAnswer);
	}

	@Test
	@DisplayName("Answer 변경 테스트")
	public void AnswerRepositoryUpdateTest() {
		//given
		Answer savedAnswer = answerRepository.save(answer);
		//when
		Answer beforeAnswer = answerRepository.findById(savedAnswer.getId()).get();
		beforeAnswer.setContents("변경된 내용입니다.");
		answerRepository.flush();
		Answer afterAnswer = answerRepository.findById(savedAnswer.getId()).get();

		//then
		assertThat(afterAnswer.getContents()).isEqualTo("변경된 내용입니다.");
	}

	@Test
	@DisplayName("Answer 삭제 테스트")
	public void AnswerRepositoryDeleteTest() {
		//given
		Answer savedAnswer = answerRepository.save(answer);
		Answer savedOtherAnswer = answerRepository.save(otherAnswer);

		//when
		answerRepository.deleteById(savedAnswer.getId());

		//then
		assertThat(answerRepository.findAll()).hasSize(1);
		assertThat(answerRepository.findAll()).contains(savedOtherAnswer);
	}
}