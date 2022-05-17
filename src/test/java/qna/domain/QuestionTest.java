package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 질문_등록() {
        Question question = new Question("제목", "내용").writeBy(UserTest.JAVAJIGI);
        Question saved = questionRepository.save(question);
        assertThat(saved).isEqualTo(question);
    }

    @Test
    void 질문_조회() {
        User saved = userRepository.save(UserTest.JAVAJIGI);
        Question savedQuestion = new Question("제목", "내용").writeBy(UserTest.JAVAJIGI);
        questionRepository.save(savedQuestion);

        Question foundQuestion = questionRepository.findById(savedQuestion.getId()).get();
        assertThat(foundQuestion).isEqualTo(savedQuestion);

        User foundUser = userRepository.findById(foundQuestion.getWriterId()).get();
        assertThat(foundUser.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId());
    }

    @Test
    void 질문_수정() {
        Question question = new Question("제목", "내용").writeBy(UserTest.JAVAJIGI);
        questionRepository.save(question);
        questionRepository.flush();

        String expectedTitle = "제목 수정";
        String expectedContents = "내용 수정";

        question.setTitle(expectedTitle);
        question.setContents(expectedContents);
        questionRepository.save(question);
        questionRepository.flush();

        Question found = questionRepository.findById(question.getId()).get();
        assertThat(found.getTitle()).isEqualTo(expectedTitle);
        assertThat(found.getContents()).isEqualTo(expectedContents);
    }

    @Test
    void 질문_삭제() {
        Question question = new Question("제목", "내용").writeBy(UserTest.JAVAJIGI);
        Question saved = questionRepository.save(question);
        assertThat(saved.isDeleted()).isFalse();
        questionRepository.flush();

        questionRepository.delete(saved);
        questionRepository.flush();

        Question found = questionRepository.findById(saved.getId()).get();
        assertThat(found.isDeleted()).isTrue();
    }
}
