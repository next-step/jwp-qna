package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class AnswerRepositoryTest {
    
    @Autowired
    private AnswerRepository answers;

}
