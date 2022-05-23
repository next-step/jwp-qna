package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User saveUser() {
        return userRepository.save(new User("mellow", "1234", "mazinga", "mazinga@example.com"));
    }

    private Question saveQuestion(User user) {
        return questionRepository.save(new Question("title1", "contents1").writeBy(user));
    }

    private Answer saveAnswer(User user, Question question) {
        return answerRepository.save(new Answer(user, question, "Answers Contents1"));
    }

    @Test
    @DisplayName("데이터를 저장한다.")
    void save_test() {
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer save = saveAnswer(user, question);
        assertAll(
                () -> assertThat(save.getId()).isNotNull(),
                () -> assertThat(save.getContents()).isEqualTo("Answers Contents1")
        );
    }

    @Test
    @DisplayName("데이터를 모두 조회한다.")
    void find_all_test() {
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer save = saveAnswer(user, question);
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Id로 데이터를 조회한다.")
    void find_by_id_test() {
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer save = saveAnswer(user, question);
        Optional<Answer> answerOptional = answerRepository.findByIdAndDeletedFalse(save.getId());
        answerOptional.ifPresent(answer -> {
            assertAll(
                    () -> assertThat(answer.getContents()).isEqualTo("Answers Contents1"),
                    () -> assertThat(answer.getQuestion()).isNotNull(),
                    () -> assertThat(answer.getWriter()).isNotNull(),
                    () -> assertThat(answer.isDeleted()).isFalse()
            );
        });
    }

    @Test
    @DisplayName("QuestionId로 조회한다.")
    void find_By_Question_Id_Deleted_False_test() {
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer save = saveAnswer(user, question);
        List<Answer> questionIdAndDeletedFalse = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(questionIdAndDeletedFalse.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("전체 데이터를 삭제한다.")
    void delete_all_test() {
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer save = saveAnswer(user, question);
        answerRepository.deleteAll();
        assertThat(answerRepository.findAll().size()).isZero();
    }

    @Test
    @DisplayName("양방향 연관관계를 확인한다.")
    void relation_test() {
        User user = saveUser();
        Question question = saveQuestion(user);
        Answer save = saveAnswer(user, question);
        Optional<Answer> answerOptional = answerRepository.findById(save.getId());
        answerOptional.ifPresent(answer -> {
            assertAll(
                    () -> assertThat(answer.getWriter()).isEqualTo(user),
                    () -> assertThat(user.getAnswers()).contains(answer)
            );
        });
    }
}
