package com.letsmove.controller;

import com.letsmove.dto.NewPasswordUser;
import com.letsmove.entity.Token;
import com.letsmove.entity.Users;
import com.letsmove.service.EmailSenderService;
import com.letsmove.service.TokenService;
import com.letsmove.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String login() {
        return "rev";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage(@RequestParam(value = "error", required = false) String error,
                                  @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Почта или пароль неверны");
            model.setViewName("/login");
        }
        if (logout != null) {
            model.addObject("logout", "Logged out successfully.");
            model.setViewName("/login");
        }
        return model;
    }

    @RequestMapping(value = "/userMain", method = RequestMethod.GET)
    public String hello() {
        return "userMain";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("registration");
        modelAndView.addObject("user", new Users());
        return modelAndView;
    }


    @PostMapping(value = "/registration")
    public String registration(@ModelAttribute(name = "user") Users user) {
        try {
            ModelAndView model = new ModelAndView();
            this.userService.save(user);
            return "login";
        } catch (Exception e) {
            return "registration";
        }
    }

    @RequestMapping(value = "/adminMain", method = RequestMethod.GET)
    public String adminMain() {
        return "adminMain";
    }

    @GetMapping(value = "/forgotPassword")
    public String resetPasswordPage() {
        return "forgotPassword";
    }

    @PostMapping(value = "/passwordRecoveryEmail")
    public ModelAndView getEmailForResetPassword(@RequestParam String login) throws MessagingException {
        ModelAndView modelAndView = new ModelAndView("changePassword");
        Users saved = userService.findByLogin(login);
        Token token = tokenService.saveToken(saved, tokenService.makeToken());
        emailSenderService.sendEmail(saved.getEmail(), "Введите данный токен, чтобы сбросить ваш пароль: " + String.valueOf(token.getToken()), "Восстановление пароля");
        NewPasswordUser newPasswordUser = new NewPasswordUser();
        newPasswordUser.setEmail(saved.getEmail());
        modelAndView.addObject("reset", newPasswordUser);
        return modelAndView;
    }

    @PostMapping(value = "/newPasswordUser")
    public String newPassword(@ModelAttribute(name = "reset") NewPasswordUser newPasswordUser) {
        try {
            Users users = userService.findByEmailUser(newPasswordUser.getEmail());
            Token byUserAndToken = tokenService.findByUserAndToken(users, newPasswordUser.getToken());
            if (newPasswordUser.getPassword().equals(newPasswordUser.getRepeatPassword())) {
                users.setPassword(bCryptPasswordEncoder.encode(newPasswordUser.getPassword()));
                userService.update(users);
                tokenService.deleteToken(byUserAndToken);
                return "login";
            } else {
                return "changePassword";
            }
        } catch (Exception e) {
            return "changePassword";
        }
    }
}
