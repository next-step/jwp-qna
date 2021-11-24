package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.domain.commons.*;

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

  @DisplayName("답변을 저장한다.")
  @Test
  void save() {
    User questionWriter = UserFactory.create(UserId.of("test_question_pro"), Password.of("pass1word@"), Name.of("test"), Email.of("dev22@gmail.com"));
    User answerWriter = UserFactory.create(UserId.of("wonjune"), Password.of("pass1word@"), Name.of("june"), Email.of("dev@gmail.com"));
    Question question = QuestionFactory.create(Title.of("test question"), Contents.of("contents")).writeBy(questionWriter);

    Answer answer = AnswerFactory.create(answerWriter, question, Contents.of("test contents"));

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
    User questionWriter = UserFactory.create(UserId.of("test_question_pro"), Password.of("pass1word@"), Name.of("test"), Email.of("dev22@gmail.com"));
    User answerWriter = UserFactory.create(UserId.of("wonjune"), Password.of("pass1word@"), Name.of("june"), Email.of("dev@gmail.com"));
    Question question = QuestionFactory.create(Title.of("test question"), Contents.of("contents")).writeBy(questionWriter);

    Answer answer = AnswerFactory.create(answerWriter, question, Contents.of("test contents"));
    answerRepository.save(answer);

    answerRepository.delete(answer);
    assertThat(answerRepository.findById(answer.getId()).isPresent()).isFalse();
  }

  @DisplayName("답변의 내용을 수정한다.")
  @Test
  void updateContentsById() {
    User questionWriter = UserFactory.create(UserId.of("test_question_pro"), Password.of("pass1word@"), Name.of("test"), Email.of("dev22@gmail.com"));
    User answerWriter = UserFactory.create(UserId.of("wonjune"), Password.of("pass1word@"), Name.of("june"), Email.of("dev@gmail.com"));
    Question question = QuestionFactory.create(Title.of("test question"), Contents.of("contents")).writeBy(questionWriter);

    Answer answer = AnswerFactory.create(answerWriter, question, Contents.of("test contents"));
    answerRepository.save(answer);
    Contents updateContents = Contents.of("updated contents");

    answer.setContents(updateContents);

    assertThat(answerRepository.findById(answer.getId()).map(Answer::getContents).get()).isEqualTo(updateContents);
  }

  @DisplayName("ID로 답변을 찾는다.")
  @Test
  void findById() {
    User questionWriter = UserFactory.create(UserId.of("test_question_pro"), Password.of("pass1word@"), Name.of("test"), Email.of("dev22@gmail.com"));
    User answerWriter = UserFactory.create(UserId.of("wonjune"), Password.of("pass1word@"), Name.of("june"), Email.of("dev@gmail.com"));
    Question question = QuestionFactory.create(Title.of("test question"), Contents.of("contents")).writeBy(questionWriter);

    Answer answer = AnswerFactory.create(answerWriter, question, Contents.of("test contents"));
    answerRepository.save(answer);

    Answer actual = answerRepository.findById(answer.getId()).orElse(null);

    assertThat(actual).isEqualTo(answer);
  }

  @DisplayName("답변을 작성자 ID로 찾는다.")
  @Test
  void findAnswersByWriterId() {
    User questionWriter = UserFactory.create(UserId.of("test_question_pro"), Password.of("pass1word@"), Name.of("test"), Email.of("dev22@gmail.com"));
    User answerWriter = UserFactory.create(UserId.of("wonjune"), Password.of("pass1word@"), Name.of("june"), Email.of("dev@gmail.com"));
    Question question = QuestionFactory.create(Title.of("test question"), Contents.of("contents")).writeBy(questionWriter);

    Answer answer = AnswerFactory.create(answerWriter, question, Contents.of("test contents"));
    userRepository.save(answerWriter);
    userRepository.save(questionWriter);
    questionRepository.save(question);
    answerRepository.save(answer);

    assertThat(answerRepository.findByWriterId(answerWriter.getId()).get(0)).isEqualTo(answer);
  }

  @DisplayName("작성자에 의해 질문을 삭제한다.")
  @Test
  void deleteAnswerByWriter() {
    User writer = UserFactory.create(1L, UserId.of("wonjune"), Password.of("pass1word@"), Name.of("june"), Email.of("dvg@gmail.com"));
    Question question = QuestionFactory.create(Title.of("testQ"), Contents.of("test")).writeBy(writer);
    Answer answer = AnswerFactory.create(writer, question, Contents.of("test"));

    answer.delete(writer);

    assertThat(answer.isDeleted()).isTrue();
  }
}
