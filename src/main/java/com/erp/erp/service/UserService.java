package com.erp.erp.service;

import com.erp.erp.model.User;
import com.erp.erp.repository.SystemSettingRepository;
import com.erp.erp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SystemSettingRepository systemSettingRepository;

    public UserService(UserRepository userRepository, SystemSettingRepository systemSettingRepository) {
        this.userRepository = userRepository;
        this.systemSettingRepository = systemSettingRepository;
    }

    public User getSupervisor(User user) {
        if (user.getSupervisor() == null) {
            throw new RuntimeException("User " + user.getUsername() + " has no supervisor assigned");
        }
        return user.getSupervisor();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setFullName(user.getFullName());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setRole(user.getRole());
                    existingUser.setOrganization(user.getOrganization());
                    existingUser.setSupervisor(user.getSupervisor());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameOrEmail(username, username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String generateOtp(User user) {
        // Generate 6 digit random OTP
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);

        // Get OTP Expiry from System Settings (default 5 minutes)
        int expiryMinutes = systemSettingRepository.findByKey("otp_expiry_minutes")
                .map(s -> Integer.parseInt(s.getValue()))
                .orElse(5);

        user.setResetPasswordOtp(otp);
        user.setResetPasswordOtpExpiry(LocalDateTime.now().plusMinutes(expiryMinutes));
        userRepository.save(user);

        return otp;
    }

    public String resendOtp(User user) {
        // We can reuse generateOtp but might add logic to prevent resending too fast
        // For now, generating a fresh one is standard
        return generateOtp(user);
    }

    public boolean verifyOtp(User user, String otp) {
        if (user.getResetPasswordOtp() == null || user.getResetPasswordOtpExpiry() == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(user.getResetPasswordOtpExpiry())) {
            // OTP expired
            return false;
        }

        return user.getResetPasswordOtp().equals(otp);
    }

    public void updatePassword(User user, String encodedPassword) {
        user.setPassword(encodedPassword);
        // Clear OTP after successful reset
        user.setResetPasswordOtp(null);
        user.setResetPasswordOtpExpiry(null);
        userRepository.save(user);
    }
}
