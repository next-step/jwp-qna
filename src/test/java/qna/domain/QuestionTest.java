package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
public class QuestionTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(
        UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(
        UserTest.SANJIGI);

    @BeforeEach
    public void init() {
        UserTest.JAVAJIGI.setId(null);
        UserTest.SANJIGI.setId(null);

        Q1.setId(null);
        Q2.setId(null);

    }


    @Test
    public void saveEntity() {
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(questionRepository.save(Q1));
        questions.add(questionRepository.save(Q2));

        assertThat(questions).extracting("id").isNotNull();
        assertThat(questions).extracting("title").containsExactly("title1", "title2");
    }

    @Test
    public void equalTest() {
        Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Question q1Saved = questionRepository.save(Q1);
        assertThat(q1Saved.hashCode()).isEqualTo(Q1.hashCode());
    }


    @Test
    public void annotingTest() {
        Question q1Saved = questionRepository.save(Q1);
        assertThat(q1Saved.getCreatedAt()).isNotNull();
        assertThat(q1Saved.getUpdatedAt()).isNotNull();
    }

    @Test
    public void cascadePersist() {
        assertThat(Q1.getWriter().getId()).isNull();
        Question q1Saved = questionRepository.save(Q1);
        assertThat(q1Saved.getWriter().getId()).isNotNull();
    }

    @Test
    public void oneToManyTest() {
        AnswerTest.A1.setId(null); //id가없어야 save를 하고 casecade를통한 save도 이뤄짐
        AnswerTest.A2.setId(null);
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);

        //em.clear(); //현재 Q1은 영속성에 존재하기때문에, answers에 A1과 A2가 없음. 새로가지고와야함
        Optional<Question> q1ById = questionRepository.findByIdAndDeletedIsFalse(Q1.getId());
        assertThat(q1ById.get().getAnswers())
            .extracting("id")
            .containsExactly(AnswerTest.A1.getId(), AnswerTest.A2.getId());
    }

    @Test
    public void deleteQuestionNotMineTest() {
        Question question = new Question("title1", "contents1").writeBy(
            UserTest.SANJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question,
            "Answers Contents1");
        questionRepository.save(question);
        answerRepository.save(answer);
        System.out.println(question);

        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI)).isInstanceOf(
                CannotDeleteException.class)
            .hasMessage("질문을 삭제할 권한이 없습니다.");

    }
}
