package com.maalx_back.repository;

import com.maalx_back.entity.UserAdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdditionalInfoRepository extends JpaRepository<UserAdditionalInfo, Long> {

    // 특정 사용자 ID로 추가 정보를 조회하는 메서드
    UserAdditionalInfo findByUserUserId(Long userId);
}
