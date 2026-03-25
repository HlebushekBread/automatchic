package net.breadlab.automatchic.controller;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
}
