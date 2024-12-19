package com.soumya.Project.Management.service;

import com.soumya.Project.Management.model.User;

public interface UserService {
    User findProfileByJwt(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
    User findUserById(Long userId) throws Exception;
    User updateUserProjectCount(User user);
}
