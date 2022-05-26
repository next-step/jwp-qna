package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(
        SANJIGI);

    @BeforeEach
    public void init() {
        InitUtils.init();
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
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
    public void oneToManyTest() {
        questionRepository.save(Q1);

        Optional<Question> q1ById = questionRepository.findByIdAndDeletedIsFalse(Q1.getId());
        assertThat(q1ById.get().getAnswers())
            .extracting("id")
            .contains(A1.getId())
            .contains(A2.getId());
    }

    @Test
    public void deleteQuestionNotMineTest() {
        questionRepository.save(Q1);

        assertThatThrownBy(() -> Q1.delete(SANJIGI)).isInstanceOf(
                CannotDeleteException.class)
            .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("answer중 자신의 것이 아닌게 있으면 throw")
    public void deleteQuestionCheckAnswersIsMineTest() {
        questionRepository.save(Q1); //자바지기 Question

        assertThatThrownBy(() -> Q1.delete(JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");


    }

    @Test
    @DisplayName("삭제시 question은 해당 아이디만, answer은 연관된 모든 answer id가 반환된다")
    public void deleteQuestionGetTargetId() throws CannotDeleteException {
        //given
        Question q1Local = new Question("title1", "contents1").writeBy(
            JAVAJIGI);
        Answer answer1Local = new Answer(UserTest.JAVAJIGI, q1Local,
            "Answers Contents1");
        Answer answer2Local = new Answer(UserTest.JAVAJIGI, q1Local,
            "Answers Contents2");

        //when
        questionRepository.save(q1Local); //자바지기 Question
        Map<ContentType, List<Long>> deleteTargetIds = q1Local.delete(JAVAJIGI);

        //then
        assertThat(deleteTargetIds)
            .contains(entry(ContentType.QUESTION, Arrays.asList(q1Local.getId())))
            .contains(entry(ContentType.ANSWER,
                Arrays.asList(answer1Local.getId(), answer2Local.getId())));
    }

    @Test
    public void getAnswersAfterDelete () throws CannotDeleteException {
        //given
        Question q1Local = new Question("title1", "contents1").writeBy(
            JAVAJIGI);
        Answer answer1Local = new Answer(UserTest.JAVAJIGI, q1Local,
            "Answers Contents1");
        Answer answer2Local = new Answer(UserTest.JAVAJIGI, q1Local,
            "Answers Contents2");

        //when
        questionRepository.save(q1Local);
        q1Local.delete(UserTest.JAVAJIGI);
        //then

        assertThat(q1Local.getAnswersIsNotDelete()).hasSize(0);
        assertThat(q1Local.getAnswers()).hasSize(2);
    }
}
