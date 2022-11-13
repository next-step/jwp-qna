package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswersTest {
    public Answer answer;
    @BeforeEach
    void setUp() {
        answer = TestFixture.createAnswer();
    }

    @Test
    @DisplayName("답변목록 추가")
    void answer_Add() {
        //given
        Answers answers = new Answers(new ArrayList<>());
        //when
        answers.addAnswer(answer);
        //then
        assertThat(answers.contains(answer)).isTrue();
    }

    @Test
    @DisplayName("답변목록 삭제")
    void answer_Remove() {
        //given
        Answers answers = new Answers(new ArrayList<>(Collections.singletonList(answer)));
        //when
        answers.removeAnswer(answer);
        //then
        assertThat(answers.contains(answer)).isFalse();
    }

}
