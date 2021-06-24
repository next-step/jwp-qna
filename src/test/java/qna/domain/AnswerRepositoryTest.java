package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;
    @Autowired
    private UserRepository users;
    @Autowired
    private QuestionRepository questions;

    private User writer1;
    private User writer2;
    private Question savedQuestion1;
    private Question savedQuestion2;

    @BeforeEach
    void setUp() {
        writer1 = users.save(UserTest.JAVAJIGI);
        Question question1 = QuestionTest.Q1;
        question1.writeBy(writer1);
        savedQuestion1 = questions.save(question1);

        writer2 = users.save(UserTest.SANJIGI);
        Question question2 = QuestionTest.Q2;
        question2.writeBy(writer2);
        savedQuestion2 = questions.save(question2);
    }

    @AfterEach
    void tearDown() {
        answers.deleteAll();
        users.deleteAll();
        questions.deleteAll();
    }

    @Test
    void saveTests(){
        Answer expected = new Answer(1L, writer1, savedQuestion1, "content1");
        Answer actual = answers.save(expected);
        Long t = actual.getId();
        String t2 = actual.getContents();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void findByIdAndDeletedFalseTest(){

        //Given
        Answer answer1 = new Answer(1L, writer1, savedQuestion1, "content1");
        Answer answer2 = new Answer(2L, writer2, savedQuestion2, "content2");

        //When
        answer2.delete();
        Answer expected = answers.save(answer1);
        Answer expectedDeleted = answers.save(answer2);

        Long one = expected.getId();
        Long two = expectedDeleted.getId();
        Optional<Answer> actual = answers.findByIdAndDeletedFalse(one);
        Optional<Answer> actualDeleted = answers.findByIdAndDeletedFalse(two);

        //Then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actualDeleted).isEmpty()
        );
    }
}