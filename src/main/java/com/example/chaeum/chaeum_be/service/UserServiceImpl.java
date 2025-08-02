package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.code.ResponseCode;
import com.example.chaeum.chaeum_be.dto.response.ErrorResponseDTO;
import com.example.chaeum.chaeum_be.dto.response.ResponseDTO;
import com.example.chaeum.chaeum_be.dto.user.LoginRequestDTO;
import com.example.chaeum.chaeum_be.dto.user.OnboardingRequestDTO;
import com.example.chaeum.chaeum_be.dto.user.RegisterDTO;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.entity.UserPreference;
import com.example.chaeum.chaeum_be.enums.PurposeType;
import com.example.chaeum.chaeum_be.exception.GlobalException;
import com.example.chaeum.chaeum_be.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    @Override
    public ResponseEntity<?> register(RegisterDTO dto) {
        String password = dto.getPassword();
        String pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$";

        if(!password.matches(pattern)) {
            return ResponseEntity
                    .status(ErrorCode.INVALID_PASSWORD_FORMAT.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.INVALID_PASSWORD_FORMAT, null));

        }

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
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_REGISTER, dto));
    }

    // 로그인
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> login(LoginRequestDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if(!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity
                    .status(ErrorCode.PASSWORD_NOT_CORRECT.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.PASSWORD_NOT_CORRECT, null));
        }

        return ResponseEntity
                .status(ResponseCode.SUCCESS_LOGIN.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_LOGIN, dto));
    }

    // 온보딩
    @Override
    public ResponseEntity<?> onboarding(OnboardingRequestDTO dto) {
        User user = getLoggedInUser(dto);

        if(!user.isFirstLogin()) {
            return ResponseEntity
                    .status(ErrorCode.ALREADY_ONBOARDED.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.ALREADY_ONBOARDED, null));
        }

        UserPreference preference = new UserPreference();
        preference.setUser(user);
        preference.setPurpose(dto.getPurpose());

        if ((dto.getPurpose() == PurposeType.BUY || dto.getPurpose() == PurposeType.BOTH)
                && (dto.getUsagePurpose() == null || dto.getUsagePurpose().isEmpty())) {
            return ResponseEntity
                    .status(ErrorCode.USAGE_PURPOSE_REQUIRED_FOR_BUYERS.getStatus().value())
                    .body(new ErrorResponseDTO(ErrorCode.USAGE_PURPOSE_REQUIRED_FOR_BUYERS, null));
        }

        preference.setUsagePurpose(dto.getUsagePurpose());
        preference.setAdditionalDetail(dto.getAdditionalDetail());

        user.setUserPreference(preference);

        user.setFirstLogin(false);
        userRepository.save(user);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_ONBOARDING.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_ONBOARDING, dto));
    }

    private User getLoggedInUser(OnboardingRequestDTO dto) {
        String email = dto.getEmail();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }
}
