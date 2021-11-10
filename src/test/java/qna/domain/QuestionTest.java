package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @BeforeAll
    private void beforeEach() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    @DisplayName("삭제되지 않은 질문을 조회한다.")
    @Test
    public void find_notDelete() {
        // given

        // when
        List<Question> realQuestions = questionRepository.findByDeletedFalse();

        // then
        Assertions.assertThat(realQuestions.size()).isEqualTo(2);

        assertAll(
            () -> Assertions.assertThat(realQuestions.get(0).getId()).isEqualTo(Q1.getId()),
            () -> Assertions.assertThat(realQuestions.get(0).getTitle()).isEqualTo(Q1.getTitle()),
            () -> Assertions.assertThat(realQuestions.get(0).getContents()).isEqualTo(Q1.getContents()),
            () -> Assertions.assertThat(realQuestions.get(0).getWriterId()).isEqualTo(Q1.getWriterId())
        );

        assertAll(
            () -> Assertions.assertThat(realQuestions.get(1).getId()).isEqualTo(Q2.getId()),
            () -> Assertions.assertThat(realQuestions.get(1).getTitle()).isEqualTo(Q2.getTitle()),
            () -> Assertions.assertThat(realQuestions.get(1).getContents()).isEqualTo(Q2.getContents()),
            () -> Assertions.assertThat(realQuestions.get(1).getWriterId()).isEqualTo(Q2.getWriterId())
        );
    }

    @DisplayName("특정 유저의 질문을 조회한다.")
    @Test
    public void find_forWriter() {
        // given

        // when
        List<Question> realQuestions = questionRepository.findByWriterId(UserTest.SANJIGI.getId());

        // then
        Assertions.assertThat(realQuestions.size()).isEqualTo(1);
        
        assertAll(
            () -> Assertions.assertThat(realQuestions.get(0).getTitle()).isEqualTo(Q2.getTitle()),
            () -> Assertions.assertThat(realQuestions.get(0).getContents()).isEqualTo(Q2.getContents()),
            () -> Assertions.assertThat(realQuestions.get(0).getWriterId()).isEqualTo(Q2.getWriterId())
        );
    }
}
