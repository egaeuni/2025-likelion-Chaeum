package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.entity.House;
import com.example.chaeum.chaeum_be.entity.PublicHouse;
import com.example.chaeum.chaeum_be.enums.SaleType;
import com.example.chaeum.chaeum_be.enums.SourceType;
import com.example.chaeum.chaeum_be.repository.HouseRepository;
import com.example.chaeum.chaeum_be.repository.PublicHouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicToHouseImportServiceImpl implements PublicToHouseImportService {

    private final PublicHouseRepository publicRepo;
    private final HouseRepository houseRepo;

    @Override
    @Transactional
    public int importOnce() {
        int migrated = 0;

        for (PublicHouse p : publicRepo.findAll()) {

            // 1) saleType 먼저 매핑: 실패하면 저장하지 않기
            SaleType mappedSaleType = parseSaleType(p.getSaleType());
            if (mappedSaleType == null) {
                continue;
            }

            // 2) 외부 식별자: 매핑된 saleType 기준으로 생성(문자열 말고 enum 사용)
            String extId = buildExternalIdUsingMapped(p.getAddress(), mappedSaleType);

            // 3) 중복 방지: 있으면 업데이트, 없으면 새로 생성
            House h = houseRepo.findBySourceAndExternalId(SourceType.PUBLIC, extId)
                    .orElseGet(House::new);

            // 4) 읽기 전용 + owner=null
            h.setSource(SourceType.PUBLIC);
            h.setExternalId(extId);
            h.setReadOnly(true);
            h.setOwner(null);

            // 5) address & saleType만 저장, 나머지는 null 유지
            h.setAddress(safe(p.getAddress()));
            h.setSaleType(mappedSaleType);

            houseRepo.save(h);
            migrated++;
        }
        return migrated;
    }

    // 공백/중복공백 정리
    private String safe(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s{2,}", " ");
    }

    // 매핑 규칙 수정하기
    private SaleType parseSaleType(String raw) {
        if (raw == null) return null;
        String k = raw.trim();

        if (k.equals("단독") || k.equals("단독주택") || k.equals("주택") || k.equals("단독주택(다가구)")) {
            return SaleType.COUNTRY_HOUSE;
        }
        if (k.equals("아파트")) {
            return SaleType.APARTMENT_VILLA;
        }

        return null;
    }

    // 매핑된 saleType으로 externalId 생성
    private String buildExternalIdUsingMapped(String address, SaleType mappedSaleType) {
        String a = safe(address).toLowerCase();
        String s = mappedSaleType.name().toLowerCase();
        return a + "|" + s;
    }
}
