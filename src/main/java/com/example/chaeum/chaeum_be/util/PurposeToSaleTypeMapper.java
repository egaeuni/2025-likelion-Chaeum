package com.example.chaeum.chaeum_be.util;

import com.example.chaeum.chaeum_be.enums.SaleType;
import com.example.chaeum.chaeum_be.enums.UsagePurposeType;

import java.util.*;
import static com.example.chaeum.chaeum_be.enums.SaleType.*;
import static com.example.chaeum.chaeum_be.enums.UsagePurposeType.*;

public final class PurposeToSaleTypeMapper {
    private PurposeToSaleTypeMapper(){}

    public static Map<SaleType, Integer> buildScores(Collection<UsagePurposeType> purposes) {
        Map<SaleType, Integer> score = new EnumMap<>(SaleType.class);
        if (purposes == null) return score;

        for (UsagePurposeType p : purposes) {
            switch (p) {
                case BUSINESS -> add(score, FACTORY_WAREHOUSE, 3, LAND, 2, GUESTHOUSE_FARMSTAY, 1);
                case STUDIO -> add(score, PREFAB_HOUSE, 3, COUNTRY_HOUSE, 2, RURAL_FARM_HOUSE, 1);
                case RESIDENTIAL -> add(score, APARTMENT_VILLA, 3, COUNTRY_HOUSE, 2, RURAL_FARM_HOUSE, 1);
                case CAFE_OR_MEETING -> add(score, GUESTHOUSE_FARMSTAY, 3, COUNTRY_HOUSE, 2, LAND, 1);
                case INVESTMENT -> add(score, APARTMENT_VILLA, 3, LAND, 2, PREFAB_HOUSE, 1);
                case STORAGE -> add(score, FACTORY_WAREHOUSE, 3, LAND, 2, COUNTRY_HOUSE, 1);
                case COMMUNITY -> add(score, RURAL_FARM_HOUSE, 3, COUNTRY_HOUSE, 2, GUESTHOUSE_FARMSTAY, 1);
                case ETC -> {  }    // 점수 부여 X
            }
        }
        return score;
    }

    public static int weightOf(SaleType saleType, Map<SaleType,Integer> score) {
        if (saleType == null || score == null) return 0;
        return score.getOrDefault(saleType, 0);
    }

    private static void add(Map<SaleType, Integer> m,
                            SaleType s1, int w1, SaleType s2, int w2, SaleType s3, int w3) {
        m.merge(s1, w1, Integer::sum);
        m.merge(s2, w2, Integer::sum);
        m.merge(s3, w3, Integer::sum);
    }
}
