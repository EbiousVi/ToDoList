package com.example.ace.controller;

import com.example.ace.domain.dto.CaptchaResponseDto;
import com.example.ace.domain.dto.UserDto;
import com.example.ace.repository.UserRepository;
import com.example.ace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private static final String captchaURL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    public RegController(UserService userService, UserRepository userRepository, RestTemplate restTemplate) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/registration")
    public String registerNewUser(@ModelAttribute("user") @Valid UserDto userDto,
                                  @RequestParam("g-recaptcha-response") String captchaResponse,
                                  BindingResult bindingResult,
                                  Model model) {
        String url = String.format(captchaURL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        assert response != null;
        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
            model.addAttribute("type", "danger");
        }

        System.out.println(response.isSuccess());
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "", "Such User already exists");
        }

        if (userDto.getEmail().length() == 0) {
            bindingResult.rejectValue("email", "", "Enter mail");
        }

        if (userDto.getPassword().length() <= 2) {
            bindingResult.rejectValue("password", "", "Password is too short");
        }

        if (!response.isSuccess() || bindingResult.hasErrors()) {
            return "registration";
        }
        userService.createNewUser(userDto);
        return ("redirect:/login");
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UserDto());
        return "registration";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = false;
        try {
            isActivated = userService.activateUser(code);
        } catch (Exception ignored) {
        }
        if (isActivated) {
            model.addAttribute("activate", "User successfully activated");
            model.addAttribute("type", "success");
        } else {
            model.addAttribute("activate", "Activation code is not found!");
            model.addAttribute("type", "danger");
        }
        return "login";
    }
}
