package ru.d1soul.departments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.d1soul.departments.api.repository.authentification.UserRepository;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.model.AuthUser;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "")
public class UserController {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/register")
    public List<AuthUser> findAllMainDeptEmpl() {
        return userRepository.findAll();
    }




    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register")
    public AuthUser registerUser (@Valid @RequestBody AuthUser authUser) {
        authUser.setPassword(bCryptPasswordEncoder.encode(authUser.getPassword()));
        authUser.setConfirmPassword(bCryptPasswordEncoder.encode(authUser.getConfirmPassword()));
        return  userRepository.save(authUser);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/update-user{userName}")
    public AuthUser updateUser(@PathVariable  String userName, @Valid @RequestBody AuthUser authUser){
        return userService.findByUserName(userName).map(userUpd -> {
            userUpd.setUserName(authUser.getUserName());
            userUpd.setPassword(bCryptPasswordEncoder.encode(authUser.getPassword()));
            userUpd.setConfirmPassword(bCryptPasswordEncoder.encode(authUser.getConfirmPassword()));
            userUpd.setBirthDate(authUser.getBirthDate());
            userUpd.setGender(authUser.getGender());
            userUpd.setRoles(authUser.getRoles());
            return userService.save(userUpd);
        }).get();
    }


}
