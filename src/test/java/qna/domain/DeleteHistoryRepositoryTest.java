package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.saveAll(
                Arrays.asList(UserTest.JAVAJIGI, UserTest.SANJIGI, UserTest.YONG)
        );
    }

    @Test
    void save() {
        final DeleteHistory deleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, 1L,  UserTest.JAVAJIGI));
        assertThat(deleteHistoryRepository.findById(deleteHistory.getId())).isNotNull();
    }
}