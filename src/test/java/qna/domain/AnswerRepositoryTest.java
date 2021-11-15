package qna.domain;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static qna.domain.UserTest.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	AnswerRepository answerRepository;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	UserRepository userRepository;

	User user1;
	User user2;
	Question question1;
	Question question2;

	@BeforeEach
	void setUp() {
		user1 = userRepository.save(JAVAJIGI);
		user2 = userRepository.save(SANJIGI);
		question1 = new Question("공지사항1", "테스트1").writeBy(user1);
		question2 = new Question("공지사항2", "테스트2").writeBy(user2);

		questionRepository.saveAll(asList(question1, question2));
	}

	@Test
	void 삭제되지_않은_대상건중_한건을_true변경시_제외한_대상건만_조회() {
		//given
		answerRepository.save(new Answer(user1, question1, "안녕하세요"));
		answerRepository.save(new Answer(user2, question2, "안녕하세요2"));

		// when
		List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());
		Answer answer = answers.get(0);
		answer.setDeleted(true);

		List<Answer> expectedAnswers = answerRepository.findByDeletedFalse();

		// then
		assertThat(expectedAnswers.size() == 1).isTrue();
	}

	@Test
	void 여러건을_저장할때_리스트에_데이터를_담는경우_동일한_객체라도_모두_저장된다() {
		List<Answer> answers = asList(
			new Answer(user1, question1, "안녕하세요"),
			new Answer(user1, question1, "안녕하세요"),
			new Answer(user1, question1, "안녕하세요"));

		List<Answer> answers1 = answerRepository.saveAll(answers);
		assertThat(answers1.size()).isEqualTo(3);
	}

}
