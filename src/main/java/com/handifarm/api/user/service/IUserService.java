package com.handifarm.api.user.service;

import com.handifarm.api.user.dto.request.UserJoinRequestDTO;
import com.handifarm.api.user.dto.request.UserLoginRequestDTO;
import com.handifarm.api.user.dto.response.UserLoginResponseDTO;
import com.handifarm.api.user.entity.User;
import com.handifarm.jwt.TokenUserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {

    // id 중복체크
    boolean idDuplicateCheck(final String userId);

    // 회원가입 처리
    void join(final UserJoinRequestDTO dto) throws Exception;

    // 로그인 처리 및 토큰 발급
    UserLoginResponseDTO authenticate(final UserLoginRequestDTO dto);

    // 프로필 이미지 등록
    void uploadUserProfile(TokenUserInfo userInfo, MultipartFile profileImg) throws Exception;

    // 프로필 이미지 링크 리턴
    String getUserProfile(final String userId);

}
