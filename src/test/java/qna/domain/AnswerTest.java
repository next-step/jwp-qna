package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {

    @Autowired
    AnswerRepository answerRepository;

    public static final Answer A1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @BeforeEach
    public void init() {
        UserTest.JAVAJIGI.setId(null);
        UserTest.SANJIGI.setId(null);

        QuestionTest.Q1.setId(null);
        QuestionTest.Q2.setId(null);

        A1.setId(null);
        A2.setId(null);
    }

    @Test
    public void saveEntity() {

        //when
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(answerRepository.save(A1));
        answers.add(answerRepository.save(A2));

        //then
        assertThat(answers).extracting("id").isNotNull();
        assertThat(answers).extracting("contents").containsExactly("Answers Contents1", "Answers Contents2");
    }

    @Test
    public void equalTest() {
        //given
        Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

        //when
        Answer a1Saved = answerRepository.save(A1);

        //then
        assertThat(a1Saved.hashCode()).isEqualTo(A1.hashCode());
    }


    @Test
    public void annotingTest() {

        //when
        Answer answerSaved = answerRepository.save(A1);

        //then
        assertThat(answerSaved.getCreatedAt()).isNotNull();
        assertThat(answerSaved.getUpdatedAt()).isNotNull();
    }

    @Test
    public void cascadePersist() {
        assertThat(A1.getWriter().getId()).isNull();
        assertThat(A1.getQuestion().getId()).isNull();

        answerRepository.save(A1);
        assertThat(A1.getWriter().getId()).isNotNull();
        assertThat(A1.getQuestion().getId()).isNotNull();
    }
}
