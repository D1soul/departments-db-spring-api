package ru.d1soul.departments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.d1soul.departments.api.service.authentification.ResetPasswordService;
import ru.d1soul.departments.api.service.authentification.UserService;
import ru.d1soul.departments.security.jwt.dto.PasswordResetDto;
import ru.d1soul.departments.model.PasswordResetToken;
import ru.d1soul.departments.security.jwt.dto.AuthUser;
import ru.d1soul.departments.security.jwt.dto.JwtUserDto;
import ru.d1soul.departments.model.User;
import ru.d1soul.departments.security.jwt.dto.JwtResponse;
import ru.d1soul.departments.security.jwt.JwtTokenProvider;
import ru.d1soul.departments.security.jwt.dto.PasswordChangingUser;
import ru.d1soul.departments.service.authentification.UserDetailsServiceImpl;
import ru.d1soul.departments.web.exception.BadFormException;
import ru.d1soul.departments.web.exception.NotFoundException;
import ru.d1soul.departments.web.exception.UnauthorizedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/auth")
public class UserController {

    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;
    private ResetPasswordService resetPasswordService;
    private JavaMailSender javaMailSender;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService,
                          JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService,
                          ResetPasswordService resetPasswordService, JavaMailSender javaMailSender) {

        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.resetPasswordService = resetPasswordService;
        this.javaMailSender = javaMailSender;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthUser authUser){
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(authUser.getUsername());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authUser.getUsername(), authUser.getPassword()));
            String username = userDetails.getUsername();
            String password = userDetails.getPassword();
            Set<String> roles = userDetails.getAuthorities().stream().map(
                                       GrantedAuthority::getAuthority).collect(Collectors.toSet());
            String token = jwtTokenProvider.createToken(new JwtUserDto(username, password, roles));
            return new ResponseEntity<>(new JwtResponse(username, roles, token), HttpStatus.OK);
        }
        catch (BadCredentialsException  e){
            throw new UnauthorizedException("Неверно указан пароль!");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users")
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users/{username}")
    public User findUserByUsername(@PathVariable String username){
        return userService.findByUsername(username).orElseThrow(()-> {
            throw new NotFoundException("Пользователь с именем: " + username + " не обнаружен!");
        });
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/registration")
    public void registerUser (@Valid @RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isEmpty()){
            if (user.getPassword().equals(user.getConfirmPassword())) {
                userService.save(user);
            }
            else throw new BadFormException("Пароль и проверочный пароль не совпадают!");
        }
        else throw new BadFormException("Пользователь с именем: " + user.getUsername() + " уже существует");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> resetPassword(HttpServletRequest request,
                               @RequestParam("email") String userEmail) {
        User user = userService.findByEmail(userEmail).orElseThrow(()-> {
            throw new BadFormException("Пользователь с таким е-майл не найден!");
        });
        String url = request.getScheme() + "://" + request.getServerName() + ":"
                                            + request.getServerPort() + "/auth";
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiryDate(30);
        resetPasswordService.save(resetToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("wild.bill.java.man@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Запрос на сброс пароля");
        mailMessage.setText("Для подтверждения смены пароля пройдите по ссылке: \n"
                + url + "/reset-password?token=" + resetToken.getToken());
        javaMailSender.send(mailMessage);
        Map<String, String> map = new HashMap<>();
        map.put("successMessage", "Ссылка для сброса пароля была отправлена на е-майл " + userEmail);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    /*
    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public ResponseEntity<Map<Object, Object>> find(HttpServletResponse httpServletResponse) {
        Map<Object, Object> map = new HashMap<>();
      //  map.put("message", "goBack!");


        map.put("Location", "http://stackoverflow.com");
        httpServletResponse.setHeader("Location", "http://localhost:4200/login");
        httpServletResponse.setStatus(302);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

     */


    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public void find( RedirectAttributes redirectAttributes,
            HttpServletResponse httpServletResponse) {
            redirectAttributes.addAttribute("message", "goBack!");
        httpServletResponse.setHeader("Location", "http://localhost:4200/login");
        httpServletResponse.setStatus(302);
    }



  /*  @Transactional
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPasswordMessage(@RequestParam("token") String userToken) {

        Map<String, String> map = new HashMap<>();
        PasswordResetToken resetToken = resetPasswordService.findByToken(userToken).orElseThrow(()-> {
            throw new BadFormException("Токен не обнаружен!");
        });
        if (resetToken.isExpired()){
            map.put("error", "Срок действия токена истек, пожалуйста, снова запросите новый пароль.");
            resetPasswordService.deleteByToken(userToken);
        }
        else {
            map.put("token", resetToken.getToken());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

   */

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/reset-password")
    public String resetPasswordMessage(@RequestParam("token") String userToken) {

        Map<String, String> map = new HashMap<>();
        PasswordResetToken resetToken = resetPasswordService.findByToken(userToken).orElseThrow(()-> {
            throw new BadFormException("Токен не обнаружен!");
        });
        if (resetToken.isExpired()){
            map.put("error", "Срок действия токена истек, пожалуйста, снова запросите новый пароль.");
            resetPasswordService.deleteByToken(userToken);
        }
        else {
            map.put("token", resetToken.getToken());
        }
        return "http://stackoverflow.com";
    }




    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetForgottenPassword(@Valid PasswordResetDto passwordReset) {
        PasswordResetToken token = resetPasswordService.findByToken(passwordReset.getToken()).orElseThrow(()->{
            throw new NotFoundException("Токен не обнаружен!");
        });
        Map<String, String> map = new HashMap<>();
        User user = token.getUser();
        if (user != null) {
            user.setPassword(passwordReset.getPassword());
            user.setConfirmPassword(passwordReset.getConfirmPassword());
            userService.save(user);
            resetPasswordService.deleteByToken(passwordReset.getToken());
            map.put("message", "Пароль и проверочный пароль успешно изменены!");
        }
        else {
            map.put("error", "Ссылка неверна или не работае! Попробуйте снова повторить сброс пароля.");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/changing-password")
    public User changePassword(@RequestBody PasswordChangingUser user){
        String username = user.getUsername();
        String oldPassword = user.getPassword();
        String newPassword = user.getNewPassword();
        String newConfirmPassword = user.getNewConfirmPassword();
        if (newPassword.equals(newConfirmPassword)){
            return userService.changePassword(username, oldPassword, newPassword, newConfirmPassword);
        }
        else {
            throw new BadFormException("Новый пароль и проверочный пароль не совпадают!");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/users/{username}")
    public User updateUser(@PathVariable  String username,
                           @Valid @RequestBody User user){
        return userService.findByUsername(username).map(newUser -> {
            newUser.setUsername(user.getUsername());
            newUser.setEmail(user.getEmail());
            newUser.setBirthDate(user.getBirthDate());
            newUser.setGender(user.getGender());
            newUser.setRoles(user.getRoles());
            return userService.save(newUser);
        }).orElseThrow(()->{
            throw new NotFoundException("Пользователь с именем: " + username + " не обнаружен!");
        });
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value =  "/users/{username}")
    public void deleteUser(@PathVariable String username) {
       userService.deleteByUsername(username);
    }
}