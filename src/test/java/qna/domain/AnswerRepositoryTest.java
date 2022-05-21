package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void set() {
        answer1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "안녕");
        answer2 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "안녕2");
        answers.saveAll(Arrays.asList(answer1, answer2));
    }

    @Test
    @DisplayName("댓글내용으로 조회")
    void findByContents() {
        assertThat(answers.findByContents("안녕")).isEqualTo(answer1);
    }

    @Test
    @DisplayName("작성자 key로 답글 조회")
    void findByWriterId() {
        assertThat(answers.findByWriterId(UserTest.JAVAJIGI.getId())).isEqualTo(answer1);
        assertThat(answers.findByWriterId(UserTest.SANJIGI.getId())).isEqualTo(answer2);
    }

    @Test
    void save() {
        Answer answer = new Answer(UserTest.IU, QuestionTest.Q1, "안녕");
        Answer save = answers.save(answer);
        assertThat(save).isNotNull();
        System.out.println(answers.findByWriterId(UserTest.IU.getId()));
    }
}
