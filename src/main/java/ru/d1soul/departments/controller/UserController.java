package ru.d1soul.departments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.security.jwt.dto.AuthUser;
import ru.d1soul.departments.security.jwt.dto.JwtUserDto;
import ru.d1soul.departments.model.User;
import ru.d1soul.departments.security.jwt.dto.JwtResponse;
import ru.d1soul.departments.security.jwt.JwtTokenProvider;
import ru.d1soul.departments.service.authentification.UserDetailsServiceImpl;
import ru.d1soul.departments.web.BadFormException;
import ru.d1soul.departments.web.NotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/auth")
public class UserController {

    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider,
                          AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {

        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthUser authUser){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                                             authUser.getUsername(),
                                                             authUser.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authUser.getUsername());
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();
        Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        String token = jwtTokenProvider.createToken(new JwtUserDto(username, password, roles));
        return new ResponseEntity<>(new JwtResponse(username, roles, token), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users")
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users/{username}")
    public User findUserByUsername(@PathVariable String username){
        Optional<User> user = userService.findByUsername(username);
        if(user.isPresent()) {
            return user.get();
        }
        else throw new NotFoundException("User with Username: " + username + " Not Found!");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/registration")
    public void registerUser (@Valid @RequestBody User user) {
        Optional<User> newUser = userService.findByUsername(user.getUsername());
        if (newUser.isEmpty()){
            if (user.getPassword().equals(user.getConfirmPassword())) {
                userService.save(user);
            }
            else {
                throw new BadFormException("Password and confirmPassword not matches!");
            }
        }
        else {
            throw new BadFormException("User with username: " + newUser.get().getUsername() + " already exists");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/changing-password")
    public User changePassword(@RequestBody AuthUser authUser){
        String username = authUser.getUsername();
        String oldPassword = authUser.getPassword();
        String newPassword = authUser.getNewPassword();
        String newConfirmPassword = authUser.getNewConfirmPassword();
        if (newPassword.equals(newConfirmPassword)){
            return userService.changePassword(username, oldPassword, newPassword, newConfirmPassword);
        }
        else {
            throw new BadFormException("New password and confirmPassword not matches!");
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/users/{username}")
    public User updateUser(@PathVariable  String username,
                           @Valid @RequestBody User user){

        return userService.findByUsername(username).map(newUser -> {
            newUser.setUsername(user.getUsername());
            newUser.setBirthDate(user.getBirthDate());
            newUser.setGender(user.getGender());
            newUser.setRoles(user.getRoles());
            return userService.save(newUser);
        }).get();
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value =  "/users/{username}")
    public void deleteUser(@PathVariable String username) {
       userService.deleteByUsername(username);
    }
}
