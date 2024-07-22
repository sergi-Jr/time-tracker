package sergi.example.user.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sergi.example.user.dal.UserService;
import sergi.example.user.dto.UserCreateDTO;
import sergi.example.user.dto.UserDTO;
import sergi.example.user.dto.UserUpdateDTO;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService service) {
        userService = service;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO create(@Valid @RequestBody UserCreateDTO data) {
        return userService.create(data);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO update(@Valid @RequestBody UserUpdateDTO data, @PathVariable Long id) {
        return userService.update(id, data);
    }
}
