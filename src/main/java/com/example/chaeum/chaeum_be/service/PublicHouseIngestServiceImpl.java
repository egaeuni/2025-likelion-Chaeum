package com.example.chaeum.chaeum_be.service;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.entity.PublicHouse;
import com.example.chaeum.chaeum_be.exception.GlobalException;
import com.example.chaeum.chaeum_be.repository.PublicHouseRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicHouseIngestServiceImpl implements PublicHouseIngestService {

    private final PublicHouseRepository repo;
    private final ObjectMapper om = new ObjectMapper();
    private final RestTemplate rt = new RestTemplate();

    @Value("${openapi.odcloud.service-key}")
    private String serviceKey;

    // 기본값
    private static final String DEFAULT_TITLE        = "공공 빈집 매물";
    private static final String DEFAULT_DEAL_TYPE    = "미정";
    private static final String DEFAULT_DEPOSIT_RENT = "미정";
    private static final String DEFAULT_AREA         = "불확실";
    private static final String DEFAULT_POSTED_ON    = "불확실";
    private static final String DEFAULT_PHONE_NUMBER = "관할기관에 전화해주세요.";


    // 공공 API 13개 데이터셋
    private static final List<Dataset> DATASETS = List.of(
            // 1. 서울특별시 광진구
            ds(
                    "https://api.odcloud.kr/api/15036279/v1/uddi:d1ed2252-55e6-45a1-8300-960a6cd26e79",
                    "data",
                    new String[]{"법정동명"},   // address
                    new String[]{"현황용도"}    // saleType
            ),
            
            // 2. 서울특별시 도봉구
            ds(
                    "https://api.odcloud.kr/api/15049614/v1/uddi:99b86522-0ed1-4cfa-806e-baef71cc5aee",
                    "data",
                    new String[]{"도로명주소"},   // address
                    new String[]{"주택유형"}    // saleType
            ),

            // 3. 부산광역시 수영구
            ds(
                    "https://api.odcloud.kr/api/15060082/v1/uddi:c47f70c8-fb75-4136-9bef-cb0d5a81a9de",
                    "data",
                    new String[]{"소재지"},   // address
                    new String[]{"주택유형"}    // saleType
            ),

            // 4. 광주광역시 북구
            ds(
                    "https://api.odcloud.kr/api/15085496/v1/uddi:61a15fde-4a38-47b8-bd1b-bee102e4c19a",
                    "data",
                    new String[]{"도로명주소"},   // address
                    new String[]{"구조유형"}    // saleType
            ),

            // 5. 충청북도 충주
            ds(
                    "https://api.odcloud.kr/api/15144409/v1/uddi:11a8fbdd-020d-4e31-8607-a7791e031b41",
                    "data",
                    new String[]{"도로명주소"},   // address
                    new String[]{"주택유형"}    // saleType
            ),

            // 6. 경상북도 청도
            ds(
                    "https://api.odcloud.kr/api/15144491/v1/uddi:6bcddf4e-e05f-4a6a-8b21-f2bd1ffa9b1f",
                    "data",
                    new String[]{"도로명주소"},   // address
                    new String[]{"주택유형"}    // saleType
            ),

            // 7. 경상남도 하동
            ds(
                    "https://api.odcloud.kr/api/15138897/v1/uddi:522af439-01cb-46d9-a2a7-b4cc3c0ffec5",
                    "data",
                    new String[]{"소재지"},   // address
                    new String[]{"주택유형"}    // saleType
            ),

            // 8. 전라도 남원
            ds(
                    "https://api.odcloud.kr/api/15036241/v1/uddi:bc81e4d4-439c-4ed4-bd02-c02fe1285c47",
                    "data",
                    new String[]{"소재지 도로명주소"},   // address
                    new String[]{"주택유형"}    // saleType
            ),

            // 9. 전라남도 담양
            ds(
                    "https://api.odcloud.kr/api/15043151/v1/uddi:03dca382-e6a3-4a15-ba88-e410fe66f766_202003301307",
                    "data",
                    new String[]{"위치주소(지번 비공개)"},   // address
                    new String[]{"주택유형"}    // saleType
            )
    );

    private static Dataset ds(String baseUrl, String arrayPath,
                              String[] addressKeys, String[] saleTypeKeys) {
        return new Dataset(baseUrl, arrayPath, addressKeys, saleTypeKeys);
    }

    private record Dataset(String baseUrl, String arrayPath,
                           String[] addressKeys, String[] saleTypeKeys) {}

    @Override
    public int ingestAll() {
        int total = 0;

        for (Dataset ds : DATASETS) {
            try {
                var uri = UriComponentsBuilder.fromHttpUrl(ds.baseUrl())
                        .queryParam("page", 1)
                        .queryParam("perPage", 10)
                        .queryParam("serviceKey", serviceKey)
                        .build(true)
                        .toUri();

                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                headers.set("User-Agent", "Mozilla/5.0");

                ResponseEntity<String> res =
                        rt.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);

                if (!res.getStatusCode().is2xxSuccessful()) {
                    log.warn("[{}] status={}", res.getStatusCodeValue());
                    throw new GlobalException(ErrorCode.API_REQUEST_FAILED);
                }

                JsonNode root = om.readTree(res.getBody());
                JsonNode arr  = atPath(root, ds.arrayPath());
                if (arr == null || !arr.isArray() || arr.isEmpty()) {
                    log.info("[{}] '{}' 경로에 데이터 없음", ds.arrayPath());
                    continue;
                }

                int inserted = 0;

                for (JsonNode rec : arr) {
                    String addressRaw  = firstText(rec, ds.addressKeys());
                    String saleTypeRaw = firstText(rec, ds.saleTypeKeys());

                    if (isBlank(addressRaw) || isBlank(saleTypeRaw)) {
                        log.debug("[{}] 스킵(주소/매물종류 누락): {}", rec);
                        continue;
                    }

                    String address  = normalize(addressRaw);
                    String saleType = normalize(saleTypeRaw);

                    // 중복이면 스킵
                    if (repo.existsByAddressAndSaleType(address, saleType)) {
                        continue;
                    }

                    PublicHouse entity = PublicHouse.builder()
                            .address(address)                      // 공공 API
                            .saleType(saleType)                    // 공공 API

                            .title(DEFAULT_TITLE)                  // 기본값
                            .dealType(DEFAULT_DEAL_TYPE)           // 기본값
                            .depositRent(DEFAULT_DEPOSIT_RENT)     // 기본값
                            .area(DEFAULT_AREA)                    // 기본값
                            .postedOn(DEFAULT_POSTED_ON)           // 기본값
                            .phoneNum(DEFAULT_PHONE_NUMBER)        // 기본값

                            .currentJeonse(null)                   // 나머지는 null
                            .currentDepositRent(null)
                            .roomCount(null)
                            .direction(null)
                            .parkingSpace(null)
                            .heatingType(null)
                            .transportation(null)
                            .facilities(null)
                            .options(null)
                            .etc(null)
                            .moveInAvailableDate(null)
                            .imageUrl(null)
                            .build();

                    repo.save(entity);
                    inserted++;
                }

                total += inserted;
                log.info("[{}] inserted={}", inserted);

            } catch (GlobalException ge) {
                throw ge;
            } catch (Exception e) {
                log.error("[{}] 수집/파싱 오류", e);
                throw new GlobalException(ErrorCode.DATA_PARSING_FAILED);
            }
        }

        log.info("총 적재: {}", total);
        return total;
    }

    private static boolean isBlank(String s){ return s == null || s.isBlank(); }
    private static String normalize(String s){ return s.trim().replaceAll("\\s{2,}", " "); }

    private static JsonNode atPath(JsonNode root, String dotPath) {
        if (root == null || dotPath == null || dotPath.isBlank()) return null;
        JsonNode cur = root;
        for (String p : dotPath.split("\\.")) {
            cur = cur.path(p);
            if (cur.isMissingNode()) return null;
        }
        return cur;
    }

    private static String firstText(JsonNode node, String[] keys) {
        if (node == null || keys == null) return null;
        for (String k : keys) {
            JsonNode v = node.path(k);
            if (v != null && !v.isMissingNode() && !v.isNull()) {
                String s = v.asText(null);
                if (!isBlank(s)) return s;
            }
        }
        return null;
    }
}
