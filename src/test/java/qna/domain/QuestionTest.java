package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {

    @Autowired
    QuestionRepository questionRepository;

    public static final Question Q1 = new Question(1L,"title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L,"title2", "contents2").writeBy(UserTest.SANJIGI);

    @BeforeEach
    public void init(){
        UserTest.JAVAJIGI.setId(null);
        UserTest.SANJIGI.setId(null);

        Q1.setId(null);
        Q2.setId(null);

    }


    @Test
    public void saveEntity(){
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(questionRepository.save(Q1));
        questions.add(questionRepository.save(Q2));

        assertThat(questions).extracting("id").isNotNull();
        assertThat(questions).extracting("title").containsExactly("title1", "title2");
    }

    @Test
    public void equalTest(){
        Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Question q1Saved= questionRepository.save(Q1);
        assertThat(q1Saved.hashCode()).isEqualTo(Q1.hashCode());
    }


    @Test
    public void annotingTest(){
        Question q1Saved = questionRepository.save(Q1);
        assertThat(q1Saved.getCreatedAt()).isNotNull();
        assertThat(q1Saved.getUpdatedAt()).isNotNull();
    }

    @Test
    public void cascadePersist(){
        assertThat(Q1.getWriter().getId()).isNull();
        Question q1Saved = questionRepository.save(Q1);
        assertThat(q1Saved.getWriter().getId()).isNotNull();
    }
}
