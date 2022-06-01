package qna.domain;

import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseRepositoryTest {
    @Autowired
    UserRepository userRepository;

    User savedJavajigi;
    User savedSanjigi;

    protected void saveUsers() {
        savedJavajigi = userRepository.save(JAVAJIGI);
        savedSanjigi = userRepository.save(SANJIGI);
    }
}
