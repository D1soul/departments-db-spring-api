package ru.d1soul.departments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.security.jwt.dto.JwtUserDto;
import ru.d1soul.departments.model.User;
import ru.d1soul.departments.security.jwt.dto.JwtResponse;
import ru.d1soul.departments.security.jwt.JwtTokenProvider;
import ru.d1soul.departments.web.NotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "")
public class UserController {

    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider,
                          AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody User user){
        Authentication authentication =
                       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                                           user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getAuthorities();
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();
        Set<String> roles = userDetails.getAuthorities()
                                       .stream().map(GrantedAuthority::getAuthority)
                                       .collect(Collectors.toSet());

        String token = jwtTokenProvider.createToken(new JwtUserDto(username, password, roles));
        return new ResponseEntity<>(new JwtResponse(username, token), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/jwt-response")
    public String findJwtResponse() {
        User user2 = userService.findByUsername("admin").get();
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        user2.getUsername(), user2.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getAuthorities();
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();
        Set<String> roles = userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String token = jwtTokenProvider.createToken(new JwtUserDto(username, password, roles));


        return token;
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
              userService.save(user);
        }
        else {
            throw new BadCredentialsException("User with username: " + newUser.get().getUsername() + " already exists");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/users/{username}")
    public User updateUser(@PathVariable  String username,
                           @Valid @RequestBody User user){

       return userService.findByUsername(username).map(newUser -> {
           newUser.setUsername(user.getUsername());
           newUser.setPassword(user.getPassword());
           newUser.setConfirmPassword(user.getConfirmPassword());
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
