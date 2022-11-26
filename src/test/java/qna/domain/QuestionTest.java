package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.helper.QuestionHelper;
import qna.helper.UserHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
class QuestionTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save() {
        final UserHelper userHelper = new UserHelper(userRepository);
        final User JAVAJIGI = userHelper.createUser("javajigi", "password", "name", "javajigi@slipp.net");
        final User SANJIGI = userHelper.createUser("sanjigi", "password", "name", "sanjigi@slipp.net");

        final QuestionHelper questionHelper = new QuestionHelper(questionRepository);
        final Question Q1 = questionHelper.createQuestion("title1", "contents1", JAVAJIGI);
        final Question Q2 = questionHelper.createQuestion("title2", "contents2", SANJIGI);

        assertAll(
                () -> assertDoesNotThrow(() -> questionRepository.save(Q1)),
                () -> assertDoesNotThrow(() -> questionRepository.save(Q2))
        );
    }

    @Nested
    @DisplayName("find 관련 메서드")
    class FindMethods {
        private List<Question> savedQuestions;

        @BeforeEach
        void setup() {
            final User savedUser = new UserHelper(userRepository)
                    .createUser("ndka134yg", "1234", "사용자 1", "user-1@email.com");
            final List<Question> questions = new ArrayList<>(Arrays.asList(
                    new Question("question title 1", "question content 1").writeBy(savedUser),
                    new Question("question title 2", "question content 2").writeBy(savedUser),
                    new Question("question title 3", "question content 3").writeBy(savedUser).setDeleted(true)
            ));
            savedQuestions = questionRepository.saveAll(questions);
        }

        @Test
        void findByDeletedFalse() {
            final List<Question> foundQuestions = questionRepository.findByDeletedFalse();
            assertAll(
                    () -> assertThat(foundQuestions)
                            .hasSize((int) savedQuestions.stream().filter(x -> !x.isDeleted()).count()),
                    () -> assertThat(foundQuestions.stream().noneMatch(Question::isDeleted)).isTrue()
            );
        }

        @Test
        void findByIdAndDeletedFalse() {
            final Question foundQuestion =
                    questionRepository.findByIdAndDeletedFalse(savedQuestions.get(0).getId()).get();
            assertAll(
                    () -> assertThat(foundQuestion.getId()).isEqualTo(savedQuestions.get(0).getId()),
                    () -> assertThat(foundQuestion.isDeleted()).isFalse()
            );
        }
    }
}
