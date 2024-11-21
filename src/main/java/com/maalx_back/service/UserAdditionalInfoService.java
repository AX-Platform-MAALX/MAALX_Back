package com.maalx_back.service;

import com.maalx_back.dto.UserAdditionalInfoDto;
import com.maalx_back.entity.User;
import com.maalx_back.entity.UserAdditionalInfo;
import com.maalx_back.repository.UserAdditionalInfoRepository;
import com.maalx_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAdditionalInfoService {

    @Autowired
    private UserAdditionalInfoRepository additionalInfoRepository;

    @Autowired
    private UserRepository userRepository;

    public UserAdditionalInfo addOrUpdateAdditionalInfo(Long userId, UserAdditionalInfoDto additionalInfoDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        UserAdditionalInfo additionalInfo = new UserAdditionalInfo();
        additionalInfo.setUser(user);
        // 추가 정보 업데이트
        additionalInfo.setCompanyName(additionalInfoDto.getCompanyName());
        additionalInfo.setIndustry(additionalInfoDto.getIndustry());
        additionalInfo.setRevenue(additionalInfoDto.getRevenue());
        additionalInfo.setPainPoint(additionalInfoDto.getPainPoint());
        additionalInfo.setDetailedIssue(additionalInfoDto.getDetailedIssue());
        additionalInfo.setConsultingField(additionalInfoDto.getConsultingField());
        additionalInfo.setAiNeeds(additionalInfoDto.getAiNeeds());
        additionalInfo.setDetailedDemand(additionalInfoDto.getDetailedDemand());
        additionalInfo.setDate(additionalInfoDto.getDate());
        return additionalInfoRepository.save(additionalInfo);
    }

    // User ID로 추가 정보 전체 조회 및 DTO 변환 후 반환
    public List<UserAdditionalInfoDto> getAdditionalInfos(Long userId) {
        List<UserAdditionalInfo> additionalInfos = additionalInfoRepository.findAllByUserUserId(userId);

        if (additionalInfos.isEmpty()) {
            return Collections.emptyList();
        }

        // 엔티티 리스트를 DTO 리스트로 변환
        return additionalInfos.stream()
                .map(UserAdditionalInfoDto::new)
                .collect(Collectors.toList());
    }
    public UserAdditionalInfoDto getAdditionalInfo(Long userId) {
        UserAdditionalInfo additionalInfo = additionalInfoRepository.findOneByUserUserId(userId);
        if (additionalInfo == null) {
            return null;
        }
        // UserAdditionalInfo 엔티티를 AdditionalInfoResponseDto로 변환
        return new UserAdditionalInfoDto(additionalInfo);
    }
    // userId로 UserAdditionalInfo의 총 개수 조회
    public Long getAdditionalInfoCount(Long userId) {
        return additionalInfoRepository.countByUserUserId(userId);
    }
}
