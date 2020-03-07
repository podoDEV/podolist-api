package com.podo.podolist.controller;

import com.podo.podolist.exception.NoContentException;
import com.podo.podolist.model.User;
import com.podo.podolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(
        origins = {"*"},
        allowCredentials = "true"
)
@RequiredArgsConstructor
public class UserController implements Authenticated {

    private final UserService userService;

    @GetMapping("/users")
    public List<User.Response> getUsers(Pageable pageable) {
        return userService.getUsers(pageable).stream()
                .map(User.Response::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{userId}")
    public User.Response getUser(@PathVariable int userId) {
        return userService.getUser(userId)
                .map(User.Response::from)
                .orElseThrow(NoContentException::new);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User.Response createUesr(@RequestBody User.Request userRequest) {
        return User.Response.from(userService.createUser(userRequest.toUser()));
    }

    @PutMapping("/users/{userId}")
    public User.Response updateUser(@PathVariable int userId,
                                    @RequestBody User.Request userRequest) {
        return User.Response.from(userService.updateUser(userId, userRequest.toUser()));
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/users/me")
    public User.Response getMe(@ApiIgnore @ModelAttribute("userId") Integer userId) {
        return userService.getUser(userId)
                .map(User.Response::from)
                .orElseThrow(NoContentException::new);
    }
}
