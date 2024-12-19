package com.soumya.Project.Management.serviceimpl;

import com.soumya.Project.Management.JwtUtils.JwtProvider;
import com.soumya.Project.Management.model.User;
import com.soumya.Project.Management.repository.UserRepository;
import com.soumya.Project.Management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public User findProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromJwt(jwt);
        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found with this email");
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new Exception("User not found");
        return optionalUser.get();
    }

    @Override
    public User updateUserProjectCount(User user) {
        user.setProjectCount(user.getProjectCount()+1);
        return userRepository.save(user);
    }
}
