package com.example.demo.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OTPService {
    private final Map<String, String> otpStorage = new HashMap<>();

    public boolean validateOtp(Long courseId, Long lessonId, String otp) {
        String key = courseId + "-" + lessonId;
        String storedOtp = otpStorage.get(key);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public void generateOtp(Long courseId, Long lessonId, String otp) {
        otpStorage.put(courseId + "-" + lessonId, otp);
    }
}
