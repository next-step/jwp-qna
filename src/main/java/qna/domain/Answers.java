package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Answer의 일급컬렉션
 * @since 2021.11.26
 * @author Inmook,Jeong
 */
public class Answers {

	private List<Answer> answers;

	public Answers(List<Answer> answers) {
		this.answers = answers;
	}

	/**
	 * 전체 답변 삭제
	 * @param user
	 */
	public Answers deleteAnswers(User user) {
		if(isAnswersOwner(user)) {
			deleteAll();
		}
		return this;
	}

	/**
	 * 특정 답변 삭제
	 * @param user
	 * @param answer
	 */
	public Answers deleteAnswer(User user, Answer answer) {
		if(isAnswerOwner(user, answer)) {
			deleteAnswer(answer);
		}
		return this;
	}

	private Answers deleteAll() {
		for(Answer answer : this.answers) {
			deleteAnswer(answer);
		}
		return this;
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

	public int count() {
		return this.answers.size();
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}
}
