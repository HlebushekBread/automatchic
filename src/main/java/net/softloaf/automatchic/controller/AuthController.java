package net.softloaf.automatchic.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.dto.JwtRequest;
import net.softloaf.automatchic.dto.JwtResponse;
import net.softloaf.automatchic.dto.NewUserDto;
import net.softloaf.automatchic.security.JwtUtils;
import net.softloaf.automatchic.security.UserDetailsImpl;
import net.softloaf.automatchic.security.UserDetailsServiceImpl;
import net.softloaf.automatchic.service.UserService;
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

        UserDetailsImpl userDetails;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newUserDto.getUsername(), newUserDto.getPassword()));
            userDetails = userDetailsService.loadUserByUsername(newUserDto.getUsername());
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect credentials", HttpStatus.UNAUTHORIZED);
        }
        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
