package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * packageName : qna.domain
 * fileName : AnswerRepositoryTest
 * author : haedoang
 * date : 2021-11-09
 * description :
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@TestMethodOrder(MethodOrderer.MethodName.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("Answer save")
    public void T1_save() {
        //WHEN
        Answer save = answerRepository.save(AnswerTest.A1);
        Answer save2 = answerRepository.save(AnswerTest.A2);
        //THEN
        assertThat(save.getContents()).isEqualTo(AnswerTest.A1.getContents());
        assertThat(save2.getContents()).isEqualTo(AnswerTest.A2.getContents());
    }
}
