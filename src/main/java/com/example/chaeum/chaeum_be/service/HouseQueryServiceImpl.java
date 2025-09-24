package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.dto.house.HouseCardDTO;
import com.example.chaeum.chaeum_be.dto.house.HouseFilterRequestDTO;
import com.example.chaeum.chaeum_be.entity.House;
import com.example.chaeum.chaeum_be.entity.HouseImage;
import com.example.chaeum.chaeum_be.enums.SourceType;
import com.example.chaeum.chaeum_be.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseQueryServiceImpl implements HouseQueryService {

    private final HouseRepository repo;

    @Override
    @Transactional(readOnly = true)
    public Page<HouseCardDTO> filter(HouseFilterRequestDTO r) {

        Specification<House> spec = Specification.where(null);

        // 1) 사용자 등록만
        if (r.isUserOnly()) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("source"), SourceType.USER));
        }

        // 2) 지역
        if (r.getRegion() != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("region"), r.getRegion()));
        }

        // 3) 매물종류(다중)
        if (r.getSaleTypes() != null && !r.getSaleTypes().isEmpty()) {
            spec = spec.and((root, q, cb) -> root.get("saleType").in(r.getSaleTypes()));
        }

        // 4) 거래방식(다중)
        if (r.getDealTypes() != null && !r.getDealTypes().isEmpty()) {
            spec = spec.and((root, q, cb) -> root.get("dealType").in(r.getDealTypes()));
        }

        // 5) 가격 (PriceType 기반 - enum 매칭)
        if (r.getPriceTypes() != null && !r.getPriceTypes().isEmpty()) {
            spec = spec.and((root, q, cb) -> root.get("priceType").in(r.getPriceTypes()));
        }

        Page<House> page;
        Sort sort = Sort.by(Sort.Direction.DESC, "postedOn");

        Integer size = r.getSize();
        if (size == null || size <= 0) {
            // 전체 조회
            List<House> list = repo.findAll(spec, sort);
            List<HouseCardDTO> content = toCardDtos(list);
            return new PageImpl<>(content, Pageable.unpaged(), content.size());
        } else {
            int pageIdx = (r.getPage() == null || r.getPage() < 0) ? 0 : r.getPage();
            Pageable pageable = PageRequest.of(pageIdx, size, sort);
            page = repo.findAll(spec, pageable);
        }

        List<HouseCardDTO> content = toCardDtos(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
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
                .imageUrls(h.getImages() == null ? List.of()
                        : h.getImages().stream().map(HouseImage::getImageUrl).toList())
                .postedOn(h.getPostedOn())
                .build()
        ).toList();
    }
}

