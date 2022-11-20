package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("답변 저장소 테스트")
class AnswerRepositoryTests {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private User user;
    private Question question;

    @BeforeEach
    void before() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
    }

    @Test
    @DisplayName("답변을 저장한다.")
    void save() {
        Answer expected = new Answer(user, question, "Answers Contents1");
        Answer answer = answerRepository.save(expected);

        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(answer.getQuestion()).isEqualTo(expected.getQuestion()),
                () -> assertThat(answer.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(answer.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("식별자로 답변을 조회한다.")
    void findById() {
        Answer expected = answerRepository.save(new Answer(user, question, "Answers Contents1"));

        Answer answer = answerRepository.findById(expected.getId()).get();

        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(expected.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(answer.getQuestion()).isEqualTo(expected.getQuestion()),
                () -> assertThat(answer.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(answer.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("식별자로 삭제되지 않은 답변을 조회한다.")
    void findByIdAndDeletedFalse() {
        Answer expected = answerRepository.save(new Answer(user, question, "Answers Contents1"));

        Answer answer = answerRepository.findByIdAndDeletedFalse(expected.getId()).get();
        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(expected.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(answer.getQuestion()).isEqualTo(expected.getQuestion()),
                () -> assertThat(answer.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(answer.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("답변을 삭제한다.")
    void delete() {
        Answer expected = answerRepository.save(new Answer(user, question, "Answers Contents1"));

        answerRepository.delete(expected);

        assertThat(answerRepository.findById(expected.getId())).isEmpty();
    }
}
