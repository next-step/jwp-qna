package qna.domain;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class QuestionTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Question question1;
    private Question question2;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(User.builder().userId("javajigi").password("password").name("name").email("javajigi@slipp.net").build());
        user2 = userRepository.save(User.builder().userId("sanjigi").password("password").name("name").email("sanjigi@slipp.net").build());

        question1 = questionRepository.save(Question.builder().title("title1").contents("contents1").build().writeBy(user1));
        question2 = questionRepository.save(Question.builder().title("title2").contents("contents2").build().writeBy(user2));
    }

    @Test
    @DisplayName("질문 글을 저장한다.")
    void save() {
        //given //when //then
        assertAll(
                () -> assertThat(question1.getId()).isNotNull(),
                () -> assertThat(question1.getTitle()).isEqualTo("title1"),
                () -> assertThat(question1.getContents()).isEqualTo("contents1"),
                () -> assertThat(question1.getWriter()).isEqualTo(user1)
        );
    }

    @Test
    @DisplayName("삭제 되지 않은 질문 목록을 조회한다.")
    void findByDeletedFalse() {
        //given
        question2.setDeleted(true);

        //when
        List<Question> remainQuestions = questionRepository.findByDeletedFalse();

        //then
        assertThat(remainQuestions).hasSize(1);
    }

    @Test
    @DisplayName("삭제 되지 않은 질문 한 건을 조회한다.")
    void findByIdAndDeletedFalse() {
        //given //when
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(question2.getId());

        //then
        AssertionsForClassTypes.assertThat(findQuestion).hasValueSatisfying(question -> assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getTitle()).isEqualTo("title2"),
                () -> assertThat(question.getContents()).isEqualTo("contents2"),
                () -> assertThat(question.getWriter()).isEqualTo(user2)
        ));
    }
}
