package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
public class AnswerTest {
  @Autowired
  private AnswerRepository answerRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private QuestionRepository questionRepository;

  private User questionWriter;
  private User answerWriter;
  private Question question;

  @BeforeEach
  void setUp() {
    questionWriter = UserFactory.create("test_question_pro", "pass", "test", "dev22@gmail.com");
    answerWriter = UserFactory.create("wonjune", "pass", "june", "dev@gmail.com");
    question = QuestionFactory.create("test question", "contents").writeBy(questionWriter);
  }

  @DisplayName("답변을 저장한다.")
  @Test
  void save() {
    Answer answer = AnswerFactory.create(answerWriter, question, "test contents");

    Answer actual = answerRepository.save(answer);

    assertAll(
      () -> assertThat(actual.getId()).isNotNull(),
      () -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
      () -> assertThat(actual.getQuestion()).isEqualTo(answer.getQuestion()),
      () -> assertThat(actual.getWriter()).isEqualTo(answer.getWriter()));
  }

  @DisplayName("답변을 삭제한다.")
  @Test
  void delete() {
    Answer answer = AnswerFactory.create(answerWriter, question, "test contents");
    answerRepository.save(answer);

    answerRepository.delete(answer);
    assertThat(answerRepository.findById(answer.getId()).isPresent()).isFalse();
  }

  @DisplayName("답변의 내용을 수정한다.")
  @Test
  void updateContentsById() {
    Answer answer = AnswerFactory.create(answerWriter, question, "test contents");
    answerRepository.save(answer);
    String updateContents = "updated contents";

    answer.setContents(updateContents);

    assertThat(answerRepository.findById(answer.getId()).map(Answer::getContents).get()).isEqualTo(updateContents);
  }

  @DisplayName("ID로 답변을 찾는다.")
  @Test
  void findById() {
    Answer answer = AnswerFactory.create(answerWriter, question, "test contents");
    answerRepository.save(answer);

    Answer actual = answerRepository.findById(answer.getId()).orElse(null);

    assertThat(actual).isEqualTo(answer);
  }

  @DisplayName("답변을 작성자 ID로 찾는다.")
  @Test
  void findAnswersByWriterId() {
    Answer answer = AnswerFactory.create(answerWriter, question, "test contents");
    userRepository.save(answerWriter);
    userRepository.save(questionWriter);
    questionRepository.save(question);
    answerRepository.save(answer);

    answerWriter.getAnswer().add(answer);

    assertThat(answerRepository.findByWriterId(answerWriter.getId()).get(0)).isEqualTo(answer);
  }
}
