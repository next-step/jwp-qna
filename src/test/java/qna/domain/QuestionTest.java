package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.user;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = question();

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Create 및 ID 생성 테스트")
    @Test
    void save() {
        Q1.mappedToWriter(userRepository.save(user()));

        Question save = questionRepository.save(Q1);
        assertThat(save.getId()).isNotNull();
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        Q1.mappedToWriter(userRepository.save(user()));

        Question save = questionRepository.save(Q1);
        Question found = questionRepository.findById(save.getId()).orElse(null);
        assertThat(found).isEqualTo(save);
    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        Q1.mappedToWriter(userRepository.save(user()));

        Question save = questionRepository.save(Q1);
        save.setContents("update!!");
        Question found = questionRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));
        assertThat(found.getContents()).isEqualTo("update!!");
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        Q1.mappedToWriter(userRepository.save(user()));

        Question save = questionRepository.save(Q1);
        questionRepository.delete(save);

        Question found = questionRepository.findById(save.getId()).orElse(null);
        assertThat(found).isNull();
    }

    static Question question() {
        return new Question("JPA", "JPA에서 엔티티를 저장할 때 값이 null로 저장이 되네요?").writeBy(user());
    }

    static Question question2() {
        return new Question("JPA", "기존에 있던 연관 관계를 먼저 제거하고 삭제해야해요?").writeBy(user());
    }
}
