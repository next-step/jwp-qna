package qna.domain.entity;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import qna.domain.vo.Email;
import qna.domain.vo.Name;
import qna.domain.vo.Password;
import qna.domain.vo.UserId;
import qna.exception.UnAuthorizedException;

@Entity
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Email email;

	@Embedded
	private Name name;

	@Embedded
	private Password password;

	@Embedded
	private UserId userId;

	protected User() {
	}

	private User(Long id, String userId, String password, String name, String email) {
		this.id = id;
		this.userId = UserId.generate(userId);
		this.password = Password.generate(password);
		this.name = Name.generate(name);
		this.email = Email.generate(email);
	}

	public static User generate(Long id, String userId, String password, String name,
		String email) {
		return new User(id, userId, password, name, email);
	}

	public void update(User loginUser, User target) {
		if (!matchUserId(loginUser.userId)) {
			throw new UnAuthorizedException();
		}
		if (!matchPassword(target.password)) {
			throw new UnAuthorizedException();
		}
		this.name = target.name;
		this.email = target.email;
		updatedAtNow();
	}

	private boolean matchUserId(UserId userId) {
		return this.userId.equals(userId);
	}

	private boolean matchPassword(Password targetPassword) {
		return this.password.equals(targetPassword);
	}

	public Long id() {
		return id;
	}

	public UserId userId() {
		return userId;
	}

	public String name() {
		return name.value();
	}

	public void changeName(String name) {
		this.name.changeName(name);
		updatedAtNow();
	}

	public void changeEmail(String email) {
		this.email.changeEmail(email);
		updatedAtNow();
	}

	@Override
	public String toString() {
		return "User{"
			+ "id=" + id
			+ ", userId='" + userId + '\''
			+ ", password='" + password + '\''
			+ ", name='" + name + '\''
			+ ", email='" + email + '\''
			+ '}';
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof User)) {
			return false;
		}
		User user = (User) object;
		return Objects.equals(id, user.id)
			&& Objects.equals(email, user.email)
			&& Objects.equals(name, user.name)
			&& Objects.equals(password, user.password)
			&& Objects.equals(userId, user.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, name, password, userId);
	}

	public String email() {
		return email.value();
	}
}
