package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.ArrayList;
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
        JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(
        SANJIGI);

    @BeforeEach
    public void init() {
        JAVAJIGI.setId(null);
        SANJIGI.setId(null);

        Q1.setId(null);
        Q2.setId(null);
        Q1.getAnswers().clear();
        A1.setQuestion(null);
        A2.setQuestion(null);
        Q2.getAnswers().clear();

        A1.setId(null);
        A2.setId(null);
        A1.toQuestion(Q1);
        A2.toQuestion(Q1);

        userRepository.save(SANJIGI);
        userRepository.save(JAVAJIGI);
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
        Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
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
        //em.clear(); //현재 Q1은 영속성에 존재하기때문에, answers에 A1과 A2가 없음. 새로가지고와야함
        Optional<Question> q1ById = questionRepository.findByIdAndDeletedIsFalse(Q1.getId());
        assertThat(q1ById.get().getAnswers())
            .extracting("id")
            .contains(A1.getId())
            .contains(A2.getId());
    }

    @Test
    public void deleteQuestionNotMineTest() {
        Question question = new Question("title1", "contents1").writeBy(
            SANJIGI);
        Answer answer = new Answer(JAVAJIGI, question,
            "Answers Contents1");
        questionRepository.save(question);
        answerRepository.save(answer);

        assertThatThrownBy(() -> question.delete(JAVAJIGI)).isInstanceOf(
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
        List<DeleteHistory> deleteHistories = q1Local.delete(JAVAJIGI);

        //then
        assertThat(deleteHistories)
            .extracting("contentType", "contentId")
            .contains(tuple(ContentType.QUESTION, q1Local.getId()))
            .contains(tuple(ContentType.ANSWER, answer1Local.getId()))
            .contains(tuple(ContentType.ANSWER, answer2Local.getId()));
    }

//    @Test
//    @DisplayName("question bulk update 후 false로 전부 적용되어야한다.")
//    public void questionBulkUpdate() {
//        questionRepository.save(Q1);
//        assertThat(Q1.isDeleted()).isFalse();
//        questionRepository.updateDeleteOfQuestions(Arrays.asList(Q1.getId()));
//        Optional<Question> question = questionRepository.findById(Q1.getId());
//        assertThat(question.get().isDeleted()).isTrue();
//    }

//    @Test
//    @DisplayName("answer bulk update 후 false로 전부 적용되어야한다.")
//    public void answerBulkUpdate() {
//        Question question = new Question("title1", "contents1").writeBy(JAVAJIGI);
//        Answer answer1 = new Answer( JAVAJIGI, QuestionTest.Q1,"Answers Contents1");
//        Answer answer2 = new Answer( JAVAJIGI, QuestionTest.Q1,"Answers Contents2");
//        questionRepository.save(question);
//        answerRepository.save(answer1);
//        answerRepository.save(answer2);
//
//        assertThat(answer1.isDeleted()).isFalse();
//        assertThat(answer2.isDeleted()).isFalse();
//
//        assertThat(answerRepository.updateDeleteOfAnswers(Arrays.asList(answer1.getId(),answer2.getId()))).isEqualTo(2);
//        assertThat(answerRepository.findById(answer1.getId()).get().isDeleted()).isTrue();
//    }
}
