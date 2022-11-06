package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.JAVAJIGI;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("삭제되지 않은 모든 질문들을 조회할 수 있어야 한다")
    void findByDeletedFalse() {
        // given
        User user = userRepository.save(JAVAJIGI);
        List<Question> expected = questionRepository.saveAll(Arrays.asList(Q1.writeBy(user), Q2.writeBy(user)));

        // when
        List<Question> actual = questionRepository.findByDeletedFalse();

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("입력된 아이디에 해당하면서, 삭제되지 않은 질문을 조회할 수 있어야 한다")
    void findByIdAndDeletedFalse() {
        // given
        User user = userRepository.save(JAVAJIGI);
        Question expected = questionRepository.save(Q1.writeBy(user));

        // when
        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(expected.getId());

        // then
        assertThat(result).contains(expected);

    }
}
