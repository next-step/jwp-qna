package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    UserRepository users;
    @Autowired
    QuestionRepository questions;
    @Autowired
    AnswerRepository answers;

    @DisplayName("A1 Answer 정보 저장 및 데이터 확인")
    @Test
    void saveAnswer() {
        User user = users.save(TestCreateFactory.createUser(1L));
        Question question = questions.save(TestCreateFactory.createQuestion(user));
        Answer answer = answers.save(TestCreateFactory.createAnswer(user, question));

        Long answerId = answer.getId();

        assertThat(answerId).isNotNull();
    }

    @DisplayName("writer_id로 데이터 조회")
    @Test
    void findByWriterId() {
        User writer = users.save(TestCreateFactory.createUser(1L));
        Question question = questions.save(TestCreateFactory.createQuestion(writer));
        Answer standard = answers.save(TestCreateFactory.createAnswer(writer, question));
        Answer searched = answers.findByWriterId(writer.getId());

        Long standardId = standard.getId();
        Long searchedId = searched.getId();

        assertThat(standardId).isEqualTo(searchedId);
    }

    @DisplayName("Question id 로 데이터 조회")
    @Test
    void findByQuestionId() {
        User user = users.save(TestCreateFactory.createUser(1L));
        Question question = questions.save(TestCreateFactory.createQuestion(user));
        Answer standard = answers.save(TestCreateFactory.createAnswer(user, question));

        List<Answer> searched = answers.findByQuestionId(question.getId());

        assertThat(searched).contains(standard);
    }

    @DisplayName("Id 와 Deleted 값이 fasle 인 값 찾기")
    @Test
    void findByIdAndDeletedFalse() {
        User user = users.save(TestCreateFactory.createUser(1L));
        Question question = questions.save(TestCreateFactory.createQuestion(user));
        Answer standard = answers.save(TestCreateFactory.createAnswer(user, question));
        Answer searched = answers.findByIdAndDeletedFalse(standard.getId()).get();

        boolean searchedDeleted = searched.isDeleted();

        assertThat(searchedDeleted).isFalse();
    }
}
