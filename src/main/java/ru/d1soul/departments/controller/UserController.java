package ru.d1soul.departments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.model.AuthUser;
import ru.d1soul.departments.web.NotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "")
public class UserController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService userService;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users")
    public List<AuthUser> findAllMainDeptEmpl() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users/{username}")
    public AuthUser findUserByUsername(@PathVariable String username){
        Optional<AuthUser> user = userService.findByUsername(username);
        if(user.isPresent()) {
            return user.get();
        }
        else throw new NotFoundException("User with Username: " + username + " Not Found!");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/registration")
    public AuthUser registerUser (@Valid @RequestBody AuthUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(bCryptPasswordEncoder.encode(user.getConfirmPassword()));
        return  userService.save(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/users/{username}")
    public AuthUser updateUser(@PathVariable  String username, @Valid @RequestBody AuthUser user){
        return userService.findByUsername(username).map(userUpd -> {
            userUpd.setUsername(user.getUsername());
            userUpd.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userUpd.setConfirmPassword(bCryptPasswordEncoder.encode(user.getConfirmPassword()));
            userUpd.setBirthDate(user.getBirthDate());
            userUpd.setGender(user.getGender());
            userUpd.setRoles(user.getRoles());
            return userService.save(userUpd);
        }).get();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value =  "/users/{username}")
    public void deleteUser(@PathVariable String username) {
       userService.deleteByUsername(username);
    }
}
