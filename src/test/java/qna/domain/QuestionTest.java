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
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    private Question savedQuestion;

    @BeforeEach
    private void beforeEach(){
        savedQuestion = questionRepository.save(Q1);
    }

    @Test
    @DisplayName("question 등록")
    public void saveQuestionTest(){
        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(Q1.getContents()),
                () -> assertThat(savedQuestion.getWriterId()).isEqualTo(Q1.getWriterId()),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(Q1.getContents()),
                () -> assertFalse(savedQuestion.isDeleted())
        );
    }

    @Test
    @DisplayName("deleted가 false인 question 검색")
    public void findByDeletedFalseTest(){
        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(questions).contains(savedQuestion)
        );
    }

    @Test
    @DisplayName("question id로 deleted가 false인 question 단일검색")
    public void findByIdAndDeletedFalseTest(){
        Optional<Question> oQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertQuestionDeleted(oQuestion, false);

    }

    @Test
    @DisplayName("question에 delete를 true로 수정")
    public void updateQuestionDeletedTrue(){
        savedQuestion.setDeleted(true);

        Optional<Question> oQuestion = questionRepository.findById(savedQuestion.getId());

        assertQuestionDeleted(oQuestion, true);
    }

    private void assertQuestionDeleted(Optional<Question> oAnswer, boolean isDeleted) {
        assertAll(
                () -> assertEquals(oAnswer.get(), savedQuestion),
                () -> assertThat(oAnswer.get().isDeleted()).isEqualTo(isDeleted)
        );
    }
}
