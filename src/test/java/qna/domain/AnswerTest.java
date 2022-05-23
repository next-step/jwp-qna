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

    @Autowired
    QuestionRepository questionRepository;
    public static final Answer A1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1,
        "Answers Contents1");
    public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1,
        "Answers Contents2");

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
        assertThat(answers).extracting("contents")
            .containsExactly("Answers Contents1", "Answers Contents2");
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

    @Test
    public void testQuestionOneToMany() {
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);
        assertThat(QuestionTest.Q1.getAnswers())
            .hasSize(2)
            .contains(A1, A2)
            .as("static 생성자에 의해 Q1에 2개의 Answer가 매핑되어있다");
        Answer answer1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1,
            "Answers Contents1");
        assertThat(QuestionTest.Q1.getAnswers())
            .hasSize(3)
            .containsExactly(A1, A2, answer1)
            .as("answer 연결 후 answers에 answer1이 담겨있어야한다.");

        answer1.toQuestion(QuestionTest.Q2);
        assertThat(QuestionTest.Q1.getAnswers())
            .hasSize(2)
            .containsExactly(A1, A2)
            .as("answer에 대한 question 변경시 기존 question엔 남아있지 않아야한다.");

        assertThat(QuestionTest.Q2.getAnswers())
            .hasSize(1)
            .containsExactly(answer1)
            .as("answer 연결 후 answers에 answer1이 담겨있어야한다.");
    }
}
