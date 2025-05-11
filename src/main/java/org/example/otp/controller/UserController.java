package org.example.otp.controller;

import org.example.otp.request.LoginRequest;
import org.example.otp.request.NewOtpRequest;
import org.example.otp.request.OtpRequest;
import org.example.otp.request.SignupRequest;
import org.example.otp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("otp")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest signupRequest) {

        return userService.Signup(signupRequest);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/verify")
   public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest otpRequest) {
        return ResponseEntity.ok(userService.verifyOtp(
                otpRequest.getEmail(),
                otpRequest.getOtp()
        ));
    }

    @PostMapping("/new-otp")
    public  String requestOtp(@RequestBody NewOtpRequest newOtpRequest) {
        return userService.requestOtp(newOtpRequest);
    }

}
