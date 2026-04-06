package net.softloaf.automatchic.app.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.JwtResponse;
import net.softloaf.automatchic.app.dto.UserUpdateRequest;
import net.softloaf.automatchic.app.security.JwtUtils;
import net.softloaf.automatchic.app.security.UserDetailsImpl;
import net.softloaf.automatchic.app.security.UserDetailsServiceImpl;
import net.softloaf.automatchic.app.service.SessionService;
import net.softloaf.automatchic.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final SessionService sessionService;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/self")
    public ResponseEntity<?> deleteSelf() {
        userService.deleteUser(sessionService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update/self")
    public ResponseEntity<?> updateSelf(@RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(userUpdateRequest);
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(sessionService.getCurrentUserUsername());
        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/check/self")
    public boolean checkEnabledSelf() {
        return userService.checkEnabled(sessionService.getCurrentUserId());
    }
}
