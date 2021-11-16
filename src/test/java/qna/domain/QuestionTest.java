package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        User sanjigi = userRepository.save(UserTest.SANJIGI);

        Q1.writeBy(javajigi);
        Q2.writeBy(sanjigi);

        questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    @AfterEach
    void clean() {
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("주어진 ID에 해당하는 질문을 리턴한다.")
    void 주어진_ID에_해당하는_질문을_리턴한다() {
        // given
        Question question = questionRepository.findAll().get(0);

        // when
        Question result = questionRepository.findById(question.getId()).get();

        // then
        assertThat(result).isEqualTo(question);
    }

    @Test
    @DisplayName("질문 내용을 수정한다.")
    void 질문_내용을_수정한다() {
        // given
        Question question = questionRepository.findAll().get(0);
        String newContents = "Update Contents";

        // when
        question.setContents(newContents);

        // then
        List<Question> result = questionRepository.findByContents(newContents);
        assertThat(result).containsExactly(question);
    }

    @Test
    @DisplayName("모든 질문을 삭제한다.")
    void 모든_질문을_삭제한다() {
        // given
        List<Question> prevResult = questionRepository.findAll();
        assertThat(prevResult.size()).isGreaterThan(0);

        // when
        questionRepository.deleteAll();

        // then
        List<Question> result = questionRepository.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("삭제되지 않은 질문 목록을 리턴한다.")
    void 삭제되지_않은_질문_목록을_리턴한다() {
        // when
        List<Question> result = questionRepository.findByDeletedFalse();

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("주어진 ID에 해당하는 삭제되지 않은 질문을 리턴한다.")
    void 주어진_ID에_해당하는_삭제되지_않은_질문을_리턴한다() {
        // given
        Question question = questionRepository.findAll().get(0);

        // when
        Question result = questionRepository.findByIdAndDeletedFalse(question.getId()).get();

        // then
        assertThat(result).isEqualTo(question);
    }
}
