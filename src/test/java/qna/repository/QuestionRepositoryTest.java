package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.repository.UserRepositoryTest.JAVAJIGI;
import static qna.repository.UserRepositoryTest.SANJIGI;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
public class QuestionRepositoryTest {
    public static final Question QUESTION1 = new Question("title1", "contents1");
    public static final Question QUESTION2 = new Question("title2", "contents2");

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User javajigi;
    private User sanjigi;

    @BeforeEach
    void setUp() {
        javajigi = userRepository.save(JAVAJIGI);
        sanjigi = userRepository.save(SANJIGI);
    }

    @Nested
    @DisplayName("명령")
    class Command {
        private Question question;

        @BeforeEach
        void setUp() {
            question = QUESTION1.writeBy(javajigi);
        }

        @Test
        @DisplayName("새로운 질문을 추가한다.")
        void save() {
            Question actual = questionRepository.save(question);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
                    () -> assertThat(actual.getContents()).isEqualTo(question.getContents())
            );
        }
    }

    @Nested
    @DisplayName("조회")
    class Query {
        private Question question1;
        private Question question2;

        @BeforeEach
        void setUp() {
            question1 = questionRepository.save(QUESTION1.writeBy(javajigi));

            QUESTION2.setDeleted(true);
            question2 = questionRepository.save(QUESTION2.writeBy(sanjigi));
        }

        @Test
        @DisplayName("UserId로 질문을 찾는다.")
        void findByUserId() {
            Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question1.getId());
            assertAll(
                    () -> assertThat(actual).isNotEmpty(),
                    () -> assertThat(actual).map(Question::getTitle).hasValue(question1.getTitle()),
                    () -> assertThat(actual).map(Question::getContents).hasValue(question1.getContents()),
                    () -> assertThat(actual).map(Question::isDeleted).hasValue(Boolean.FALSE)
            );
        }

        @Test
        @DisplayName("삭제되지 않은 질문을 찾는다.")
        void findByDeletedFalse() {
            List<Question> actual = questionRepository.findByDeletedFalse();
            assertAll(
                    () -> assertThat(actual).isNotEmpty(),
                    () -> assertThat(actual).hasSameElementsAs(Arrays.asList(question1))
            );
        }
    }
}
