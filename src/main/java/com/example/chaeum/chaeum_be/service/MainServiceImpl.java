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

        List<HouseCardDTO> recommended =
                (purpose == PurposeType.SELL) ? List.of() : recommendForBuyer(pref, RECO_LIMIT);

        List<HouseCardDTO> hot = latestUserHouses(HOT_LIMIT);

        return MainSectionResponseDTO.builder()
                .purpose(purpose)
                .recommended(recommended)
                .hot(hot)
                .build();
    }

    // 집 추천
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
        Specification<House> spec = (root, q, cb) -> cb.equal(root.get("source"), SourceType.USER);
        Pageable top = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "postedOn"));
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
