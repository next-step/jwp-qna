package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.repository.QuestionRepository;

@DataJpaTest
public class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EntityManager entityManager;

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("등록된 질문을 삭제한다.")
    @Test
    void delete(){
        long q1 = 1L;
        Question question = questionRepository.findById(q1).get();

        question.delete();
        questionRepository.flush();
        entityManager.clear();

        Question actual = questionRepository.findById(q1).get();
        assertThat(actual.isDeleted()).isTrue();
    }

}
