package com.avab.avab.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Place;
import com.avab.avab.dto.response.RecreationResponseDTO.PopularRecreationListDTO;

public interface RecreationService {

    List<PopularRecreationListDTO> getTop3RecreationsByViewCount();

    Page<Recreation> searchRecreations(
            User user,
            String searchKeyword,
            Keyword keyword,
            Integer participants,
            Integer playTime,
            Place place,
            Gender gender,
            Age age,
            Integer page);
}
