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
class QuestionRepositoryTest {

  @Autowired
  private QuestionRepository questionRepository;
  @Autowired
  private UserRepository userRepository;

  private User savedUser;

  @BeforeEach
  void setUp() {
    User toSaveUser = new User("ted", "password", "name", "enemfk777@gmail.com");
    savedUser = userRepository.save(toSaveUser);
  }

  @DisplayName("저장 후 반환 값은 원본과 같다.")
  @Test
  void saveTest() {
    //given
    Question toSaveQuestion = new Question("tedTitle1", "tedContent1").writeBy(savedUser);

    //when & then
    Question saved = questionRepository.save(toSaveQuestion);
    assertAll(
        () -> assertThat(saved.getId()).isNotNull(),
        () -> assertThat(saved.getWriter()).isEqualTo(toSaveQuestion.getWriter()),
        () -> assertThat(saved.getContents()).isEqualTo(toSaveQuestion.getContents()),
        () -> assertThat(saved.getTitle()).isEqualTo(toSaveQuestion.getTitle())
    );
  }

  @DisplayName("deleted가 false인 데이터는 id로 조회할 수 있다.")
  @Test
  void findByIdAndDeletedFalseTest() {
    //given
    Question toSaveQuestion = new Question("tedTitle1", "tedContent1").writeBy(savedUser);

    //when & then
    Question saved = questionRepository.save(toSaveQuestion);
    assertAll(
        () -> assertThat(questionRepository.findByIdAndDeletedFalse(saved.getId())).hasValue(saved),
        () -> saved.setDeleted(true),
        () -> questionRepository.save(saved),
        () -> assertThat(questionRepository.findByIdAndDeletedFalse(saved.getId())).isNotPresent()
    );
  }

  @DisplayName("deleted가 false인 데이터는 연관된 QuestionId로 조회하면 응답에 포함되어 있다.")
  @Test
  void findByDeletedFalseTest() {
    //given
    Question toSaveQuestion = new Question("tedTitle1", "tedContent1").writeBy(savedUser);

    //when & then
    Question saved = questionRepository.save(toSaveQuestion);
    assertAll(
        () -> assertThat(questionRepository.findByDeletedFalse()).contains(saved),
        () -> saved.setDeleted(true),
        () -> questionRepository.save(saved),
        () -> assertThat(questionRepository.findByDeletedFalse()).isEmpty()
    );
  }

}
