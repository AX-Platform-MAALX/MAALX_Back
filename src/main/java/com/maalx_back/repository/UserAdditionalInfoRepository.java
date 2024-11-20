package com.maalx_back.repository;

import com.maalx_back.entity.UserAdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAdditionalInfoRepository extends JpaRepository<UserAdditionalInfo, Long> {

    // 특정 사용자 ID로 추가 정보를 조회하는 메서드
    UserAdditionalInfo findOneByUserUserId(Long userId);
    // 특정 사용자에 대한 모든 추가 정보 반환
    List<UserAdditionalInfo> findAllByUserUserId(Long userId);

    Long countByUserUserId(Long userId);
}
