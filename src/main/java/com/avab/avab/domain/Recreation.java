package com.avab.avab.domain;

import com.avab.avab.domain.common.BaseEntity;
import com.avab.avab.domain.mapping.RecreationFavorite;
import com.avab.avab.domain.mapping.RecreationRecreationKeyword;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
public class Recreation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String title;

    @Column(length = 300)
    private String imageUrl;

    private Float total_stars;

    @Column(length = 300)
    private String summary;

    private Integer minParticipants;

    private Integer maxParticipants;

    private Integer playTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationWay> recreationWayList = new ArrayList<>();

    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationAge> recreationAgeList = new ArrayList<>();

    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationGender> recreationGenderList = new ArrayList<>();

    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationHashtag> recreationHashTagsList = new ArrayList<>();

    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationRecreationKeyword> recreationRecreationKeywordList = new ArrayList<>();

    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationPlace> recreationPlaceList = new ArrayList<>();

    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationPreparation> recreationPreparationList = new ArrayList<>();

    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationFavorite> recreationFavoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "recreation", cascade = CascadeType.ALL)
    private List<RecreationReview> recreationReviewList = new ArrayList<>();
}