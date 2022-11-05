package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest extends NewEntityTestBase {

    @Autowired
    private UserRepository repository;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        repository.saveAll(Arrays.asList(NEWUSER1, NEWUSER2));
    }

    @Test
    @DisplayName("동일성 테스트")
    void test1() {
        Optional<User> id2 = repository.findByUserId(NEWUSER1.getUserId());

        assertThat(id2.get()).isSameAs(NEWUSER1);
    }

    @Test
    @DisplayName("조회 테스트")
    void test2() {
        List<User> all = repository.findAll();

        assertThat(all).containsExactly(NEWUSER1, NEWUSER2);
    }
}