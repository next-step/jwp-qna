package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question saveQ1;
    private Question saveQ2;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setup() {
        saveQ1 = questionRepository.save(Q1);
        saveQ2 = questionRepository.save(Q2);
    }

    @DisplayName("질문을 저장한다.")
    @Test
    void save() {
        assertAll(
                () -> assertThat(saveQ1.getId()).isNotNull(),
                () -> assertThat(saveQ1.getTitle()).isEqualTo(saveQ1.getTitle()),
                () -> assertThat(saveQ2.getId()).isNotNull(),
                () -> assertThat(saveQ2.getTitle()).isEqualTo(saveQ2.getTitle())
        );
    }

    @DisplayName("미삭제 질문들을 조회한다.")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        assertThat(questionRepository.findByDeletedFalse().size()).isEqualTo(2);
    }

    @DisplayName("ID로 미삭제 질문을 조회한다.")
    @Test
    void findByIdAndDeletedFalse() {
        assertThat(questionRepository.findByIdAndDeletedFalse(Q1.getId()).get()).isEqualTo(saveQ1);
    }

}
