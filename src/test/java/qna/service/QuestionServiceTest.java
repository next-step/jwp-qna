package qna.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
public class QuestionServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;
	
	@Test
	@DisplayName("저장 후 조회 테스트")
	void save_find_test() {
		User eaststar1129 = new User("eaststar1129", "password", "eaststar", "eaststar1129@eamil.com");
		User saveUser = userRepository.save(eaststar1129);
		Optional<User> findUser = userRepository.findByUserId(saveUser.getUserId());

		Question question = new Question("title", "Question contents").writeBy(findUser.get());
		Question saveQuestion = questionRepository.save(question);
		Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());
		
		assertAll(() -> assertTrue(findQuestion.isPresent()), 
				() -> assertNotNull(findQuestion.get().getId()), 
				() -> assertEquals(findQuestion.get().getTitle(), "title"),
				() -> assertEquals(findQuestion.get().getContents(), "Question contents"), 
				() -> assertTrue(findQuestion.get().isOwner(eaststar1129)),
				() -> assertNotNull(findQuestion.get().getCreatedAt()));
	}
}
