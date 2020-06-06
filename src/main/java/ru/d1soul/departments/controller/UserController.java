package ru.d1soul.departments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/departments-app/auth")
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
    @PreAuthorize("hasRole('ANONYMOUS')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users")
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(value = "/users/{username}")
    public User findUserByUsername(@PathVariable String username){
        return userService.findByUsername(username).orElseThrow(()-> {
            throw new NotFoundException("Пользователь с именем: " + username + " не обнаружен!");
        });
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ANONYMOUS')")
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

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping(value = "/users/{username}")
    public User updateUser(@PathVariable  String username,
                           @Valid @RequestBody User user){
        return userService.findByUsername(username).map(newUser -> {
            newUser.setUsername(user.getUsername());
            newUser.setEmail(user.getEmail());
            newUser.setBirthDate(user.getBirthDate());
            newUser.setGender(user.getGender());
            newUser.setIsBanned(user.getIsBanned());
            newUser.setRoles(user.getRoles());
            return userService.save(newUser);
        }).orElseThrow(()->{
            throw new NotFoundException("Пользователь с именем: " + username + " не обнаружен!");
        });
    }

    @PostMapping("/forgot-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Map<String, String>> resetPassword(HttpServletRequest request,
                               @RequestParam("email") String userEmail) {
        User user = userService.findByEmail(userEmail).orElseThrow(()-> {
            throw new BadFormException("Пользователь с таким е-майл не найден!");
        });
        String url = request.getScheme() + "://" + request.getServerName() + ":"
                                            + request.getServerPort() + "/departments-app/auth";
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiryDate(30);
        resetPasswordService.save(resetToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("wild.bill.java.man@gmail.com");
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Запрос на восстановление пароля");
        mailMessage.setText("Здравствуйте, " + user.getUsername() + "! \n"
                +  "Для подтверждения смены пароля пройдите по ссылке: \n"
                + url + "/reset-password?token=" + resetToken.getToken());
        javaMailSender.send(mailMessage);
        Map<String, String> map = new HashMap<>();
        map.put("successMessage", "Ссылка для сброса пароля была отправлена на е-майл " + userEmail);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @GetMapping("/reset-password")
    @PreAuthorize("permitAll()")
    public ModelAndView resetPasswordMessage(@RequestParam("token") String userToken,  ModelMap modelMap) {
       ModelAndView modelAndView = new ModelAndView();
       resetPasswordService.findByToken(userToken).ifPresentOrElse(token-> {
           if (token.isExpired()){
               modelMap.addAttribute("error",
                       "Срок действия токена истек, пожалуйста, снова запросите новый пароль.");
               resetPasswordService.deleteByToken(token.getToken());
           }
           else {
                modelMap.addAttribute("token", token.getToken());
           }
       },
       ()-> modelMap.addAttribute("error", "Токен не обнаружен!"));
       modelAndView.setViewName("redirect:" + "http://localhost:4200/reset-password");
       modelAndView.addAllObjects(modelMap);
       modelAndView.setStatus(HttpStatus.OK);
       return modelAndView;
    }

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("permitAll()")
    @PutMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetForgottenPassword(@Valid @RequestBody  PasswordResetDto passwordReset) {
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
            map.put("error", "Ссылка неверна или не работает! Попробуйте снова повторить сброс пароля.");
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping(value =  "/users/{username}")
    public void deleteUser(@PathVariable String username) {
       userService.deleteByUsername(username);
    }
}