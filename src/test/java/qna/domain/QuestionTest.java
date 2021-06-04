package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    private long QUESTION_ID;

    @BeforeEach
    void init() {
        questionRepository.save(Q1);
        List<Question> list = questionRepository.findAll();
        this.QUESTION_ID = list.get(0).getId();
    }

    @Test
    @DisplayName(value = "저장된 질문을 select 하여 저장한 데이터 맞는지 검증한다")
    void select() {
        List<Question> list = questionRepository.findAll();
        long id = list.get(0).getId();
        Question question = questionRepository.findByIdAndDeletedFalse(id).get();
        assertThat(question.getTitle()).isEqualTo("title1");
        assertThat(question.getContents()).isEqualTo("contents1");
    }

    @Test
    @DisplayName(value = "새로운 질문을 저장한다")
    void insert() {
        Question question = questionRepository.save(Q2);
        assertThat(question).isEqualTo(Q2);
    }

    @Test
    @DisplayName(value = "질문의 제목을 수정하고 DB에 반영이 되었는지 검증힌다")
    void update() {
        final String newTitle = "질문 있습니다~";
        Question question = questionRepository.getOne(QUESTION_ID);
        question.setTitle(newTitle);
        questionRepository.saveAndFlush(question);

        Question afterUpdate = questionRepository.findByIdAndDeletedFalse(QUESTION_ID).get();
        assertThat(afterUpdate.getTitle()).isEqualTo(newTitle);
    }

    @Test
    @DisplayName(value = "삭제되지 않은 모든 질문을 select 한다")
    void selectWhereNotDelete() {
        List<Question> notDeletedQuestions = questionRepository.findByDeletedFalse();
        assertThat(notDeletedQuestions.size()).isEqualTo(1);
    }

    @Test
    @DisplayName(value = "DB에 저장되면 날짜 데이터가 생성된다")
    void createdAtAndUpdatedAtAreExists() {
        Question question = questionRepository.getOne(QUESTION_ID);
        assertThat(question.getCreatedAt()).isNotNull();
        assertThat(question.getUpdatedAt()).isNotNull();
    }

}
