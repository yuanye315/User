package com.example.backend.service;

import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserPageResponse;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import net.bytebuddy.asm.Advice;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserPageResponse getUsers(String userName, String startDateTime, String endDateTime,
                               int pageNum, int pageSize) {

        LocalDateTime startTime = startDateTime.isEmpty() ? LocalDateTime.parse("1970-01-01T00:00:00") : LocalDateTime.parse(startDateTime);
        LocalDateTime endTime = endDateTime.isEmpty() ? LocalDateTime.parse("3000-01-01T00:00:00") : LocalDateTime.parse(endDateTime);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<User> userPage;

        if (userName.isEmpty()) {
            userPage = userRepository.findByCreatedAtBetween(startTime, endTime, pageable);
        }
        else {
            userPage =  userRepository.findByUserNameAndCreatedAtBetween(userName, startTime, endTime, pageable);
        }

        List<UserDto> userDtos = userPage.getContent().stream()
                .map(user -> new UserDto(user.getId(), user.getUserName(), user.getCreatedAt()))
                .collect(Collectors.toList());

        UserPageResponse response = new UserPageResponse();
        response.setList(userDtos);
        response.setTotal(userPage.getTotalElements());

        return response;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
