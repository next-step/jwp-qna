package qna.domain.question;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.NotFoundException;
import qna.domain.user.User;
import qna.domain.user.UserRepository;
import qna.domain.user.UserTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext
@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("질문이 정상적으로 등록되있는지 테스트 한다")
    void saveQuestionTest(){
        User writeUser = userRepository.save(UserTest.createUser("user1"));
        Question question = QuestionTest.createQuestion(writeUser);
        Question save = questionRepository.save(question);

        assertAll(
                () -> assertThat(save.getId()).isNotNull(),
                () -> assertThat(save.isOwner(writeUser)).isTrue(),
                () -> assertThat(save.getId()).isEqualTo(question.getId()),
                () -> assertThat(save.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(save.getWriterId()).isEqualTo(question.getWriterId()),
                () -> assertThat(question.getCreatedAt()).isEqualTo(question.getCreatedAt()),
                () -> assertThat(question.getUpdatedAt()).isEqualTo(question.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("삭제되지 않은 질문 목록 조회를 테스트한다")
    void findByDeletedFalseTest(){
        User writeUser1 = userRepository.save(UserTest.createUser("user1"));
        Question question1 = QuestionTest.createQuestion(writeUser1);
        User writeUser2 = userRepository.save(UserTest.createUser("user2"));
        Question question2 = QuestionTest.createQuestion(writeUser2);
        questionRepository.save(question1);
        questionRepository.save(question2);
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("id로 삭제되지 않은 질문 한 건 조회를 테스트한다")
    void findByIdAndDeletedFalse(){
        User writeUser = userRepository.save(UserTest.createUser("user1"));
        Question question = QuestionTest.createQuestion(writeUser);
        Question save = questionRepository.save(question);
        Question getQuestion = questionRepository.findByIdAndDeletedFalse(save.getId())
                .orElseThrow(() -> new NotFoundException());

        assertAll(
                () -> assertThat(getQuestion).isNotNull(),
                () -> assertThat(getQuestion.getId()).isEqualTo(save.getId()),
                () -> assertThat(getQuestion.getContents()).isEqualTo(save.getContents()),
                () -> assertThat(getQuestion.getWriterId()).isEqualTo(save.getWriterId()),
                () -> assertThat(getQuestion.getCreatedAt()).isEqualTo(save.getCreatedAt()),
                () -> assertThat(getQuestion.getUpdatedAt()).isEqualTo(save.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("질문의 삭제여부가 true로 변경되었는지 테스트한다")
    void IsDeleteChangeTest(){
        User writeUser = userRepository.save(UserTest.createUser("user2"));
        Question question = QuestionTest.createQuestion(writeUser);
        question.setDeleted(true);
        Long id = question.getId();
        Optional<Question> byIdAndDeletedFalse = questionRepository.findByIdAndDeletedFalse(id);

        assertAll(
                () -> assertThat(byIdAndDeletedFalse).isEmpty(),
                () -> assertThat(question.isDeleted()).isTrue()
        );

    }

    @Test
    @DisplayName("질문이 실제 삭제되었는지 테스트한다")
    void deleteByIdTest() {
        User writeUser = userRepository.save(UserTest.createUser("user1"));
        Question question = QuestionTest.createQuestion(writeUser);

        Question save = questionRepository.save(question);

        questionRepository.deleteById(save.getId());
        assertAll(
                () -> assertThat(questionRepository.findById(save.getId())).isEmpty(),
                () -> assertThat(questionRepository.findByIdAndDeletedFalse(save.getId())).isEmpty()
        );
    }

}
