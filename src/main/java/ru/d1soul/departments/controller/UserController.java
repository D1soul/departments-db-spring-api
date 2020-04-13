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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.security.jwt.dto.JwtUserDto;
import ru.d1soul.departments.model.User;
import ru.d1soul.departments.security.jwt.dto.JwtResponse;
import ru.d1soul.departments.security.jwt.JwtTokenProvider;
import ru.d1soul.departments.service.authentification.UserDetailsServiceImpl;
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

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserController(UserDetailsServiceImpl userDetailsService,
                          UserService userService, JwtTokenProvider jwtTokenProvider,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          AuthenticationManager authenticationManager) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
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
        return new ResponseEntity<JwtResponse>(new JwtResponse(username, token), HttpStatus.OK);
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
              userDetailsService.saveUser(user);
        }
        else {
            throw new BadCredentialsException("User with username: " + newUser.get().getUsername() + " already exists");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/users/{username}")
    public void updateUser(@PathVariable  String username, @Valid @RequestBody User user){
       user = userService.findByUsername(username).get();
       userDetailsService.saveUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value =  "/users/{username}")
    public void deleteUser(@PathVariable String username) {
       userService.deleteByUsername(username);
    }
}
