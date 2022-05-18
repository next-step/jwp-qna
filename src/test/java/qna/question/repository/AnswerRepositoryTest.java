package qna.question.repository;

import config.annotation.LocalDataJpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.question.domain.Answer;
import qna.question.domain.Question;
import qna.user.domain.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@LocalDataJpaConfig
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private final User writer = new User(1L, "userId", "password", "name", "email");
    private final Question question = new Question(1L, "title", "content");

    @Test
    void 삭제_상태를_제외한_질문_답변_조회_테스트() {
        List<Answer> answers = answerRepository.saveAll(Arrays.asList(
                new Answer(writer, question, "content1"),
                new Answer(writer, question, "content2"),
                new Answer(writer, question, "content3"),
                new Answer(writer, question, "content4")
        ));

        List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertThat(result.size()).isGreaterThanOrEqualTo(result.size());
        for (Answer answer : answers) {
            assertThat(result.stream().anyMatch(value -> value.getContents().equals(answer.getContents()))).isTrue();
        }
    }

    @Test
    void 삭제_상태가_아닌_답변_조회_테스트() {
        Answer answer = new Answer(writer, question, "content");
        Answer savedAnswer = answerRepository.save(answer);

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(savedAnswer.getId());
        assertThat(result.get().getWriterId()).isEqualTo(savedAnswer.getWriterId());
        assertThat(result.get().getQuestionId()).isEqualTo(savedAnswer.getQuestionId());
        assertThat(result.get().getContents()).isEqualTo(savedAnswer.getContents());
    }
}