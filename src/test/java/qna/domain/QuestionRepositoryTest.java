package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

//    private Question questionResult1;
//    private Question questionResult2;

    @BeforeEach
    void setUp() {
//        questionResult1 = questions.save(QuestionTest.Q1);
//        questionResult2 = questions.save(QuestionTest.Q2);
    }

    @AfterEach
    void tearDown() {
        questions.deleteAll();
    }

    @DisplayName("삭제 기록이 잘 저장되는지 체크")
    @Test
    void saveTest(){
        Question actual = questions.save(QuestionTest.Q1);
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("삭제한 질문은 리스트에 포함되지않는지 체크")
    @Test
    void findByDeletedFalseTest(){
        Question notDeletedQuestion = questions.save(QuestionTest.Q1);
        Question deletedQuestion = questions.save(QuestionTest.Q2);
        List<Question> actual = questions.findByDeletedFalse();
        assertThat(actual).contains(notDeletedQuestion);
        assertThat(actual).doesNotContain(deletedQuestion);
    }

    @DisplayName("삭제한 질문은 조회되지 않는지 체크")
    @Test
    void findByIdAndDeletedFalseTest(){
        Question notDeletedQuestion = questions.save(QuestionTest.Q1);
        Question deletedQuestion = questions.save(QuestionTest.Q2);
        Optional<Question> actual1 = questions.findByIdAndDeletedFalse(notDeletedQuestion.getId());
        assertThat(actual1).isPresent();
        Optional<Question> actual2 = questions.findByIdAndDeletedFalse(deletedQuestion.getId());
        assertThat(actual2).isEmpty();

    }
}