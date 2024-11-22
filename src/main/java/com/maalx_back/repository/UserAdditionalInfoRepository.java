package com.maalx_back.repository;

import com.maalx_back.entity.UserAdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAdditionalInfoRepository extends JpaRepository<UserAdditionalInfo, Long> {

    UserAdditionalInfo findByUserUserId(Long userId);
}
