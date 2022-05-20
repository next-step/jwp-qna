package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

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
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void 질문_조회() {
        userRepository.save(UserTest.JAVAJIGI);
        Question actualQuestion = questionRepository.save(new Question("제목", "내용").writeBy(UserTest.JAVAJIGI));

        Question expected = questionRepository.findById(actualQuestion.getId()).get();
        assertThat(expected).isNotNull();
        assertThat(expected.getTitle()).isEqualTo("제목");
        assertThat(expected.getContents()).isEqualTo("내용");

        User actual = userRepository.findById(expected.getWriterId()).get();
        assertThat(actual).isEqualTo(UserTest.JAVAJIGI);
    }

    @Test
    void 질문_수정() {
        Question question1 = questionRepository.save(new Question("제목", "내용").writeBy(UserTest.SANJIGI));
        question1.updateTitle("수정 제목");

        Question question2 = questionRepository.findByTitle("수정 제목").get(0);
        assertThat(question2).isNotNull();
    }

    @Test
    void 질문_삭제() {
        final Question question1 = questionRepository.save(new Question("제목", "내용").writeBy(UserTest.SANJIGI));
        final List<Question> list = questionRepository.findByDeletedFalse();
        assertThat(list).hasSize(1);

        questionRepository.delete(question1);
        questionRepository.flush();

        final Question expected = questionRepository.findById(question1.getId()).get();
        assertThat(expected.isDeleted()).isTrue();
    }
}
