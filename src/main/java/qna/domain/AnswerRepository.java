package qna.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestion(Question question);

}
