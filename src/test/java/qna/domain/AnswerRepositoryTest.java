package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Answer savedAnswer;

    @BeforeEach
    void setUp() {
        User writer = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title1", "contents1", writer));
        this.savedAnswer = answerRepository.save(new Answer(writer, question, "test"));
    }

    @DisplayName("save하면 pk가 존재한다")
    @Test
    void save() {
        assertThat(savedAnswer.getId()).isNotNull();
    }

    @DisplayName("동일성 확인")
    @Test
    void findById() {
        // given & when
        Answer actual = answerRepository.findById(savedAnswer.getId()).get();

        // then
        assertThat(savedAnswer).isSameAs(actual);
    }

    @DisplayName("flush시 더티체킹을 통해 update쿼리 실행되고 contents가 업데이트 된다")
    @Test
    void update() {
        // given
        String newContents = "newTest";

        // when
        savedAnswer.setContents(newContents);
        answerRepository.flush();
        Answer actual = answerRepository.findById(savedAnswer.getId()).get();

        // when
        assertThat(actual.getContents()).isEqualTo(newContents);
    }

    @DisplayName("Answer를 삭제하고 조회하면 empty Optional이 반환된다")
    @Test
    void delete() {
        // given & when
        answerRepository.delete(savedAnswer);
        Optional<Answer> actual = answerRepository.findById(savedAnswer.getId());

        // then
        assertThat(actual).isEqualTo(Optional.empty());
    }
}
