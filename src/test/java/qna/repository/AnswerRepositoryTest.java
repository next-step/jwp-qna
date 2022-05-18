package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.repository.UserRepositoryTest.JAVAJIGI;
import static qna.repository.UserRepositoryTest.SANJIGI;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(JAVAJIGI);
        User sanjigi = userRepository.save(SANJIGI);

        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));

        answer1 = new Answer(javajigi, question, "Answers Contents1");
        answer2 = new Answer(sanjigi, question, "Answers Contents2");
    }

    @Nested
    @DisplayName("명령")
    class Command {
        @Test
        @DisplayName("새로운 답변을 추가한다.")
        void save() {
            Answer actual = answerRepository.save(answer1);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getWriter()).isEqualTo(answer1.getWriter()),
                    () -> assertThat(actual.getQuestionId()).isEqualTo(answer1.getQuestionId()),
                    () -> assertThat(actual.getContents()).isEqualTo(answer1.getContents()));
        }
    }

    @Nested
    @DisplayName("조회")
    class Query {
        @BeforeEach
        void setUp() {
            answer1.setDeleted(true);
            answerRepository.save(answer1);
            answerRepository.save(answer2);
        }

        @Test
        @DisplayName("질문ID로 삭제되지 않은 답변을 찾는다.")
        void findByQuestionIdAndDeletedFalse() {
            List<Answer> actual = answerRepository.findByQuestion_IdAndDeletedFalse(answer1.getQuestionId());
            assertAll(
                    () -> assertThat(actual).isNotEmpty(),
                    () -> assertThat(actual).containsExactly(answer2)
            );
        }

        @Test
        @DisplayName("삭제되지 않은 답변을 ID로 찾는다.")
        void findByIdAndDeletedFalse() {
            Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer2.getId());
            assertAll(
                    () -> assertThat(actual).isNotEmpty(),
                    () -> assertThat(actual).map(Answer::getContents).hasValue(answer2.getContents()),
                    () -> assertThat(actual).map(Answer::getWriter).hasValue(answer2.getWriter()),
                    () -> assertThat(actual).map(Answer::isDeleted).hasValue(Boolean.FALSE)
            );
        }
    }
}
