package qna.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
public class DeleteHistoryServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	private DeleteHistoryService deleteHistoryService;
	
	@BeforeEach
	void setup() {
		deleteHistoryService = new DeleteHistoryService(deleteHistoryRepository);
	}

	@Test
	@DisplayName("저장 후 조회 테스트")
	void save_find_test() {
		User eaststar1129 = new User("eaststar1129", "password", "eaststar", "eaststar1129@eamil.com");
		User saveUser = userRepository.save(eaststar1129);
		User tester = userRepository.save(new User("tester", "password", "tester", "tester@eamil.com"));
		User deleter = userRepository.save(new User("deleter", "password", "deleter", "deleter@eamil.com"));
		Optional<User> findUser = userRepository.findByUserId(saveUser.getUserId());

		Question question = new Question("title", "Question contents").writeBy(findUser.get());
		Question saveQuestion = questionRepository.save(question);
		Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

		Answer answer = new Answer(tester, findQuestion.get(), "Answer Contents");
		Answer saveAnswer = answerRepository.save(answer);
		Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

		DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, findAnswer.get().getId(),
				deleter.getId(), LocalDateTime.now());
		deleteHistoryService.save(deleteHistory);
		List<DeleteHistory> findDeleteHistorys = deleteHistoryRepository.findAll();
		assertEquals(findDeleteHistorys.size(), 1);
	}
	
	@Test
	@DisplayName("리스트 저장 후 조회 테스트")
	void saveAll_find_test() {
		User eaststar1129 = new User("eaststar1129", "password", "eaststar", "eaststar1129@eamil.com");
		User saveUser = userRepository.save(eaststar1129);
		User tester = userRepository.save(new User("tester", "password", "tester", "tester@eamil.com"));
		User deleter = userRepository.save(new User("deleter", "password", "deleter", "deleter@eamil.com"));
		Optional<User> findUser = userRepository.findByUserId(saveUser.getUserId());

		Question question = new Question("title", "Question contents").writeBy(findUser.get());
		Question saveQuestion = questionRepository.save(question);
		Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

		Answer answer = new Answer(tester, findQuestion.get(), "Answer Contents");
		Answer saveAnswer = answerRepository.save(answer);
		Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

		DeleteHistory deleteHistory1 = new DeleteHistory(ContentType.ANSWER, findAnswer.get().getId(),
				deleter.getId(), LocalDateTime.now());
		
		DeleteHistory deleteHistory2 = new DeleteHistory(ContentType.QUESTION, findQuestion.get().getId(),
				deleter.getId(), LocalDateTime.now());
		List<DeleteHistory> deleteHistorys = new ArrayList<>();
		deleteHistorys.add(deleteHistory1);
		deleteHistorys.add(deleteHistory2);
		
		deleteHistoryService.saveAll(deleteHistorys);
		List<DeleteHistory> findDeleteHistorys = deleteHistoryRepository.findAll();
		assertEquals(findDeleteHistorys.size(), 2);
	}
}
