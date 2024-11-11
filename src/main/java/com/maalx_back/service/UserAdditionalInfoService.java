package com.maalx_back.service;

import com.maalx_back.dto.UserAdditionalInfoDto;
import com.maalx_back.entity.User;
import com.maalx_back.entity.UserAdditionalInfo;
import com.maalx_back.repository.UserAdditionalInfoRepository;
import com.maalx_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAdditionalInfoService {

    @Autowired
    private UserAdditionalInfoRepository additionalInfoRepository;

    @Autowired
    private UserRepository userRepository;

    public UserAdditionalInfo addOrUpdateAdditionalInfo(Long userId, UserAdditionalInfoDto additionalInfoDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        UserAdditionalInfo additionalInfo = additionalInfoRepository.findByUserUserId(userId);
        if (additionalInfo == null) {
            additionalInfo = new UserAdditionalInfo();
            additionalInfo.setUser(user);
        }

        // 추가 정보 업데이트
        additionalInfo.setCompanyName(additionalInfoDto.getCompanyName());
        additionalInfo.setRevenue(additionalInfoDto.getRevenue());
        additionalInfo.setTechnologyField(additionalInfoDto.getTechnologyField());
        additionalInfo.setEmployeeCount(additionalInfoDto.getEmployeeCount());
        additionalInfo.setMarketCap(additionalInfoDto.getMarketCap());

        additionalInfo.setConsultingInterest(additionalInfoDto.getConsultingInterest());
        additionalInfo.setPainPoint(additionalInfoDto.getPainPoint());
        additionalInfo.setPreferredAITech(additionalInfoDto.getPreferredAITech());

        return additionalInfoRepository.save(additionalInfo);
    }

    // User ID로 추가 정보 조회 및 DTO 변환 후 반환
    public UserAdditionalInfoDto getAdditionalInfo(Long userId) {
        UserAdditionalInfo additionalInfo = additionalInfoRepository.findByUserUserId(userId);
        if (additionalInfo == null) {
            return null;
        }
        // UserAdditionalInfo 엔티티를 AdditionalInfoResponseDto로 변환
        return new UserAdditionalInfoDto(additionalInfo);
    }
}
