package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    private Question question;

    @BeforeEach
    public void setUp() throws Exception {
        User user = users.save(new User("answerJavajigi", "password", "javajigi", "javajigi@slipp.net"));
        question = new Question("title1", "contents1").writeBy(user);
    }

    @Test
    @DisplayName("QuestionRepository 저장 후 ID not null 체크")
    void save() {
        // when
        Question actual = questions.save(question);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("QuestionRepository 저장 후 DB조회 객체 동일성 체크")
    void identity() {
        // when
        Question actual = questions.save(question);
        Question expect = questions.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("삭제안된 Question 조회 검증, 조회결과없음")
    void findByIdAndDeletedFalse_deleted_true() {
        // given
        // when
        Question actual = questions.save(question);
        actual.remove();
        Optional<Question> expect = questions.findByIdAndDeletedFalse(question.getId());

        // then
        assertThat(expect.isPresent()).isFalse();
    }

    @Test
    @DisplayName("getAnswers 메소드를 통해 답변 목록 확인")
    void getAnswers() {
        // given
        Answer answer = new Answer(question.getWriter(), question, "Answers Contents1");

        // when
        question.addAnswer(answer);
        List<Answer> answers = question.getAnswers();

        // then
        assertThat(answers).contains(answer);
    }

    @Test
    @DisplayName("remove 로 삭제 시 연관 answer 도 deleted 확인")
    void remove() {
        // given
        Answer answer1 = new Answer(question.getWriter(), question, "Answers Contents1");
        Answer answer2 = new Answer(question.getWriter(), question, "Answers Contents1");
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        questions.save(question);
        questions.flush();
        Long cacheQuestionId = question.getId();

        // when
        question.remove();
        questions.flush();
        Question deletedQuestion = questions.findById(cacheQuestionId).get();
        List<Answer> deletedAnswers = deletedQuestion.getAnswers();

        // then
        assertThat(deletedAnswers.stream()
            .filter(answer -> !answer.isDeleted())
            .count()).isEqualTo(0L);
    }

}
