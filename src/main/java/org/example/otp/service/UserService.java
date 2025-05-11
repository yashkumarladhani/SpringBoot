package org.example.otp.service;

import org.example.otp.model.Role;
import org.example.otp.model.User;
import org.example.otp.repository.UserRepository;
import org.example.otp.request.LoginRequest;
import org.example.otp.request.NewOtpRequest;
import org.example.otp.request.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    public  String Signup(SignupRequest signupRequest) {

        String email = signupRequest.getEmail().trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            return "Email Already Exists";
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(email);
        user.setPassword(signupRequest.getPassword());
        user.setRole(Role.USER);
        user.setVerified(false);


        String otp = String.format("%06d",new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiryTime(LocalDateTime.now());
        userRepository.save(user);
        emailService.sendEmail(user.getEmail(),otp);
        return "Otp sent to your email. please verified your OTP";
    }

    public String verifyOtp(String email, String otp) {
        email = email.trim().toLowerCase();
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()) {
            return "Email Not Found";
        }

        User u = user.get();

        if (u.getOtpExpiryTime().plusMinutes(2L).isBefore(LocalDateTime.now()))
            return "Otp Has Expired, please Request a new one";
        if(u.getOtp().equals(otp)) {
            u.setVerified(true);
            u.setOtp(null);
            userRepository.save(u);
            return "Otp verified";
        }else {
            return "Wrong OTP";
        }
    }



    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return "Invalid username or password";
        }

        if (user.getRole() == Role.ADMIN) {
            if (!user.isVerified()) {
                return "Please verify your OTP";
            }
            return  "Welcome Admin Dashboard";
        }
            if (!user.isVerified()) {
                return "Please verify your OTP";
            }
        return "Welcome User Dashboard";
    }

    public String requestOtp(NewOtpRequest newOtpRequest) {
        String email = newOtpRequest.getEmail().trim().toLowerCase();
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            return "Email Not Found";
        }

        User u = user.get();
        String newOtp = String.format("%06d",new Random().nextInt(999999));
        u.setOtp(newOtp);
        u.setOtpExpiryTime(LocalDateTime.now());
        userRepository.save(u);
        emailService.sendEmail(u.getEmail(),newOtp);
        return "New OTP has been sent to your email.";
    }
}
