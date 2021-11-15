package qna.domain;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static qna.domain.AnswerTest.*;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	AnswerRepository answerRepository;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	UserRepository userRepository;

	@Test
	void 삭제되지_않은_대상건중_한건을_true변경시_제외한_대상건만_조회() {

		User javajigi = userRepository.save(JAVAJIGI);

		Question question1 = new Question(null, "title1", "contents1").writeBy(javajigi);
		question1.addAnswer(new Answer(null, javajigi, question1, "답글1"));
		question1.addAnswer(new Answer(null, javajigi, question1, "답글2"));
		questionRepository.save(question1);

		// when
		List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());
		Answer answer = answers.get(0);
		answer.setDeleted(true);

		List<Answer> expectedAnswers = answerRepository.findByDeletedFalse();

		// then
		assertThat(expectedAnswers.size() == 1).isTrue();
	}
}
