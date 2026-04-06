package project.finanacedashboard.Controller.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.finanacedashboard.DTO.Request.LoginRequest;
import project.finanacedashboard.DTO.Request.UserRequest;
import project.finanacedashboard.DTO.Response.AuthResponse;
import project.finanacedashboard.DTO.Response.UserResponse;
import project.finanacedashboard.Services.CustomUserDetails;
import project.finanacedashboard.Services.UserService;
import project.finanacedashboard.Config.JwtProvider;

@RestController
@RequestMapping("/auth")
public class Auth {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public Auth(UserService userService, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    public UserResponse signup(@RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return new AuthResponse(
                jwt,
                "Login Success",
                userDetails.getUsername(),
                userDetails.getUser().getRole().getRole()
        );
    }
}
