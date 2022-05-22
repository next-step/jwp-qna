package qna.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void initialize() {
        user = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("Question을 DeletedFalse로 조회")
    void Question_조회_byDeletedFalse(){
        generateQuestionDeletedTrue();
        Question questionDeletedFalse = questionRepository
                .save(new Question("title1", "contents1").writeBy(user));
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).containsExactly(questionDeletedFalse);
    }

    @Test
    @DisplayName("Question을 Id, DeletedFalse로 조회")
    void Question_조회_byId_DeletedFalse(){
        Question questionDeletedTrue = generateQuestionDeletedTrue();
        Question questionDeletedFalse = questionRepository
                .save(new Question("title1", "contents1").writeBy(user));
        Assertions.assertAll(
                () -> assertThat(questionRepository.findByIdAndDeletedFalse(questionDeletedTrue.getId()))
                        .isEmpty(),
                () -> assertThat(questionRepository.findByIdAndDeletedFalse(questionDeletedFalse.getId())).get()
                        .isEqualTo(questionDeletedFalse)
        );
    }

    private Question generateQuestionDeletedTrue() {
        Question question = new Question("title1", "contents1").writeBy(user);
        question.setDeleted(true);
        return questionRepository.save(question);
    }
}
