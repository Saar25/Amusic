package org.saartako.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<UserEntity> findALl() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<UserEntity> registry(@PathVariable("id") long id) {
        return userService.findById(id);
    }
}
