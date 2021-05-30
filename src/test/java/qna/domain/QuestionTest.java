package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("질문 생성")
    void create() {
        //given
        //when
        questionRepository.save(Q1);
        Optional<Question> question = questionRepository.findById(1L);
        //then
        assertThat(question.isPresent()).isTrue();
    }

    @Test
    @DisplayName("질문 삭제")
    void delete() {
        //given
        //when
        questionRepository.save(Q1);
        questionRepository.deleteByTitle("title1");
        Optional<Question> question = questionRepository.findById(1L);
        //then
        assertThat(question.isPresent()).isFalse();
    }

    @Test
    @DisplayName("지우지 않은 글 조회")
    void findNotDeleted() {
        //given
        //when
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        List<Question> questions = questionRepository.findByDeletedFalse();
        //then
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("제목 수정")
    void update() {
        //given
        //when
        Question save = questionRepository.save(Q1);
        save.setTitle("title3");
        List<Question> questions = questionRepository.findByWriterId(1L);
        //then
        assertThat(questions.size() > 0).isTrue();
        assertThat(questions.get(0).getTitle()).isEqualTo("title3");
    }
}
