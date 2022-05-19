package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sun.istack.NotNull;

import qna.domain.common.DatedAtEntity;
import qna.exception.UnAuthorizedException;

@Entity
@Table(name = "user", uniqueConstraints = {
		@UniqueConstraint(name = "UK_a3imlf41l37utmxiquukk8ajc", columnNames = { "user_id" }) })
public class User extends DatedAtEntity {
	public static final GuestUser GUEST_USER = new GuestUser();

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@Column(name = "user_id", unique = true)
	private String userId;
	@NotNull
	@Column(name = "password")
	private String password;
	@NotNull
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;

	private User() {
	}

	public User(String userId, String password, String name, String email) {
		this(null, userId, password, name, email);
	}

	public User(Long id, String userId, String password, String name, String email) {
		this.id = id;
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.email = email;
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
	}

	private boolean matchUserId(String userId) {
		return this.userId.equals(userId);
	}

	public boolean matchPassword(String targetPassword) {
		return this.password.equals(targetPassword);
	}

	public boolean equalsNameAndEmail(User target) {
		if (Objects.isNull(target)) {
			return false;
		}

		return name.equals(target.name) && email.equals(target.email);
	}

	public boolean isGuestUser() {
		return false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", userId='" + userId + '\'' + ", password='" + password + '\'' + ", name='"
				+ name + '\'' + ", email='" + email + '\'' + '}';
	}

	private static class GuestUser extends User {
		@Override
		public boolean isGuestUser() {
			return true;
		}
	}
}
