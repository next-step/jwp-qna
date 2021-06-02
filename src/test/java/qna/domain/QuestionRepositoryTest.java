package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserRepositoryTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        user1 = userRepository.save(UserRepositoryTest.JAVAJIGI);
        user2 = userRepository.save(UserRepositoryTest.SANJIGI);
    }

    @Test
    @DisplayName("question save 확인")
    void save() {
        Question question = Q1;
        Question result = questionRepository.save(new Question("title1", "contents1").writeBy(user1));

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getWriter().getId()).isEqualTo(user1.getId()),
                () -> assertThat(result.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(question.getTitle())
        );
    }

    @Test
    @DisplayName("deleted 값이 false인 Question 조회")
    void findByDeletedFalse() {
        Question question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        Question question2 = questionRepository.save(new Question("title1", "contents1").writeBy(user2));

        List<Question> resultList = questionRepository.findByDeletedFalse();

        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList).contains(question1, question2);
    }

    @Test
    @DisplayName("id로 deleted값이 false인 Question 조회")
    void findByIdAndDeletedFalse() {
        Question question1 = questionRepository.save(Q1);

        Question result = questionRepository.findByIdAndDeletedFalse(question1.getId()).get();
        assertAll(
                () -> assertThat(result.isDeleted()).isFalse(),
                () -> assertThat(result.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(question1.getTitle()),
                () -> assertThat(result.getWriter()).isEqualTo(question1.getWriter())
        );
    }

    @Test
    @DisplayName("Question Answer 양방향 테스트")
    void findById() {
        Question question = new Question(1L, "title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
        Answer answer1= new Answer(user1, question, "Answers Contents1");
        Answer answer2 = new Answer(user1, question, "Answers Contents2");
        question.addAnswers(answer1);
        question.addAnswers(answer2);

        Answers result = question.getAnswers();

        assertAll (
                () -> assertThat(result.getAnswers()).hasSize(2),
                () -> assertThat(result.getAnswers()).contains(answer1, answer2)
        );
    }

    @Test
    @DisplayName("질문 등록자가 아닌 사용자가 delete 시도시 예외 발생 테스트")
    void validOwner() {
        Question question = new Question(1L, "title1", "contents1").writeBy(user1);
        Answer answer1 = new Answer(user1, question, "Answers Contents1");
        question.addAnswers(answer1);

        assertThatThrownBy(() -> {
            question.validOwner(user2);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("Question에 다른 사람이 작성한 Answer 삭제시 예외 확인")
    void delete_another_user() throws CannotDeleteException {
        LocalDateTime deletedTime = now();
        Question question = new Question(1L, "title1", "contents1").writeBy(user1);
        Answer answer1 = new Answer(user1, question, "Answers Contents1");
        Answer answer2 = new Answer(user2, question, "Answers Contents2");
        question.addAnswers(answer1);
        question.addAnswers(answer2);

        assertThatThrownBy(() -> {
            List<DeleteHistory> deleteHistories = question.delete(user1, deletedTime);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("Question과 Answer 삭제 테스트")
    void delete() throws CannotDeleteException {
        LocalDateTime deletedTime = now();
        Question question = new Question(1L, "title1", "contents1").writeBy(user1);
        Answer answer1 = new Answer(user1, question, "Answers Contents1");
        Answer answer2 = new Answer(user1, question, "Answers Contents2");
        question.addAnswers(answer1);
        question.addAnswers(answer2);

        List<DeleteHistory> resultHistories = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer1.getId(), answer1.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, answer2.getId(), answer2.getWriter(), LocalDateTime.now())
        );
        assertThat(question.delete(user1, deletedTime)).containsAll(resultHistories);
    }
}
