package com.example.chaeum.chaeum_be.util;

import com.example.chaeum.chaeum_be.enums.RegionType;

public final class AddressRegionExtractor {
    private AddressRegionExtractor(){}

    public static RegionType extract(String address) {
        if (address == null || address.isBlank()) return null;
        String s = address.trim();

        if (startsWithAny(s, "서울", "서울특별시")) return RegionType.서울;
        if (startsWithAny(s, "경기", "경기도")) return RegionType.경기;
        if (startsWithAny(s, "인천", "인천광역시")) return RegionType.인천;
        if (startsWithAny(s, "부산", "부산광역시")) return RegionType.부산;
        if (startsWithAny(s, "대구", "대구광역시")) return RegionType.대구;
        if (startsWithAny(s, "대전", "대전광역시")) return RegionType.대전;
        if (startsWithAny(s, "광주", "광주광역시")) return RegionType.광주;
        if (startsWithAny(s, "울산", "울산광역시")) return RegionType.울산;
        if (startsWithAny(s, "세종", "세종특별자치시", "세종시")) return RegionType.세종;

        if (startsWithAny(s, "충청북도", "충북")) return RegionType.충북;
        if (startsWithAny(s, "충청남도", "충남")) return RegionType.충남;
        if (startsWithAny(s, "경상북도", "경북")) return RegionType.경북;
        if (startsWithAny(s, "경상남도", "경남")) return RegionType.경남;
        if (startsWithAny(s, "전라북도", "전북", "전북특별자치도")) return RegionType.전북;
        if (startsWithAny(s, "전라남도", "전남")) return RegionType.전남;

        if (startsWithAny(s, "강원특별자치도", "강원도", "강원")) return RegionType.강원;
        if (startsWithAny(s, "제주특별자치도", "제주도", "제주")) return RegionType.제주;

        return null; // 못 뽑으면 null
    }

    private static boolean startsWithAny(String s, String... prefixes){
        for (String p: prefixes){
            if (s.startsWith(p)) return true;
        }
        return false;
    }
}
