package qna.domain;

public class TestCreateFactory {

    static public User createUser(Long id) {
        return new User(id, id.toString(), "password", "name", "email");
    }

    static public Question createQuestion(User user) {
        return new Question("title", "contents").writeBy(user);
    }

    static public Answer createAnswer(User user, Question question) {
        return new Answer(user, question, "contents");
    }
}
