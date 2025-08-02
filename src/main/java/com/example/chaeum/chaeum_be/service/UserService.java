package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.code.ResponseCode;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.dto.response.ResponseDTO;
import com.example.chaeum.chaeum_be.dto.user.RegisterDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity
                    .status(ErrorCode.PASSWORD_MISMATCH.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.PASSWORD_MISMATCH, null));
        }

        if (userRepository.existsByPhoneNum(dto.getPhoneNum())) {
            return ResponseEntity
                    .status(ErrorCode.DUPLICATE_PHONENUM.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.DUPLICATE_PHONENUM, null));
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity
                    .status(ErrorCode.DUPLICATE_EMAIL.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.DUPLICATE_EMAIL, null));
        }

        User user = User.builder()
                .name(dto.getName())
                .phoneNum(dto.getPhoneNum())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        userRepository.save(user);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_REGISTER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_REGISTER, null));
    }
}

