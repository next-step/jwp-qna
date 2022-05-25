package qna.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    void 질문_생성() {
        User user = userRepository.save(UserTest.SANJIGI);
        Question expected = new Question("title10", "contents10").writeBy(user);
        Question actual = questionRepository.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    void 삭제_되지_않은_질문목록_조회() {
        User user = userRepository.save(UserTest.SANJIGI);
        Question deletedQuestion = questionRepository.save(
                new Question("title1", "contents1").writeBy(user));
        Question notDeletedQuestion = questionRepository.save(
                new Question("title2", "contents2").writeBy(user));

        deletedQuestion.setDeleted(true);

        List<Question> foundQuestions = questionRepository.findByDeletedFalse();
        assertThat(foundQuestions).containsExactly(notDeletedQuestion);
    }

    @Test
    void 아이디로_삭제_되지_않은_질문_조회() {
        User user = userRepository.save(UserTest.SANJIGI);
        Question deletedQuestion = questionRepository.save(
                new Question("title1", "contents1").writeBy(user));
        Question notDeletedQuestion = questionRepository.save(
                new Question("title2", "contents2").writeBy(user));

        deletedQuestion.setDeleted(true);

        Optional<Question> foundDeletedQuestion = questionRepository.findByIdAndDeletedFalse(deletedQuestion.getId());
        Optional<Question> foundNotDeletedQuestion = questionRepository.findByIdAndDeletedFalse(
                notDeletedQuestion.getId());
        assertAll(
                () -> assertThat(foundDeletedQuestion.isPresent()).isFalse(),
                () -> assertThat(foundNotDeletedQuestion.isPresent()).isTrue()
        );
    }
}