package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("질문 저장 테스트")
    void saveQuestionTest() {
        Question savedQ1 = questionRepository.save(Q1);
        Question savedQ2 = questionRepository.save(Q2);
        assertAll(
                () -> assertThat(savedQ1.getId()).isNotNull(),
                () -> assertThat(savedQ2.getTitle()).isEqualTo(Q2.getTitle())
        );
    }

    @Test
    @DisplayName("질문 목록 찾기 테스트")
    void findByDeletedFalseTest() {
        Question newQ1 = questionRepository.save(Q1);
        Question newQ2 = questionRepository.save(Q2);
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).contains(newQ1, newQ2);
    }

    @Test
    @DisplayName("질문 ID로 질문 찾기 테스트")
    void findByIdAndDeletedFalseTest() {
        Question savedQ1 = questionRepository.save(Q1);
        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(savedQ1.getId());
        assertThat(questionOptional.isPresent()).isTrue();
    }

    @Test
    @DisplayName("질문ID가 없는 데이터로 찾는 테스트")
    void notFindIdAndDeletedFalseTest(){
        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(3L);
        assertThat(questionOptional.isPresent()).isFalse();
    }
}
