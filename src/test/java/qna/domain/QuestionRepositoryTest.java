package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Question savedQuestion;

    @BeforeEach
    void setUp() {
        User writer = userRepository.save(UserTest.JAVAJIGI);
        this.savedQuestion = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
    }

    @DisplayName("save하면 pk가 존재한다")
    @Test
    void save() {
        assertThat(savedQuestion.getId()).isNotNull();
    }

    @DisplayName("동일성 확인")
    @Test
    void findById() {
        // given & when
        Question actual = questionRepository.findById(savedQuestion.getId()).get();

        // then
        assertThat(savedQuestion).isSameAs(actual);
    }

    @DisplayName("update쿼리를 실행하면 contents가 업데이트 된다")
    @Test
    void update() {
        // given
        String newContents = "newTest";

        // when
        savedQuestion.setContents(newContents);
        questionRepository.flush();
        Question actual = questionRepository.findById(savedQuestion.getId()).get();

        // then
        assertThat(actual.getContents()).isEqualTo(newContents);
    }

    @DisplayName("삭제하고 조회하면 empty Optional이 반환된다")
    @Test
    void delete() {
        // given & when
        questionRepository.delete(savedQuestion);
        Optional<Question> actual = questionRepository.findById(savedQuestion.getId());

        // then
        assertThat(actual).isEqualTo(Optional.empty());
    }
}
