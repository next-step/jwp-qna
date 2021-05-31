package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("질문 생성")
    public void createQuestion() {
        Question question = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
        assertThat(question.equals(Q2)).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("질문 저장")
    public void saveQuestion() {
        User user = userRepository.findByUserId("javajigi").orElse(userRepository.save(UserTest.JAVAJIGI));

        String title = "Save Question Title";
        String content = "Save Question Content";
        Question question = new Question(title, content).writeBy(user);
        Question saveQuestion = questionRepository.save(question);

        assertAll(
                () -> assertThat(saveQuestion.getId()).isNotNull()
        );

        Question selectQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId()).orElseThrow(NotFoundException::new);
        assertThat(selectQuestion.equals(saveQuestion)).isTrue();
    }

}
