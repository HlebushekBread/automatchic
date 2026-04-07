package net.softloaf.automatchic.app.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.JwtRequest;
import net.softloaf.automatchic.app.dto.JwtResponse;
import net.softloaf.automatchic.app.dto.NewUserRequest;
import net.softloaf.automatchic.app.security.JwtUtils;
import net.softloaf.automatchic.app.security.UserDetailsImpl;
import net.softloaf.automatchic.app.security.UserDetailsServiceImpl;
import net.softloaf.automatchic.app.service.util.SessionService;
import net.softloaf.automatchic.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final SessionService sessionService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        UserDetailsImpl userDetails;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверные данные пользователя");
        }
        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveNewUser(@RequestBody NewUserRequest newUserRequest) {
        userService.saveNewUser(newUserRequest);

        UserDetailsImpl userDetails;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newUserRequest.getUsername(), newUserRequest.getPassword()));
            userDetails = userDetailsService.loadUserByUsername(newUserRequest.getUsername());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверные данные пользователя");
        }
        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/confirm/{token}")
    public ResponseEntity<?> confirmEmail(@PathVariable String token) {
        userService.confirmUser(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resend")
    public ResponseEntity<?> resendConfirm() {
        userService.sendConfirmationEmail(sessionService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}
