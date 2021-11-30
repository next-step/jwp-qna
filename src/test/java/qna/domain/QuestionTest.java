package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1");
	public static final Question Q2 = new Question("title2", "contents2");

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	void save() {
		User user = userRepository.save(UserTest.JAVAJIGI);
		Question actual = questionRepository.save(new Question("questionTitle", "questionContents").writeBy(user));

		assertThat(actual).isNotNull();
	}

	@Test
	void findById() {
		User user = userRepository.save(UserTest.JAVAJIGI);
		Question expected = questionRepository.save(new Question("questionTitle", "questionContents").writeBy(user));
		Optional<Question> actual = questionRepository.findById(expected.getId());

		assertThat(actual).hasValue(expected);
	}

	@Test
	void createWithWriter() {
		User user = userRepository.save(UserTest.JAVAJIGI);
		Question question = new Question("questionTitle", "questionContents").writeBy(user);
		Question actual = questionRepository.save(question);

		assertThat(actual).isNotNull();
		assertThat(actual.getWriter()).isNotNull();
	}

	@Test
	void readWithWriter() {
		User user = userRepository.save(UserTest.JAVAJIGI);
		Question question = new Question("questionTitle", "questionContents").writeBy(user);

		Optional<Question> actual = questionRepository.findById(questionRepository.save(question).getId());
		assertThat(actual).hasValue(question);
	}

	@Test
	void updateWriter() {
		User user = userRepository.save(UserTest.JAVAJIGI);
		User user2 = userRepository.save(UserTest.SANJIGI);
		Question actual = questionRepository.save(new Question("questionTitle", "questionContents").writeBy(user));

		actual.writeBy(user2);

		assertThat(actual.getWriter()).isEqualTo(user2);
	}

	@Test
	void deleteWriter() {
		User user = userRepository.save(UserTest.JAVAJIGI);
		Question actual = questionRepository.save(new Question("questionTitle", "questionContents").writeBy(user));

		actual.writeBy(null);

		assertThat(actual.getWriter()).isNull();
	}

	@Test
	@DisplayName("로그인유저가 작성자가 아닌 경우 예외")
	void delete1() {
		User writer = userRepository.save(UserTest.JAVAJIGI);
		User otherUser = userRepository.save(UserTest.SANJIGI);
		Question question = questionRepository.save(new Question("questionTitle", "questionContents").writeBy(writer));

		assertThatThrownBy(() -> question.delete(otherUser))
			.isInstanceOf(CannotDeleteException.class)
			.hasMessage(Question.MESSAGE_NOT_AUTHENTICATED_ON_DELETE);
	}

	@Test
	@DisplayName("delete 성공")
	void delete2() throws CannotDeleteException {
		User writer = userRepository.save(UserTest.JAVAJIGI);
		Question question = questionRepository.save(new Question("questionTitle", "questionContents").writeBy(writer));
		question.delete(writer);

		assertThat(question).hasFieldOrPropertyWithValue("deleted", true);
	}

	@Test
	void saveWithAnswers() {
		User writer = userRepository.save(UserTest.JAVAJIGI);
		Question question = new Question("questionTitle", "questionContents").writeBy(writer);
		Answer answer = answerRepository.save(new Answer(writer, question, "answer Contents"));
		question.addAnswer(answer);
		Question actual = questionRepository.save(question);

		Answers answers = new Answers();
		answers.add(answer);

		assertThat(actual).isNotNull();
		assertThat(answers.get()).isEqualTo(actual.getAnswers().get());
	}

	@Test
	void findWithAnswers() {
		User writer = userRepository.save(UserTest.JAVAJIGI);
		User other = userRepository.save(UserTest.SANJIGI);
		Question question = new Question("questionTitle", "questionContents").writeBy(writer);
		Answer answer = new Answer(writer, question, "answer Contents");
		Answer answer2 = new Answer(other, question, "answer contents2");
		answerRepository.save(answer);
		answerRepository.save(answer2);
		question.addAnswer(answer);
		question.addAnswer(answer2);
		question = questionRepository.save(question);

		Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question.getId());

		assertThat(actual).hasValue(question);
	}
}
