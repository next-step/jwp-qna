package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User saveUser() {
        return userRepository.save(new User("mellow", "1234", "mazinga", "mazinga@example.com"));
    }

    private Question saveQuestion(User user) {
        return questionRepository.save(new Question("title1", "contents1").writeBy(user));
    }

    @Test
    @DisplayName("데이터를 저장한다.")
    void save_test() {
        User user = saveUser();
        Question save = saveQuestion(user);
        assertAll(
                () -> assertThat(save.getId()).isNotNull(),
                () -> assertThat(save.getTitle()).isEqualTo("title1"),
                () -> assertThat(save.getContents()).isEqualTo("contents1")
        );
    }

    @Test
    @DisplayName("전체 데이터를 조회한다.")
    void find_all_test() {
        User user = saveUser();
        Question save = saveQuestion(user);
        List<Question> all = questionRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("deleted로 데이터를 조회한다.")
    void find_by_deleted_test() {
        User user = saveUser();
        Question save = saveQuestion(user);
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("id와 deleted로 데이터를 조회한다.")
    void find_by_id_and_deleted_test() {
        User user = saveUser();
        Question save = saveQuestion(user);
        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(save.getId());
        questionOptional.ifPresent(question -> {
            assertAll(
                    () -> assertThat(question.getTitle()).isEqualTo("title1"),
                    () -> assertThat(question.getContents()).isEqualTo("contents1"),
                    () -> assertThat(question.isDeleted()).isFalse()
            );
        });
    }

    @Test
    @DisplayName("전체 데이터를 삭제한다.")
    void delete_all_test() {
        User user = saveUser();
        Question save = saveQuestion(user);
        questionRepository.deleteAll();
        assertThat(questionRepository.findAll().size()).isZero();
    }

    @Test
    @DisplayName("양방향 연관관계를 확인한다.")
    void relation_test() {
        User user = saveUser();
        Question save = saveQuestion(user);
        Optional<Question> questionOptional = questionRepository.findById(save.getId());
        questionOptional.ifPresent(question -> {
            assertAll(
                    () -> assertThat(question.getWriter()).isEqualTo(user),
                    () -> assertThat(user.getQuestions()).contains(question)
            );
        });
    }

    @Test
    @DisplayName("질문을 삭제하면, deleted 상태값이 변한다.")
    void delete_question_test() {
        User user = saveUser();
        Question save = saveQuestion(user);
        Optional<Question> questionOptional = questionRepository.findById(save.getId());
        questionOptional.ifPresent(question -> {
            try {
                question.delete(user);
            } catch (CannotDeleteException e) {
                e.printStackTrace();
            }
            assertThat(question.isDeleted()).isTrue();
        });
    }

    @Test
    @DisplayName("로그인 사용자와 질문한 사람이 같지 않으면 에러를 반환한다.")
    void no_owner_delete_question_test() {
        User user = saveUser();
        Question save = saveQuestion(user);
        User anotherUser = new User("another", "1234", "honggildong", "honggildong@example.com");
        Optional<Question> questionOptional = questionRepository.findById(save.getId());
        questionOptional.ifPresent(question -> {
            assertThatThrownBy(() -> {
                question.delete(anotherUser);
            }).isInstanceOf(CannotDeleteException.class)
                    .hasMessageContainingAll("질문을 삭제할 권한이 없습니다.");
        });
    }
}
