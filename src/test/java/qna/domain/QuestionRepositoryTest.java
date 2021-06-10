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
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private Question 질문;
    private User 글쓴이;

    @BeforeEach
    void setup() {
        글쓴이 = new User("lkimilhol", "1234", "김일호", "lkimilhol@gmail.com");
        질문 = new Question("질문", "내용");
        질문.writeBy(글쓴이);
        userRepository.save(글쓴이);
        questionRepository.save(질문);
    }

    @Test
    @DisplayName("질문 생성")
    void create() {
        //given
        userRepository.save(글쓴이);
        //when
        questionRepository.save(질문);
        Optional<Question> 질문_조회_결과 = questionRepository.findById(질문.getId());
        //then
        assertThat(질문_조회_결과.isPresent()).isTrue();
    }

    @Test
    @DisplayName("질문 삭제")
    void delete() {
        //given
        userRepository.save(글쓴이);
        //when
        questionRepository.save(질문);
        questionRepository.deleteByTitle("title1");
        Optional<Question> 질문_조회_결과 = questionRepository.findById(1L);
        //then
        assertThat(질문_조회_결과.isPresent()).isFalse();
    }

    @Test
    @DisplayName("지우지 않은 글 조회")
    void findNotDeleted() {
        //given
        userRepository.save(글쓴이);
        //when
        questionRepository.save(질문);
        questionRepository.save(new Question("질문2", "내용2"));
        List<Question> 질문_목록 = questionRepository.findByDeletedFalse();
        //then
        assertThat(질문_목록.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("제목 수정")
    void update() {
        //given
        userRepository.save(글쓴이);
        //when
        questionRepository.save(질문);
        질문.setTitle("title3");
        List<Question> 질문_목록 = questionRepository.findByWriterId(글쓴이.getId());
        //then
        assertThat(질문_목록.size() > 0).isTrue();
        assertThat(질문_목록.get(0).getTitle()).isEqualTo("title3");
    }

    @Test
    @DisplayName("답변 리스트 불러오기")
    void sizeAnswers() {
        //given
        userRepository.save(글쓴이);
        //when
        questionRepository.save(질문);
        질문.addAnswer(new Answer(JAVAJIGI, 질문, "answer1"));
        질문.addAnswer(new Answer(SANJIGI, 질문, "answer2"));
        //then
        assertThat(질문.getAnswers().size()).isEqualTo(2);
    }
}
