package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@EnableJpaAuditing
public class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

    private User user;
    private Question question;
    private Question deletedQuestion;

    @BeforeEach
    void setup() {
        user = new User("id1234", "password", "name", "mail@email.com");
        question = new Question("질문", "질문 내용").writeBy(user);
        deletedQuestion = new Question("질문2", "질문 내용2").writeBy(user);
        deletedQuestion.delete();
    }

    @DisplayName("질문을 저장한다.")
    @Test
    void save() {
        // when
        Question savedQuestion = questionRepository.save(question);

        // given
        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getTitle()).isNotNull(),
                () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @DisplayName("회원의 삭제되지 않은 질문 리스트를 조회한다.")
    @Test
    void findByWriter() {
        // given
        questionRepository.save(question);
        questionRepository.save(deletedQuestion);

        // when
        List<Question> questions = questionRepository.findByWriterAndDeletedFalse(question.getWriter());

        // then
        Assertions.assertThat(questions).containsExactly(question);
    }

}
