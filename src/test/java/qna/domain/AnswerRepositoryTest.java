package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    @Autowired
    private EntityManagerFactory factory;

    @Autowired
    private TestEntityManager testEntityManager;

    Question question;
    Answer answer;
    User user;

    @BeforeEach
    public void setUp() throws Exception {
        user = users.save(new User("answerJavajigi", "password", "javajigi", new Email("javajigi@slipp.net")));
        question = questions.save(new Question("title1", "contents1").writeBy(user));
        answer = new Answer(question.getWriter(), question, "Answers Contents1");
    }

    @Test
    @DisplayName("Answer 저장 후 ID not null 체크")
    void save() {
        // given
        // when
        Answer expect = answers.save(answer);

        // then
        assertThat(expect.getId()).isNotNull();
    }

    @Test
    @DisplayName("Answer 저장 후 findById 조회 결과 동일성 체크")
    void identity() {
        // given
        // when
        Answer actual = answers.save(answer);
        Answer expect = answers.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("Question 으로 답변 찾기 -> findByQuestionIdAndDeletedFalse 메소드 검증 ")
    void findByQuestionIdAndDeletedFalse() {
        // given
        // when
        Answer expect = answers.save(answer);
        List<Answer> answerList = answers.findByQuestionAndDeletedFalse(expect.getQuestion());

        // then
        assertAll(
            () -> assertThat(answerList).contains(expect),
            () -> assertThat(expect.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("remove 처리 후 findByQuestionIdAndDeletedFalse 메소드 조회 미포함 체크 ")
    void findByQuestionIdAndDeletedFalse_deleted() {
        // given
        Answer expect = answers.save(answer);
        expect.delete(user);

        // when
        List<Answer> answerList = answers.findByQuestionAndDeletedFalse(expect.getQuestion());

        // then
        assertAll(
            () -> assertThat(answerList.contains(expect)).isFalse(),
            () -> assertThat(expect.isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("질문 변경 적용 확인")
    void toQuestion() {
        // given
        Answer actual = answers.save(answer);
        Question changeQuestion = questions.save(new Question("질문변경할께요", "contents1").writeBy(user));

        //when
        actual.toQuestion(changeQuestion);
        Answer expect = answers.findByIdAndDeletedFalse(actual.getId()).get();

        // then
        assertThat(expect.getQuestion()).isEqualTo(changeQuestion);
    }

    @Test
    @DisplayName("Question,writer(User) lazy 로딩 확인")
    void question_lazy_loading() {
        // given
        PersistenceUnitUtil persistenceUnitUtil = factory.getPersistenceUnitUtil();
        Answer saveAnswer = answers.save(answer);

        // when
        testEntityManager.clear();
        Answer actual = answers.findByIdAndDeletedFalse(saveAnswer.getId()).get();

        // then
        boolean questionExpect = persistenceUnitUtil.isLoaded(actual, "question");
        boolean writerExpect = persistenceUnitUtil.isLoaded(actual, "writer");
        assertAll(
            () -> assertThat(questionExpect).isFalse(),
            () -> assertThat(writerExpect).isFalse()
        );
    }
}
