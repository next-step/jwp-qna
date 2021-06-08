package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    private User savedUser;
    private Question savedQuestion;
    private Answer savedAnswer;

    @BeforeEach
    void setUp() {
        savedUser = users.save(new User("fdevjc", "password", "yang", "email@email.com"));
        savedQuestion = questions.save(new Question(new Title("title1"), "content1").writeBy(savedUser));
        savedAnswer = answers.save(new Answer(savedUser, savedQuestion, "contents"));
    }

    @Test
    @DisplayName("질문을 통해 deleted가 false인 답변을 조회한다.")
    void findByQuestionAndDeletedFalse() {
        //when
        Answer expected = answers.findByQuestionAndDeletedFalse(savedQuestion).get(0);

        //then
        assertAll(
                () -> assertThat(expected.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("답변_id를 통해 deleted가 false인 답변을 조회한다.")
    void findByIdAndDeletedFalse() {
        //when
        Answer expected = answers.findByIdAndDeletedFalse(savedAnswer.getId()).get();

        //then
        assertAll(
                () -> assertThat(expected).isNotNull(),
                () -> assertThat(expected.isDeleted()).isFalse()
        );
    }
}