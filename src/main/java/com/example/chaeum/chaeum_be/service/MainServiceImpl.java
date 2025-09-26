package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.dto.main.MainSectionResponseDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseCardDTO;
import com.example.chaeum.chaeum_be.entity.*;
import com.example.chaeum.chaeum_be.enums.*;
import com.example.chaeum.chaeum_be.repository.HouseRepository;
import com.example.chaeum.chaeum_be.repository.UserPreferenceRepository;
import com.example.chaeum.chaeum_be.util.PurposeToSaleTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private static final int RECO_LIMIT = 6;    // 추천 6개
    private static final int HOT_LIMIT  = 10;   // 최신 10개
    private static final int CANDIDATE_FETCH_SIZE = 200;

    private final HouseRepository houseRepo;
    private final UserPreferenceRepository prefRepo;

    @Override
    @Transactional(readOnly = true)
    public MainSectionResponseDTO loadMain(User me) {
        UserPreference pref = prefRepo.findByUser(me).orElse(null);
        PurposeType purpose = (pref == null ? PurposeType.BUY : pref.getPurpose());

        List<HouseCardDTO> recommended;

        if (purpose == PurposeType.SELL) {
            // 판매자도 집을 등록했다면, 내 집의 지역 기준으로 추천
            recommended = recommendForSeller(me, RECO_LIMIT);
        } else {
            // 구매자/둘다 -> 온보딩 가중치 기반 추천
            recommended = recommendForBuyer(pref, RECO_LIMIT);
        }

        // 방금 등록된 집
        List<HouseCardDTO> hot = latestUserHouses(HOT_LIMIT);

        return MainSectionResponseDTO.builder()
                .purpose(purpose)
                .recommended(recommended)
                .hot(hot)
                .build();
    }

    // 판매자 추천
    private List<HouseCardDTO> recommendForSeller(User me, int limit) {
        // 1) 내가 등록한 최신 매물 1건 조회
        Specification<House> myLatestSpec = (root, q, cb) ->
                cb.and(
                        cb.equal(root.get("source"), SourceType.USER),
                        cb.equal(root.get("owner").get("id"), me.getId())
                );
        Page<House> myLatestPage = houseRepo.findAll(
                myLatestSpec,
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "postedOn"))
        );
        if (myLatestPage.isEmpty()) {
            // 내 집이 없으면 추천 비움
            return List.of();
        }

        House myLatest = myLatestPage.getContent().get(0);
        RegionType region = myLatest.getRegion();
        if (region == null) { return List.of(); }

        // 2) 같은 region의 USER 매물에서 내 매물은 제외하고 최신순 추천
        Specification<House> nearSpec = (root, q, cb) ->
                cb.and(
                        cb.equal(root.get("source"), SourceType.USER),
                        cb.equal(root.get("region"), region),
                        cb.notEqual(root.get("owner").get("id"), me.getId())
                );

        Page<House> nearPage = houseRepo.findAll(
                nearSpec,
                PageRequest.of(0, Math.max(1, limit), Sort.by(Sort.Direction.DESC, "postedOn"))
        );

        return toCardDtos(nearPage.getContent());
    }

    // 구매자, 둘다 추천
    private List<HouseCardDTO> recommendForBuyer(UserPreference pref, int limit) {
        int size = Math.max(1, limit);

        List<UsagePurposeType> purposes = (pref == null ? List.of() : pref.getUsagePurpose());
        boolean onlyEtc = purposes != null && !purposes.isEmpty()
                && purposes.stream().allMatch(p -> p == UsagePurposeType.ETC);

        Pageable top200 = PageRequest.of(0, CANDIDATE_FETCH_SIZE, Sort.by(Sort.Direction.DESC, "postedOn"));
        Specification<House> base = (root, q, cb) -> cb.equal(root.get("source"), SourceType.USER);
        List<House> candidates = houseRepo.findAll(base, top200).getContent();
        if (candidates.isEmpty()) return List.of();

        if (onlyEtc || purposes == null || purposes.isEmpty()) {
            return toCardDtos(candidates.stream().limit(size).toList());
        }

        Map<SaleType,Integer> score = PurposeToSaleTypeMapper.buildScores(purposes);

        List<House> sorted = candidates.stream()
                .sorted((h1, h2) -> {
                    int w2 = PurposeToSaleTypeMapper.weightOf(h2.getSaleType(), score);
                    int w1 = PurposeToSaleTypeMapper.weightOf(h1.getSaleType(), score);
                    int cmp = Integer.compare(w2, w1);
                    if (cmp != 0) return cmp;
                    return h2.getPostedOn().compareTo(h1.getPostedOn());
                })
                .toList();

        return toCardDtos(sorted.stream().limit(size).toList());
    }

    private List<HouseCardDTO> latestUserHouses(int limit) {
        int size = Math.max(1, limit);
        Specification<House> spec = (root, q, cb) ->
                cb.equal(root.get("source"), SourceType.USER);
        Pageable top = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        return toCardDtos(houseRepo.findAll(spec, top).getContent());
    }

    private List<HouseCardDTO> toCardDtos(List<House> houses) {
        return houses.stream().map(h -> HouseCardDTO.builder()
                .id(h.getId())
                .source(h.getSource())
                .region(h.getRegion())
                .title(h.getTitle())
                .address(h.getAddress())
                .saleType(h.getSaleType())
                .dealType(h.getDealType())
                .depositRent(h.getDepositRent())
                .area(h.getArea())
                .imageUrls(h.getImages()==null ? List.of()
                        : h.getImages().stream().map(HouseImage::getImageUrl).toList())
                .postedOn(h.getPostedOn())
                .build()
        ).toList();
    }
}
