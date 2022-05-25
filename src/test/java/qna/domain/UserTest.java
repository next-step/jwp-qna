package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    @Autowired
    UserRepository userRepository;
    public static final User JAVAJIGI = new User( 1L,"javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User( 2L,"sanjigi", "password", "name", "sanjigi@slipp.net");

    @BeforeEach
    public void init(){
        JAVAJIGI.setId(null);
        SANJIGI.setId(null);

        Q1.setId(null);
        Q2.setId(null);
        Q1.getAnswers().clear();
        Q2.getAnswers().clear();


        A1.setId(null);
        A2.setId(null);
        A1.toQuestion(Q1);
        A1.toQuestion(Q1);
    }

    @Test
    public void saveEntityTest(){
        ArrayList<User> users = new ArrayList<>();
        users.add(userRepository.save(JAVAJIGI));
        users.add(userRepository.save(SANJIGI));

        assertThat(users).extracting("id").isNotNull();
        assertThat(users).extracting("userId").containsExactly("javajigi", "sanjigi");
    }

    @Test
    public void equalTest(){
        User javajigi = new User( "javajigi", "password", "name", "javajigi@slipp.net");
        User u1Saved= userRepository.save(javajigi);
        assertThat(u1Saved.hashCode()).isEqualTo(javajigi.hashCode());
    }

    @Test
    public void annotingTest(){
        User u1Saved= userRepository.save(JAVAJIGI);
        assertThat(u1Saved.getCreatedAt()).isNotNull();
        assertThat(u1Saved.getUpdatedAt()).isNotNull();
    }
}
