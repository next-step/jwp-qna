package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AnswerRepositoryTest {

  @Autowired
  private AnswerRepository answerRepository;
  @Autowired
  private QuestionRepository questionRepository;
  @Autowired
  private UserRepository userRepository;

  private User savedUser;
  private Question savedQuestion;

  @BeforeEach
  void setUp() {
    User toSaveUser = new User("ted", "password", "name", "enemfk777@gmail.com");
    savedUser = userRepository.save(toSaveUser);
    Question toSaveQuestion = new Question("tedTitle1", "tedContent1").writeBy(savedUser);
    savedQuestion = questionRepository.save(toSaveQuestion);
  }

  @DisplayName("저장 후 반환 값은 원본과 같다.")
  @Test
  void saveTest() {
    Answer given = new Answer(savedUser, savedQuestion, "Answers Contents1");
    Answer saved = answerRepository.save(given);
    assertAll(
        () -> assertThat(saved.getId()).isNotNull(),
        () -> assertThat(saved.getWriter()).isEqualTo(given.getWriter()),
        () -> assertThat(saved.getContents()).isEqualTo(given.getContents()),
        () -> assertThat(saved.getQuestion()).isEqualTo(given.getQuestion())
    );
  }

  @DisplayName("deleted가 false인 데이터는 id로 조회할 수 있다.")
  @Test
  void findByIdAndDeletedFalseTest() {
    Answer given = new Answer(savedUser, savedQuestion, "Answers Contents1");
    Answer saved = answerRepository.save(given);
    assertAll(
        () -> assertThat(answerRepository.findByIdAndDeletedFalse(saved.getId())).hasValue(saved),
        () -> saved.setDeleted(true),
        () -> answerRepository.save(saved),
        () -> assertThat(answerRepository.findByIdAndDeletedFalse(saved.getId())).isNotPresent()
    );
  }

  @DisplayName("deleted가 false인 데이터는 연관된 QuestionId로 조회하면 응답에 포함되어 있다.")
  @Test
  void findByQuestionIdAndDeletedFalseTest() {
    //given
    Answer givenAnswer = new Answer(savedUser, savedQuestion, "Answers Contents1");
    Answer savedAnswer = answerRepository.save(givenAnswer);

    assertAll(
        () -> assertThat(answerRepository.findByQuestionIdAndDeletedFalse(savedQuestion.getId())).contains(savedAnswer),
        () -> savedAnswer.setDeleted(true),
        () -> answerRepository.save(savedAnswer),
        () -> assertThat(answerRepository.findByQuestionIdAndDeletedFalse(savedQuestion.getId())).isEmpty()
    );
  }

}
