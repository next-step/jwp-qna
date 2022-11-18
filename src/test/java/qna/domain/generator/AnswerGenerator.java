package qna.domain.generator;

import qna.domain.answer.Answer;
import qna.domain.answer.AnswerRepository;
import qna.domain.question.Question;
import qna.domain.user.User;

public class AnswerGenerator {

	private final AnswerRepository answerRepository;

	public AnswerGenerator(AnswerRepository answerRepository) {
		this.answerRepository = answerRepository;
	}

	public static Answer answer(User writer, Question question, String contents) {
		Answer answer = new Answer(writer, question, contents);
		question.addAnswer(answer);
		return answer;
	}

	public Answer savedAnswer(User writer, Question question, String contents) {
		Answer answer = answer(writer, question, contents);
		answer.toQuestion(question);
		return answerRepository.saveAndFlush(answer);
	}
}
