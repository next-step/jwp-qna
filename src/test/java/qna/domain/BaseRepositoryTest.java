package qna.domain;

import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import org.springframework.beans.factory.annotation.Autowired;

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
