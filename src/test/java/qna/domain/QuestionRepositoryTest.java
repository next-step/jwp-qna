package qna.domain;

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

    private Question questionResult1;
    private Question questionResult2;

    @BeforeEach
    void setUp() {
        questionResult1 = questions.save(QuestionTest.Q1);
        questionResult2 = questions.save(QuestionTest.Q2);
    }

    @DisplayName("삭제 기록이 잘 저장되는지 체크")
    @Test
    void saveTest(){
        assertThat(questionResult1.getId()).isNotNull();
    }

    @DisplayName("삭제한 질문은 리스트에 포함되지않는지 체크")
    @Test
    void findByDeletedFalseTest(){
        List<Question> actual = questions.findByDeletedFalse();
        assertThat(actual).contains(questionResult1);
        assertThat(actual).doesNotContain(questionResult2);
    }

    @DisplayName("삭제한 질문은 조회되지 않는지 체크")
    @Test
    void findByIdAndDeletedFalseTest(){

        Optional<Question> actual1 = questions.findByIdAndDeletedFalse(questionResult1.getId());
        assertThat(actual1).isPresent();

        Optional<Question> actual2 = questions.findByIdAndDeletedFalse(questionResult2.getId());
        assertThat(actual2).isEmpty();

    }
}