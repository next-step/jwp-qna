package qna.domain.generator;

import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;

public class QuestionGenerator {

	private final QuestionRepository questionRepository;

	public QuestionGenerator(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}

	public static Question question(User writer) {
		return new Question("title", "contents").writeBy(writer);
	}

	public Question savedQuestion(User writer) {
		return questionRepository.saveAndFlush(question(writer));
	}
}
