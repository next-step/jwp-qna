package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.CannotDeleteException;
import qna.domain.commons.*;

import javax.persistence.EntityManager;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import static org.springframework.test.annotation.DirtiesContext.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class QuestionTest {
  @Autowired
  QuestionRepository questionRepository;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void setUp() {
    entityManager.createNativeQuery("ALTER TABLE `question` ALTER COLUMN `id` RESTART WITH 1")
      .executeUpdate();

    Question q1 = QuestionFactory.create(Title.of("test"), Contents.of("contents"));
    Question q2 = QuestionFactory.create(Title.of("test q2"), Contents.of("contents q2"));
  }

  @DisplayName("질문을 저장한다.")
  @Test
  void save() {
    Question q1 = QuestionFactory.create(Title.of("test"), Contents.of("contents"));
    Question q2 = QuestionFactory.create(Title.of("test q2"), Contents.of("contents q2"));
    Question actual = questionRepository.save(q1);

    assertAll(
      () -> assertThat(actual.getId()).isNotNull(),
      () -> assertThat(actual.getTitle()).isEqualTo(q1.getTitle()),
      () -> assertThat(actual.getContents()).isEqualTo(q1.getContents())
    );
  }

  @DisplayName("질문을 ID로 찾는다.")
  @Test
  void findById() {
    Question q1 = QuestionFactory.create(Title.of("test"), Contents.of("contents"));
    Question q2 = QuestionFactory.create(Title.of("test q2"), Contents.of("contents q2"));
    questionRepository.save(q1);

    Question question = questionRepository.findById(q1.getId()).orElse(null);

    assertAll(
      () -> assertThat(question.getId()).isNotNull(),
      () -> assertThat(question.getTitle()).isEqualTo(q1.getTitle()),
      () -> assertThat(question.getContents()).isEqualTo(q1.getContents())
    );
  }

  @DisplayName("모든 질문을 검색한다.")
  @Test
  void findAll() {
    Question q1 = QuestionFactory.create(Title.of("test"), Contents.of("contents"));
    Question q2 = QuestionFactory.create(Title.of("test q2"), Contents.of("contents q2"));
    questionRepository.save(q1);
    questionRepository.save(q2);

    assertThat(questionRepository.findAll().size()).isEqualTo(2);
  }

  @DisplayName("모든 질문을 삭제한다.")
  @Test
  void deleteAll() {
    Question q1 = QuestionFactory.create(Title.of("test"), Contents.of("contents"));
    Question q2 = QuestionFactory.create(Title.of("test q2"), Contents.of("contents q2"));
    questionRepository.save(q1);
    questionRepository.save(q2);

    assertThat(questionRepository.count()).isEqualTo(2);

    questionRepository.deleteAll();

    assertThat(questionRepository.count()).isEqualTo(0);
  }

  @DisplayName("로그인 User id와 Question writer_id가 같지 않을 경우 예외를 던진다.")
  @Test
  void deleteByLoginUser() throws CannotDeleteException {
    User loginUser = UserFactory.create(1L, UserId.of("test"), Password.of("test"), Name.of("js"), Email.of("nextstep@gmail.com"));
    User questionWriter = UserFactory.create(2L, UserId.of("test"), Password.of("test"), Name.of("test"), Email.of("test@gmail.com"));
    Question q1 = QuestionFactory.create(Title.of("test"), Contents.of("contents")).writeBy(questionWriter);

    assertThatThrownBy(() -> q1.delete(loginUser))
      .isInstanceOf(CannotDeleteException.class);
  }

  @DisplayName("Question 하위에 답변이 없는 경우 삭제 가능하다.")
  @Test
  void deleteIfAnswersIsNotExists() throws CannotDeleteException {
    User loginUser = UserFactory.create(1L, UserId.of("test"), Password.of("test"), Name.of("js"), Email.of("nextstep@gmail.com"));
    Question q1 = QuestionFactory.create(Title.of("test"), Contents.of("contents")).writeBy(loginUser);

    q1.delete(loginUser);

    assertThat(q1.isDeleted()).isTrue();
  }

  @DisplayName("Question 하위에 답변 중 다른 다른 사람이 쓴 답변이 있을 경우 삭제할 수 없다.")
  @Test
  void deleteIfAnswersWriterIsLoginUser() {
    User loginUser = UserFactory.create(1L, UserId.of("test"), Password.of("test"), Name.of("js"), Email.of("nextstep@gmail.com"));
    User answerUser = UserFactory.create(2L, UserId.of("test"), Password.of("test"), Name.of("test"), Email.of("test@gmail.com"));
    Question q1 = QuestionFactory.create(Title.of("test"), Contents.of("contents")).writeBy(loginUser);

    Answer a1 = AnswerFactory.create(loginUser, q1, Contents.of("answer1"));
    Answer a2 = AnswerFactory.create(answerUser, q1, Contents.of("answer2"));

    q1.setAnswers(new Answers(Arrays.asList(a1, a2)));

    assertThatThrownBy(() -> q1.delete(loginUser))
      .isInstanceOf(CannotDeleteException.class)
      .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
  }

  @DisplayName("Question 내에 모든 Answer를 삭제 처리(deleted -> true)한다.")
  @Test
  void deleteQuestionAndAnswers() throws CannotDeleteException {
    User loginUser = UserFactory.create(1L, UserId.of("test"), Password.of("test"), Name.of("js"), Email.of("nextstep@gmail.com"));
    Question q1 = QuestionFactory.create(Title.of("test"), Contents.of("contents")).writeBy(loginUser);

    Answer a1 = AnswerFactory.create(loginUser, q1, Contents.of("answer1"));
    Answer a2 = AnswerFactory.create(loginUser, q1, Contents.of("answer2"));

    q1.setAnswers(new Answers(Arrays.asList(a1, a2)));
    q1.delete(loginUser);

    assertThat(q1.getAnswers().isAllDeleted()).isTrue();
  }

}
