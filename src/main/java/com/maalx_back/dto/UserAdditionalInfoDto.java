package com.maalx_back.dto;

import com.maalx_back.entity.UserAdditionalInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserAdditionalInfoDto {
    private String companyName;
    private double revenue;
    private String technologyField;
    private int employeeCount;
    private double marketCap;

    private String consultingInterest;
    private String painPoint;
    private String preferredAITech;

    // UserAdditionalInfo 엔티티에서 필요한 필드를 가져오는 생성자
    public UserAdditionalInfoDto(UserAdditionalInfo additionalInfo) {
        this.companyName = additionalInfo.getCompanyName();
        this.revenue = additionalInfo.getRevenue();
        this.technologyField = additionalInfo.getTechnologyField();
        this.employeeCount = additionalInfo.getEmployeeCount();
        this.marketCap = additionalInfo.getMarketCap();
        this.consultingInterest = additionalInfo.getConsultingInterest();
        this.painPoint = additionalInfo.getPainPoint();
        this.preferredAITech = additionalInfo.getPreferredAITech();
    }
}
