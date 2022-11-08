package qna.domain.user.userid.factory;

import qna.domain.user.userid.UserId;

public interface UserIdFactory {

    UserId create(String userId);
}
