package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.QuestionRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Question 객체를 저장하면 Id가 자동생성 되어 Not Null 이다.")
    void save() {
        assertThat(Q1.getId()).isNotNull();
        assertThat(questionRepository.save(Q1).getId()).isNotNull();
        assertThat(questionRepository.save(Q2).getId()).isNotNull();
    }

    @Test
    @DisplayName("Question 객체를 조회하면 데이터 여부에 따라 Optional 존재 여부가 다르다." +
            "또한 동일한 객체면 담긴 값도 동일하다.")
    void findByWriterId() {
        questionRepository.save(Q1);
        assertThat(questionRepository.findByWriterId(1L).isPresent()).isTrue();
        assertThat(questionRepository.findByWriterId(1L).get().getContents()).isEqualTo(Q1.getContents());
        assertThat(questionRepository.findByWriterId(10L).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Question 객체를 수정하면 수정된 데이터와 일치해야 하고 업데이트 날짜가 Not Null 이다.")
    void update() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Question actual = questionRepository.save(question);
        assertThat(actual.getUpdatedAt()).isNull();

        Long writerId = 5L;
        actual.setWriterId(writerId);

        Question updated = questionRepository.findByWriterId(writerId).get();

        assertThat(updated.getUpdatedAt()).isNotNull();
        assertThat(updated.getWriterId()).isEqualTo(writerId);
    }

}
