package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired QuestionRepository questionRepository;

    @Test
    @DisplayName("저장이 잘 되는지 테스트")
    void save() {
        Question expected = new Question("제목1", "내용1");
        Question actual = questionRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("개체를 저장한 후 다시 가져왔을 때 기존의 개체와 동일한지 테스트")
    void findById() {
        Question question = new Question("제목1", "내용1");
        Question savedQuestion = questionRepository.save(question);

        Question foundQuestion = questionRepository.findById(savedQuestion.getId()).get();
        assertThat(foundQuestion).isEqualTo(question);
    }
}
