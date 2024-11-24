package com.maalx_back.repository;

import com.maalx_back.entity.ConsultingResponse;
import com.maalx_back.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Survey findByConsultingResponse(ConsultingResponse previousResponse);
}
