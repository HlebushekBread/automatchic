package net.softloaf.automatchic.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.dto.JwtResponse;
import net.softloaf.automatchic.dto.UserUpdateRequest;
import net.softloaf.automatchic.security.JwtUtils;
import net.softloaf.automatchic.security.UserDetailsImpl;
import net.softloaf.automatchic.security.UserDetailsServiceImpl;
import net.softloaf.automatchic.service.SessionService;
import net.softloaf.automatchic.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final SessionService sessionService;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser() {
        this.userService.deleteUser(sessionService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/self")
    public ResponseEntity<?> deleteSelf() {
        this.userService.deleteUser(sessionService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update/self")
    public ResponseEntity<?> updateSelf(@RequestBody UserUpdateRequest userUpdateRequest) {
        this.userService.updateUser(userUpdateRequest);
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(sessionService.getCurrentUserUsername());
        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
