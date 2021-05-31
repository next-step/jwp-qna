package qna.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.repository.QuestionRepository;
import qna.domain.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


//TODO : 피드백 후 삭제하기
/* 개별 TEST는 성공하지만, 전체 TEST는 실패하는 이유를 모르겠습니다.. */

@DataJpaTest
public class QuestionTest {
    public static final Question QUESTION_OF_JAVAJIGI = new Question("title1", "contents1")
            .writeBy(UserTest.USER_JAVAJIGI);

    public static final Question QUESTION_OF_SANJIGI = new Question("title2", "contents2")
            .writeBy(UserTest.USER_SANJIGI);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(UserTest.USER_JAVAJIGI);
        user2 = userRepository.save(UserTest.USER_SANJIGI);

        question1 = questionRepository.save(QUESTION_OF_JAVAJIGI);
        question2 = questionRepository.save(QUESTION_OF_SANJIGI);
    }

    @Test
    public void exists() {
        assertAll(
            () -> assertThat(question1.getId()).isNotNull(),
            () -> assertThat(question2.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("동등성 비교")
    public void isEqualTo() {
        assertAll(
            () -> assertThat(question1).isEqualTo(QUESTION_OF_JAVAJIGI),
            () -> assertThat(question2).isEqualTo(QUESTION_OF_SANJIGI)
        );
    }

    @Test
    @DisplayName("작성자 비교")
    public void isWriterEqualTo() {
        assertAll(
            () -> assertThat(question1.getWriter()).isEqualTo(UserTest.USER_JAVAJIGI),
            () -> assertThat(question2.getWriter()).isEqualTo(UserTest.USER_SANJIGI),

            () -> assertThat(question1.getWriter()).isEqualTo(user1),
            () -> assertThat(question2.getWriter()).isEqualTo(user2)
        );
    }

    @Test
    @DisplayName("삭제되지 않은 질문리스트 조회")
    public void findByDeletedFalse() {
        List<Question> deletedQuestions = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(deletedQuestions.size()).isEqualTo(2),
            () -> assertThat(deletedQuestions).contains(question1, question2)
        );
    }

    @Test
    @DisplayName("질문 삭제후 삭제되지 않은 질문리스트 조회")
    public void findByDeletedFalse2() {
        question1.deleted();

        List<Question> deletedQuestions = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(deletedQuestions.size()).isEqualTo(1),
            () -> assertThat(deletedQuestions).doesNotContain(question1),
            () -> assertThat(deletedQuestions).contains(question2)
        );
    }

    @Test
    public void findByIdAndDeletedFalse() {
        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(question1.getId());

        assertAll(
            () -> assertThat(questionOptional).isNotEmpty(),
            () -> assertThat(questionOptional.get()).isEqualTo(question1)
        );
    }

}