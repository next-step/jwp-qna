package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private LocalDateTime startTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.now();
    }

    @DisplayName("유저 저장")
    @Test
    void save() {
        User expected = new User("userId", "password", "name", "email");

        User actual = userRepository.save(expected);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
            () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            () -> assertThat(actual.getCreatedAt()).isAfterOrEqualTo(startTime),
            () -> assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(startTime)
        );
    }

    @DisplayName("id로 유저 조회")
    @Test
    void findByUserId() {
        User expected = new User("userId", "password", "name", "email");
        userRepository.save(expected);

        User actual = userRepository.findByUserId("userId").get();

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
            () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
            () -> assertThat(actual.getCreatedAt()).isAfterOrEqualTo(startTime),
            () -> assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(startTime)
        );
    }
}