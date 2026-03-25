package net.breadlab.automatchic.controller;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.dto.JwtRequest;
import net.breadlab.automatchic.dto.JwtResponse;
import net.breadlab.automatchic.dto.NewUserDto;
import net.breadlab.automatchic.security.JwtUtils;
import net.breadlab.automatchic.security.UserDetailsImpl;
import net.breadlab.automatchic.security.UserDetailsServiceImpl;
import net.breadlab.automatchic.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
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
            return new ResponseEntity<>("Incorrect credentials", HttpStatus.UNAUTHORIZED);
        }
        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveNewUser(@RequestBody NewUserDto newUserDto) {
        userService.saveNewUser(newUserDto);
        return ResponseEntity.noContent().build();
    }
}
