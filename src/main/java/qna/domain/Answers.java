package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Answer의 일급컬렉션
 * @since 2021.11.26
 * @author Inmook,Jeong
 */
public class Answers {

	@Autowired
	AnswerRepository answerRepository;

	private List<Answer> answers;

	private Answers(){}

	public Answers(Long questionId) {
		this.answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
	}

	/**
	 * 전체 답변 삭제
	 * @param user
	 */
	public boolean deleteAnswers(User user) {
		if(isAnswersOwner(user)) {
			deleteAll(user);
			return true;
		}
		return false;
	}

	/**
	 * 특정 답변 삭제
	 * @param user
	 * @param answer
	 */
	public boolean deleteAnswer(User user, Answer answer) {
		if(isAnswerOwner(user, answer)) {
			deleteAnswer(answer);
			return true;
		}
		return false;
	}

	private void deleteAll(User user) {
		for(Answer answer : this.answers) {
			deleteAnswer(answer);
		}
	}

	private void deleteAnswer(Answer answer) {
		answer.setDeleted(true);
	}

	/**
	 * 로그인한 사용자가 모든 답변의 작성자인지 확인
	 * @param user
	 * @return
	 */
	public boolean isAnswersOwner(User user) {
		return this.answers.stream().allMatch(answer -> answer.isOwner(user));
	}

	/**
	 * 로그인한 사용자가 특정 답변의 작성자인지 확인
	 * @param user
	 * @param targetAnswer
	 * @return
	 */
	public boolean isAnswerOwner(User user, Answer targetAnswer) {
		return this.answers.stream().allMatch(answer -> answer.isOwner(user) && answer.equals(targetAnswer));
	}

	public int size() {
		return this.size();
	}
}
