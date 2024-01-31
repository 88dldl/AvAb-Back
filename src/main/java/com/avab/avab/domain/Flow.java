package com.avab.avab.domain;

import java.util.ArrayList;
import java.util.List;

import com.avab.avab.domain.mapping.FlowFavorite;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import com.avab.avab.domain.common.BaseEntity;
import com.avab.avab.domain.mapping.FlowRecreation;
import com.avab.avab.domain.mapping.FlowRecreationKeyword;
import com.avab.avab.domain.mapping.FlowRecreationPurpose;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Flow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer totalPlayTime;

    private Integer participants;

    private Long viewCount;

    @Column(length = 100)
    private String title;

    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowAge> ageList = new ArrayList<>();

    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowGender> genderList = new ArrayList<>();

    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowRecreation> flowRecreationList = new ArrayList<>();

    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowRecreationKeyword> flowRecreationKeywordList = new ArrayList<>();

    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowRecreationPurpose> flowRecreationPurposeList = new ArrayList<>();

    @OneToMany(mappedBy = "flow", cascade = CascadeType.ALL)
    private List<FlowFavorite> flowFavoriteList = new ArrayList<>();
}