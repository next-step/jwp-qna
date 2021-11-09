package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void cleanUp() {
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("question 저장 테스트")
    @Test
    void questionSaveTest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        User writer = new User("id", "password", " name", "email");
        userRepository.save(writer);
        Question savedQuestion = questionRepository.save(new Question("title", "contents", writer, false));

        //when
        List<Question> questions = questionRepository.findAll();

        //then
        Question question = questions.get(0);
        assertAll(() -> {
            assertThat(question.getId(), is(notNullValue()));
            assertThat(question.getTitle(), is(savedQuestion.getTitle()));
            assertThat(question.getContents(), is(savedQuestion.getContents()));
            assertThat(question.getWriter(), is(savedQuestion.getWriter()));
            assertThat(question.isDeleted(), is(savedQuestion.isDeleted()));
            assertTrue(question.getCreatedDate().isAfter(now));
            assertTrue(question.getModifiedDate().isAfter(now));
        });
    }

}
