package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = Question.of("title1", "contents1");
    public static final Question Q2 = Question.of("title2", "contents2");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("save 테스트")
    @Test
    void save() {
        // given
        User newUser = userRepository.save(JAVAJIGI);

        // when
        Question newQuestion = questionRepository.save(Q1.writeBy(newUser));

        // then
        assertAll(
                () -> assertThat(newQuestion.getId()).isNotNull()
                , () -> assertThat(newQuestion.getTitle()).isEqualTo(Q1.getTitle())
                , () -> assertThat(newQuestion.getContents()).isEqualTo(Q1.getContents())
                , () -> assertThat(newQuestion.getWriter().getId()).isNotNull()
        );
    }

    @DisplayName("findByDeletedFalse 테스트")
    @Test
    void findByDeletedFalse() {
        // given
        User newUser = userRepository.save(JAVAJIGI);
        Question newQuestion = questionRepository.save(Q1.writeBy(newUser));

        // when
        List<Question> questions = questionRepository.findByDeletedFalse();

        // then
        assertAll(
                () -> assertThat(questions).hasSize(1)
                , () -> assertThat(questions.get(0).getId()).isNotNull()
                , () -> assertThat(questions).containsExactly(newQuestion)
        );
    }

    @DisplayName("findByIdAndDeletedFalse 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        User newUser = userRepository.save(JAVAJIGI);
        Question newQuestion = questionRepository.save(Q1.writeBy(newUser));

        // when
        Question findQuestion = questionRepository.findByIdAndDeletedFalse(newQuestion.getWriter().getId()).get();

        // then
        assertAll(
                () -> assertThat(findQuestion.getId()).isNotNull()
                , () -> assertThat(findQuestion.getWriter().getId()).isNotNull()
                , () -> assertThat(findQuestion).isEqualTo(newQuestion)
        );
    }
}
