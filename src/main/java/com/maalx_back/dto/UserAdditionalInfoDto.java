package com.maalx_back.dto;

import com.maalx_back.entity.UserAdditionalInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UserAdditionalInfoDto {
    private String companyName;
    private String industry;
    private double revenue;

    private String painPoint;
    private String detailedIssue;
    private String consultingField;
    private String aiNeeds;
    private String detailedDemand;
    private LocalDate date;  // 등록 날짜를 저장할 필드

    // UserAdditionalInfo 엔티티에서 필요한 필드를 가져오는 생성자
    public UserAdditionalInfoDto(UserAdditionalInfo additionalInfo) {
        this.companyName = additionalInfo.getCompanyName();
        this.industry = additionalInfo.getIndustry();
        this.revenue = additionalInfo.getRevenue();
        this.painPoint = additionalInfo.getPainPoint();
        this.detailedIssue = additionalInfo.getDetailedIssue();
        this.consultingField = additionalInfo.getPainPoint();
        this.aiNeeds = additionalInfo.getAiNeeds();
        this.detailedDemand=additionalInfo.getDetailedDemand();
        this.date=additionalInfo.getDate();
    }
}
