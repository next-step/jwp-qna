package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private Question question;
    private User user;

    @BeforeEach
    void setup() {
        user = new User("lkimilhol", "1234", "김일호", "lkimilhol@gmail.com");
        question = new Question("질문", "내용");
        question.writeBy(user);
        userRepository.save(user);
        questionRepository.save(question);
    }

    @Test
    @DisplayName("질문 생성")
    void create() {
        //given
        userRepository.save(user);
        //when
        questionRepository.save(question);
        Optional<Question> question = questionRepository.findById(1L);
        //then
        assertThat(question.isPresent()).isTrue();
    }

    @Test
    @DisplayName("질문 삭제")
    void delete() {
        //given
        userRepository.save(user);
        //when
        questionRepository.save(question);
        questionRepository.deleteByTitle("title1");
        Optional<Question> question = questionRepository.findById(1L);
        //then
        assertThat(question.isPresent()).isFalse();
    }

    @Test
    @DisplayName("지우지 않은 글 조회")
    void findNotDeleted() {
        //given
        userRepository.save(user);
        //when
        questionRepository.save(question);
        questionRepository.save(new Question("질문2", "내용2"));
        List<Question> questions = questionRepository.findByDeletedFalse();
        //then
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("제목 수정")
    void update() {
        //given
        userRepository.save(user);
        //when
        Question save = questionRepository.save(question);
        save.setTitle("title3");
        List<Question> questions = questionRepository.findByWriterId(user.getId());
        //then
        assertThat(questions.size() > 0).isTrue();
        assertThat(questions.get(0).getTitle()).isEqualTo("title3");
    }

    @Test
    @DisplayName("답변 리스트 불러오기")
    void sizeAnswers() {
        //given
        userRepository.save(user);
        //when
        Question save = questionRepository.save(question);
        save.addAnswer(new Answer(JAVAJIGI, save, "answer1"));
        save.addAnswer(new Answer(SANJIGI, save, "answer2"));
        //then
        assertThat(save.getAnswers().size()).isEqualTo(2);
    }
}
