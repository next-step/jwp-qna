package qna.domain.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.question.QuestionTest.Q1;
import static qna.domain.question.QuestionTest.Q2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.NotFoundException;
import qna.domain.user.User;
import qna.domain.user.UserRepository;
import qna.domain.user.UserTest;


@DirtiesContext
@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("질문이 등록되있는지 테스트 한다")
    void saveQuestionTest(){
        User writeUser = userRepository.save(new User("userId", "password", "name", "email"));
        Question question = new Question(writeUser.getId(), "title1", "contents1").writeBy(writeUser);

        Question save = questionRepository.save(question);

        assertAll(
                () -> assertThat(save.getId()).isNotNull(),
                () -> assertThat(save.isOwner(writeUser)).isTrue(),
                () -> assertThat(save.getId()).isEqualTo(question.getId()),
                () -> assertThat(save.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(save.getWriter()).isEqualTo(question.getWriter()),
                () -> assertThat(question.getCreatedAt()).isEqualTo(Q1.getCreatedAt()),
                () -> assertThat(question.getUpdatedAt()).isEqualTo(Q1.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("삭제되지 않은 질문 목록 조회를 테스트한다")
    void findByDeletedFalseTest(){
        User writeUser1 = userRepository.save(new User("userId", "password", "name", "email"));
        Question question1 = new Question( "title1", "contents1").writeBy(writeUser1);
        User writeUser2 = userRepository.save(new User("userId2", "password2", "name2", "email2"));
        Question question2 = new Question( "title1", "contents1").writeBy(writeUser2);
        questionRepository.save(question1);
        questionRepository.save(question2);
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("id로 삭제되지 않은 질문 한 건 조회를 테스트한다")
    void findByIdAndDeletedFalse(){
        User writeUser1 = userRepository.save(new User("userId", "password", "name", "email"));
        Question question1 = new Question( "title1", "contents1").writeBy(writeUser1);
        Question find = questionRepository.findByIdAndDeletedFalse(question1.getId())
                .orElseThrow(() -> new NotFoundException());
        assertAll(
                () -> assertThat(find.getId()).isNotNull(),
                () -> assertThat(find.isOwner(writeUser1)).isTrue(), // TODO:: exception
                () -> assertThat(find.getId()).isEqualTo(question1.getId()),
                () -> assertThat(find.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(find.getWriter()).isEqualTo(question1.getWriter()),
                () -> assertThat(find.getCreatedAt()).isEqualTo(question1.getCreatedAt()), // TODO:: exception
                () -> assertThat(find.getUpdatedAt()).isEqualTo(question1.getUpdatedAt()) // TODO:: exception
        );
    }

    @Test
    @DisplayName("질문의 삭제여부가 true로 변경되었는지 테스트한다")
    void isDeleteChangeTest(){
        Question question = questionRepository.save(Q1);
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
    void deleteByIdTest(){
        Question question = questionRepository.save(Q1);
        questionRepository.deleteById(question.getId());
        assertAll(
                () -> assertThat(questionRepository.findById(question.getId())).isEmpty(),
                () -> assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isEmpty()
        );
    }

    @Test
    @DisplayName("질문의 작성자를 확인한다")
    void getQuestionByWriterId(){
        assertThat(Q1.getWriter().getId()).isEqualTo(UserTest.JAVAJIGI.getId());
    }

}
