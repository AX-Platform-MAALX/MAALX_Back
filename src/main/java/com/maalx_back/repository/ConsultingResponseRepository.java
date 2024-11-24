package com.maalx_back.repository;

import com.maalx_back.entity.ConsultingResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultingResponseRepository extends JpaRepository<ConsultingResponse, Long> {
    // 사용자별로 응답 조회 메서드 추가 가능
    int countByUser_UserId(Long userId);
    List<ConsultingResponse> findByUser_UserId(Long userId);

    ConsultingResponse findByUser_UserIdAndConsultingIndex(Long userId, int previousIndex);
}
