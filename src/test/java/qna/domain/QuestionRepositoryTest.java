package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    void save() {
        final User writer = userRepository.save(UserTest.TESTUSER);
        final Question actual = questionRepository.save(QuestionTest.Q3.writeBy(writer));
        assertThat(actual.getId()).isEqualTo(QuestionTest.Q3.getId());
    }

    @Test
    @DisplayName("삭제되지 않은 질문들을 찾는다")
    void findByDeletedFalse() {
        final List<User> writers = userRepository.saveAll(Arrays.asList(UserTest.JAVAJIGI, UserTest.SANJIGI));
        final List<Question> questions = questionRepository.saveAll(Arrays.asList(
                QuestionTest.Q1.writeBy(writers.get(0)), QuestionTest.Q2.writeBy(writers.get(1))));
        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertThat(findQuestions.size()).isEqualTo(2);
        for (Question question : findQuestions) {
            assertThat(question.isDeleted()).isFalse();
        }
    }

    @Test
    @DisplayName("삭제되지 않은 질문들을 id 를 통해서 찾는다")
    void findByIdAndDeletedFalse() {
        final User writer = userRepository.save(UserTest.TESTUSER);
        final List<Question> questions = questionRepository.saveAll(Arrays.asList(QuestionTest.Q4.writeBy(writer)));
        //questions.get(0).setDeleted(true);
        questionRepository.delete(questions.get(0));

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(QuestionTest.Q4.getId());
        assertThat(findQuestion.isPresent()).isFalse();
    }
}