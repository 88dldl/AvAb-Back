package com.avab.avab.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.FlowAge;
import com.avab.avab.domain.FlowGender;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.RecreationKeyword;
import com.avab.avab.domain.RecreationPurpose;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.domain.mapping.FlowFavorite;
import com.avab.avab.domain.mapping.FlowRecreation;
import com.avab.avab.domain.mapping.FlowRecreationKeyword;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowDetailDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowPreviewPageDTO;
import com.avab.avab.dto.response.FlowResponseDTO.FlowScrapDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationPreviewDTO;
import com.avab.avab.dto.response.RecreationResponseDTO.RecreationReviewDTO.AuthorDTO;

@Component
public class FlowConverter {

    public static FlowPreviewPageDTO toFlowPreviewPageDTO(Page<Flow> flowPage, User user) {
        List<FlowPreviewDTO> flowPreviewDTOList =
                flowPage.stream().map(flow -> toFlowPreviewDTO(flow, user)).toList();

        return FlowPreviewPageDTO.builder()
                .flowList(flowPreviewDTOList)
                .totalPages(flowPage.getTotalPages())
                .build();
    }

    public static FlowPreviewDTO toFlowPreviewDTO(Flow flow, User user) {
        User author = flow.getAuthor();

        Boolean isScraped =
                user != null
                        ? flow.getFlowFavoriteList().stream()
                                .anyMatch((flowFavorite -> flowFavorite.getUser().equals(user)))
                        : null;

        List<Purpose> purposeList =
                flow.getFlowRecreationPurposeList().stream()
                        .map(FlowRecreationPurpose::getPurpose)
                        .map(RecreationPurpose::getPurpose)
                        .toList();

        return FlowPreviewDTO.builder()
                .id(flow.getId())
                .title(flow.getTitle())
                .author(
                        AuthorDTO.builder()
                                .username(author.getUsername())
                                .userId(author.getId())
                                .build())
                .purpose(purposeList)
                .isScraped(isScraped)
                .totalPlayTime(flow.getTotalPlayTime())
                .scrapCount(flow.getScrapCount())
                .viewCount(flow.getViewCount())
                .build();
    }

    public static Flow toFlow(
            PostFlowDTO postFlowDTO,
            User user,
            List<Recreation> recreationList,
            List<RecreationKeyword> recreationKeywordList,
            List<RecreationPurpose> recreationPurposeList) {

        Flow flow =
                Flow.builder()
                        .participants(postFlowDTO.getParticipants())
                        .totalPlayTime(postFlowDTO.getTotalPlayTime())
                        .title(postFlowDTO.getTitle())
                        .author(user)
                        .build();

        List<FlowRecreation> flowRecreationList =
                postFlowDTO.getRecreationSpecList().stream()
                        .flatMap(
                                recreationSpec ->
                                        recreationList.stream()
                                                .filter(
                                                        recreation ->
                                                                recreation
                                                                        .getId()
                                                                        .equals(
                                                                                recreationSpec
                                                                                        .getRecreationId()))
                                                .map(
                                                        recreation ->
                                                                FlowRecreation.builder()
                                                                        .flow(flow)
                                                                        .recreation(recreation)
                                                                        .customPlayTime(
                                                                                recreationSpec
                                                                                        .getCustomPlayTime())
                                                                        .seq(
                                                                                recreationSpec
                                                                                        .getSeq())
                                                                        .build()))
                        .toList();

        List<FlowRecreationKeyword> flowRecreationKeywordList =
                recreationKeywordList.stream()
                        .map(
                                recreationKeyword ->
                                        FlowRecreationKeyword.builder()
                                                .flow(flow)
                                                .keyword(recreationKeyword)
                                                .build())
                        .toList();

        List<FlowRecreationPurpose> flowRecreationPurposeList =
                recreationPurposeList.stream()
                        .map(
                                recreationPurpose ->
                                        FlowRecreationPurpose.builder()
                                                .flow(flow)
                                                .purpose(recreationPurpose)
                                                .build())
                        .toList();

        List<FlowAge> flowAgeList =
                postFlowDTO.getAgeList().stream()
                        .map(age -> FlowAge.builder().age(age).flow(flow).build())
                        .toList();

        List<FlowGender> flowGenderList =
                postFlowDTO.getGenderList().stream()
                        .map(gender -> FlowGender.builder().flow(flow).gender(gender).build())
                        .toList();

        flow.getFlowRecreationList().addAll(flowRecreationList);
        flow.getFlowRecreationKeywordList().addAll(flowRecreationKeywordList);
        flow.getFlowRecreationPurposeList().addAll(flowRecreationPurposeList);
        flow.getAgeList().addAll(flowAgeList);
        flow.getGenderList().addAll(flowGenderList);

        return flow;
    }

    public static FlowDetailDTO toFlowDetailDTO(Flow flow, User user) {
        List<Age> ageList =
                flow.getAgeList().stream().map(FlowAge::getAge).collect(Collectors.toList());

        List<Keyword> keywordList =
                flow.getFlowRecreationKeywordList().stream()
                        .map(FlowRecreationKeyword::getKeyword)
                        .map(RecreationKeyword::getKeyword)
                        .collect(Collectors.toList());

        List<Gender> genderList =
                flow.getGenderList().stream()
                        .map(FlowGender::getGender)
                        .collect(Collectors.toList());

        List<Recreation> recreationList =
                flow.getFlowRecreationList().stream()
                        .map(FlowRecreation::getRecreation)
                        .collect(Collectors.toList());

        List<RecreationPreviewDTO> recreationPreviewListDTO =
                RecreationConverter.toRecreationPreviewListDTO(recreationList, user);

        List<Purpose> purposeList =
                flow.getFlowRecreationPurposeList().stream()
                        .map(FlowRecreationPurpose::getPurpose)
                        .map(RecreationPurpose::getPurpose)
                        .collect(Collectors.toList());

        Boolean isScraped =
                user != null
                        ? flow.getFlowFavoriteList().stream()
                                .anyMatch((flowFavorite -> flowFavorite.getUser().equals(user)))
                        : null;

        User author = flow.getAuthor();

        FlowDetailDTO flowDetailDTO =
                FlowDetailDTO.builder()
                        .id(flow.getId())
                        .totalPlayTime(flow.getTotalPlayTime())
                        .participants(flow.getParticipants())
                        .viewCount(flow.getViewCount())
                        .title(flow.getTitle())
                        .age(ageList)
                        .keywordList(keywordList)
                        .gender(genderList)
                        .recreations(recreationPreviewListDTO)
                        .purposeList(purposeList)
                        .author(
                                AuthorDTO.builder()
                                        .userId(author.getId())
                                        .username(author.getUsername())
                                        .build())
                        .isFavorite(isScraped)
                        .build();

        return flowDetailDTO;
    }

    public static List<FlowDetailDTO> toFlowDetailListDTO(List<Flow> flows, User user) {
        return flows.stream().map(flow -> toFlowDetailDTO(flow, user)).toList();
    }

    public static FlowScrapDTO toFlowScrapDTO(Boolean isScraped) {
        return FlowScrapDTO.builder().isScraped(isScraped).build();
    }

    public static FlowFavorite toFlowFavorite(Flow flow, User user) {
        return FlowFavorite.builder().flow(flow).user(user).build();
    }
}
