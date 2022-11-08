package qna.domain.user.factory;

import qna.domain.repository.UserRepository;
import qna.domain.user.User;
import qna.domain.user.email.Email;
import qna.domain.user.email.factory.EmailFactory;
import qna.domain.user.email.factory.EmailFactoryImpl;
import qna.domain.user.name.Name;
import qna.domain.user.name.factory.NameFactory;
import qna.domain.user.name.factory.NameFactoryImpl;
import qna.domain.user.password.Password;
import qna.domain.user.password.factory.PasswordFactory;
import qna.domain.user.password.factory.PasswordFactoryImpl;
import qna.domain.user.userid.UserId;
import qna.domain.user.userid.factory.UserIdFactory;
import qna.domain.user.userid.factory.UserIdFactoryImpl;

public class UserFactoryImpl implements UserFactory {

    private final UserIdFactory userIdFactory;
    private final PasswordFactory passwordFactory;
    private final NameFactory nameFactory;
    private final EmailFactory emailFactory;

    public UserFactoryImpl(UserRepository userRepository) {
        this.userIdFactory = new UserIdFactoryImpl(userRepository);
        this.passwordFactory = new PasswordFactoryImpl();
        this.nameFactory = new NameFactoryImpl();
        this.emailFactory = new EmailFactoryImpl();
    }

    @Override
    public User create(Long id, String userId, String password, String name, String email) {
        UserId createUserId = userIdFactory.create(userId);
        Password createPassword = passwordFactory.create(password);
        Name createName = nameFactory.create(name);
        Email createEmail = emailFactory.create(email);
        return new User(id, createUserId, createPassword, createName, createEmail);
    }
}
