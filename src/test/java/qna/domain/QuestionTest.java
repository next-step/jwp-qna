package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(
        UserTest.SANJIGI);

    @BeforeEach
    public void init() {
        UserTest.JAVAJIGI.setId(null);
        UserTest.SANJIGI.setId(null);

        Q1.setId(null);
        Q2.setId(null);

        A1.setId(null);
        A2.setId(null);

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
        A1.setId(null); //id가없어야 save를 하고 casecade를통한 save도 이뤄짐
        A2.setId(null);
        answerRepository.save(A1);
        answerRepository.save(A2);

        //em.clear(); //현재 Q1은 영속성에 존재하기때문에, answers에 A1과 A2가 없음. 새로가지고와야함
        Optional<Question> q1ById = questionRepository.findByIdAndDeletedIsFalse(Q1.getId());
        assertThat(q1ById.get().getAnswers())
            .extracting("id")
            .containsExactly(A1.getId(), A2.getId());
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

        assertThatThrownBy(() -> question.getDeleteIds(UserTest.JAVAJIGI)).isInstanceOf(
                CannotDeleteException.class)
            .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("answer중 자신의 것이 아닌게 있으면 throw")
    public void deleteQuestionCheckAnswersIsMineTest() {
        questionRepository.save(Q1); //자바지기 Question
        answerRepository.save(A1); //자바지기 Question

        answerRepository.save(A2); //산지기 Question
        System.out.println("ee");
        assertThatThrownBy(() -> Q1.getDeleteIds(UserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");


    }

    @Test
    @DisplayName("삭제시 question은 해당 아이디만, answer은 연관된 모든 answer id가 반환된다")
    public void deleteQuestionGetTargetId() throws CannotDeleteException {
        questionRepository.save(Q1); //자바지기 Question
        answerRepository.save(A1); //자바지기 Question
        A2.setWriter(UserTest.JAVAJIGI);
        answerRepository.save(A2); //자바지기 Question
        HashMap<ContentType, List> deleteTargetIds = Q1.delete(UserTest.JAVAJIGI);

        assertThat(deleteTargetIds.get(ContentType.QUESTION)).contains(Q1.getId());
        assertThat(deleteTargetIds.get(ContentType.ANSWER))
            .contains(A1.getId())
            .contains(A2.getId());
    }

    @Test
    @DisplayName("question bulk update 후 false로 전부 적용되어야한다.")
    public void questionBulkUpdate() {
        questionRepository.save(Q1);
        assertThat(Q1.isDeleted()).isFalse();
        questionRepository.updateDeleteOfQuestions(Arrays.asList(Q1.getId()));
        Optional<Question> question = questionRepository.findById(Q1.getId());
        assertThat(question.get().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("answer bulk update 후 false로 전부 적용되어야한다.")
    public void answerBulkUpdate() {
        questionRepository.save(Q1);
        answerRepository.save(A1);
        answerRepository.save(A2);

        assertThat(A1.isDeleted()).isFalse();
        assertThat(A2.isDeleted()).isFalse();

        assertThat(answerRepository.updateDeleteOfAnswers(
            Arrays.asList(A1.getId(), A2.getId()))).isEqualTo(2);
        assertThat(answerRepository.findById(A1.getId()).get().isDeleted()).isTrue();
        assertThat(answerRepository.findById(A2.getId()).get().isDeleted()).isTrue();
    }
}
