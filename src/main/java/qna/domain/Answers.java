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

	/*
	TODO
	1. Question ID로 Answer List 가져오기
	2. 로그인한 User가 모든 Answer의 Owner인지 검증
	3. 모든 Answer 삭제
	4. Question의 특정 Answer 삭제
		- return 타깃 Answer가 삭제된 Answers
	 */

	public Answers(Long questionId) {
		this.answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
	}

	public void deleteAnswers(User user) {
		if(isAnswersOwner(user)) {
			// TODO DELETE
		}
	}

	private boolean isAnswersOwner(User user) {
		return this.answers.stream().allMatch(answer -> answer.isOwner(user));
	}

	private boolean isAnswerOwner(User user, Answer targetAnswer) {
		return this.answers.stream().allMatch(answer -> answer.isOwner(user) && answer.equals(targetAnswer));
	}
}
