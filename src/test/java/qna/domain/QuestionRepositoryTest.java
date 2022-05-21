package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserRepositoryTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 질문_등록() {
        Question question = new Question("제목", "내용").writeBy(UserRepositoryTest.JAVAJIGI);
        Question saved = questionRepository.save(question);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void 질문_조회() {
        final User user1 = userRepository.save(new User("donghee.han", "password", "donghee", "donghee@slipp.net"));
        final Question question1 = questionRepository.save(new Question("제목", "내용").writeBy(user1));

        Question question2 = questionRepository.findById(question1.getId()).get();
        assertThat(question2).isNotNull();
        assertThat(question2.getTitle()).isEqualTo("제목");
        assertThat(question2.getContents()).isEqualTo("내용");

        User user2 = userRepository.findById(question2.getWriterId()).get();
        assertThat(user2).isEqualTo(user1);
    }

    @Test
    void 질문_수정() {
        Question question1 = questionRepository.save(new Question("제목", "내용").writeBy(UserRepositoryTest.SANJIGI));
        question1.updateTitle("수정 제목");

        Question question2 = questionRepository.findByTitle("수정 제목").get(0);
        assertThat(question2).isNotNull();
    }

    @Test
    void 질문_삭제() {
        final Question question1 = questionRepository.save(new Question("제목", "내용").writeBy(UserRepositoryTest.SANJIGI));
        final List<Question> list = questionRepository.findByDeletedFalse();
        assertThat(list).hasSize(1);

        questionRepository.delete(question1);
        questionRepository.flush();

        final Question expected = questionRepository.findById(question1.getId()).get();
        assertThat(expected.isDeleted()).isTrue();
    }
}
