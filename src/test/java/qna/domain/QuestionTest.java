package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DataJpaTest
public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	private final QuestionRepository questionRepository;
	private final UserRepository userRepository;
	private final AnswerRepository answerRepository;

	private User user;
	private User user2;

	@Autowired
	public QuestionTest(QuestionRepository questionRepository, UserRepository userRepository, AnswerRepository answerRepository) {
		this.questionRepository = questionRepository;
		this.userRepository = userRepository;
		this.answerRepository = answerRepository;
	}

	@BeforeEach
	void setUp() {
		user = userRepository.save(UserTest.JAVAJIGI);
		user2 = userRepository.save(UserTest.SANJIGI);
		Question question = questionRepository.save(new Question("title1", "contents1"));
		Question questoin2 = questionRepository.save(new Question("title2", "contents2"));
		question.writeBy(user);
		questoin2.writeBy(user2);
	}

	@Test
	void save_test() {
		Question actual = questionRepository.findAll().get(0);
		assertThat(actual.getId()).isNotNull();
	}

	@Test
	void findByTitle_test() {
		Question actual = questionRepository.findFirstByTitle("title1").get();
		assertThat(actual.getTitle()).isEqualTo("title1");
	}

	@Test
	void getWriter_test() {
		Question question = questionRepository.findAll().get(0);
		User actual = question.getWriter();
		assertThat(actual.getUserId()).isEqualTo("javajigi");
	}

	@Test
	@Transactional
	void getAnswer_test() {
		String contents = "Answers Contents1";
		Question question = questionRepository.findAll().get(0);
		Answer answer = answerRepository.save(new Answer(user, question, contents));
		question.addAnswer(answer);

		List<Answer> actual = question.getAnswers();
		assertThat(actual.get(0).getContents()).isEqualTo(contents);
	}

	@Test
	@DisplayName("질문자가 아닌 유저가 질문삭제 요청을 하면 익셉션이 발생한다")
	void delete_test() {
		User loggedInUser = user;
		Question question = questionRepository.findFirstByTitle("title2").get();
		assertThatThrownBy(() -> question.delete(loggedInUser))
				.isInstanceOf(CannotDeleteException.class);
	}

	@Test
	@DisplayName("질문자가 질문 삭제 요청을 하면 삭제된다")
	void delete_test2() throws CannotDeleteException {
		User loggedInUser = user;
		Question question = questionRepository.findFirstByTitle("title1").get();
		question.delete(loggedInUser);
		assertThat(question.isDeleted()).isTrue();
	}

	@Test
	@DisplayName("질문이 삭제될 떄 답변도 같이 삭제되어야 한다")
	void answer_delete_test() throws CannotDeleteException {
		User loggedInUser = user;
		Question question = questionRepository.findFirstByTitle("title1").get();
		Answer answer = answerRepository.save(new Answer(user, question, "not loggedInUser"));
		question.addAnswer(answer);

		question.delete(loggedInUser);
		assertThat(answer.isDeleted()).isTrue();
	}

	@Test
	@DisplayName("로그인 유저가 아닌 다른작성자의 질문이 있으면 익셉션이 발생한다")
	void answer_delete_test2() {
		User loggedInUser = user;
		Question question = questionRepository.findFirstByTitle("title1").get();
		Answer answer = answerRepository.save(new Answer(user2, question, "not loggedInUser"));
		question.addAnswer(answer);

		assertThatThrownBy(() -> question.delete(loggedInUser))
				.isInstanceOf(CannotDeleteException.class);
	}
}
