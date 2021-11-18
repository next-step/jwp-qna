package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
class QuestionRepositoryTest {
	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	UserRepository userRepository;

	User user;
	User otherUser;

	@BeforeEach
	public void setUp() {
		user = userRepository.save(UserTest.JAVAJIGI);
		otherUser = userRepository.save(UserTest.SANJIGI);
	}

	@Test
	@DisplayName("Question 생성 테스트")
	public void QuestionRepositoryCreateTest() {
		//given
		//when
		Question savedQuestion = questionRepository.save(QuestionTest.Q1.writeBy(user));
		//then
		Question findQuestion = questionRepository.findById(savedQuestion.getId()).get();
		assertThat(findQuestion.getContents()).isEqualTo(savedQuestion.getContents());
	}

	@Test
	@DisplayName("Question 동일성 테스트")
	public void QuestionRepositoryEqualsTest() {
		//given
		//when
		Question savedQuestion = questionRepository.save(QuestionTest.Q1.writeBy(user));
		Question findQuestion = questionRepository.findById(savedQuestion.getId()).get();
		//then
		assertThat(savedQuestion).isEqualTo(findQuestion);
		assertThat(savedQuestion).isSameAs(findQuestion);
	}

	@Test
	@DisplayName("Question 변경 테스트")
	public void QuestionRepositoryUpdateTest() {
		//given
		Question savedQuestion = questionRepository.save(QuestionTest.Q1.writeBy(user));
		//when
		savedQuestion.setContents("변경된 내용입니다.");
		questionRepository.flush();
		//then
		Question findQuestion = questionRepository.findById(savedQuestion.getId()).get();
		assertThat(findQuestion.getContents()).isEqualTo(savedQuestion.getContents());
	}

	@Test
	@DisplayName("Question hard delete 테스트")
	public void QuestionRepositoryHardDeleteTest() {
		//given
		Question savedOne = questionRepository.save(QuestionTest.Q1.writeBy(user));
		Question savedTwo = questionRepository.save(QuestionTest.Q1.writeBy(otherUser));
		//when
		questionRepository.deleteById(savedOne.getId());
		//then
		assertThat(questionRepository.findAll()).hasSize(1);
		assertThat(questionRepository.findAll()).contains(savedTwo);
	}

	@Test
	@DisplayName("Question soft delete 성공 테스트")
	public void QuestionRepositorySoftDeleteTest() throws Exception {
		//given
		Question savedOne = questionRepository.save(QuestionTest.Q1.writeBy(user));
		//when
		savedOne.delete(user);
		//then
		assertThat(savedOne.isDeleted()).isTrue();
	}

	@Test
	@DisplayName("Question soft delete 실패 (유저 불일치) 테스트")
	public void QuestionRepositorySoftDeleteNotSameUserTest(){
		//given
		//when
		Question savedOne = questionRepository.save(QuestionTest.Q1.writeBy(user));
		//then
		assertThatThrownBy(() -> savedOne.delete(otherUser))
			.isInstanceOf(CannotDeleteException.class)
			.hasMessage("질문을 삭제할 권한이 없습니다.");

	}

}