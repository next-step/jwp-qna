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
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void cleanUp() {
       answerRepository.deleteAll();
       questionRepository.deleteAll();
       userRepository.deleteAll();
    }

    @DisplayName("answer 저장 테스트")
    @Test
    void answerSaveTest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Question question = new Question("title", "content");
        User writer = new User("id", "password", " name", "email");
        questionRepository.save(question);
        userRepository.save(writer);
        Answer savedAnswer = answerRepository.save(new Answer(1L, writer, question, "content"));

        //when
        List<Answer> answers = answerRepository.findAll();

        //then
        Answer answer = answers.get(0);
        assertAll(() -> {
            assertThat(answer.getId(), is(notNullValue()));
            assertThat(answer.getWriter(), is(savedAnswer.getWriter()));
            assertThat(answer.getQuestion(), is(savedAnswer.getQuestion()));
            assertThat(answer.getContents(), is(savedAnswer.getContents()));
            assertThat(answer.isDeleted(), is(savedAnswer.isDeleted()));
            assertTrue(answer.getCreatedDate().isAfter(now));
            assertTrue(answer.getModifiedDate().isAfter(now));
        });
    }

}
