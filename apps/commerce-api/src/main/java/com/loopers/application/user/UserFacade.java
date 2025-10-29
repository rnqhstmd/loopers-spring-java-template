package com.loopers.application.user;


import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

	private final UserService userService;

    public UserInfo signUp(String userId, String email, String birthDate, Gender gender) {
        User user = userService.signUp(userId, email, birthDate, gender);
        return UserInfo.from(user);
    }

    public UserInfo getUserInfo(String userId) {
        User user = userService.getUserByUserId(userId);
        return UserInfo.from(user);
    }
}
