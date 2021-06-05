package qna.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.service.QnaService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;


    private User user1;
    private User user2;

    private Question question1;
    private Question question2;

    @BeforeEach
    void setup() {
        user1 = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        user2 = userRepository.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));

        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        question2 = questionRepository.save(new Question("title2", "contents1").writeBy(user2));
    }

    @Test
    @DisplayName("Question 저장 확인")
    void saveTest() {
        Question result = questionRepository.save(new Question("title1", "contents1").writeBy(user1));

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getWriter().getId()).isEqualTo(user1.getId()),
                () -> assertThat(result.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(question1.getTitle())
        );
    }

    @Test
    @DisplayName("deleted 값이 false인 Question 조회")
    void findByDeletedFalseTest() {
        List<Question> resultList = questionRepository.findByDeletedFalse();

        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList).contains(question1, question2);
    }

    @Test
    @DisplayName("id로 deleted값이 false인 Question 조회")
    void findByIdAndDeletedFalseTest() {
        Question result = questionRepository.findByIdAndDeletedFalse(question1.getId()).get();

        assertAll(
                () -> assertThat(result.isDeleted()).isFalse(),
                () -> assertThat(result.getContents()).isEqualTo(question1.getContents()),
                () -> assertThat(result.getTitle()).isEqualTo(question1.getTitle()),
                () -> assertThat(result.getWriter()).isEqualTo(question1.getWriter())
        );
    }

    @Test
    @DisplayName("질문 작성자가 아닌 사용자는 질문을 지울 수 없다.")
    void deleteFailTest() {
        assertThatThrownBy(() -> question1.deletedByUser(user2)).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

}
