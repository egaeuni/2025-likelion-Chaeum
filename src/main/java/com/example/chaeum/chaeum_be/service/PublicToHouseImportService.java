package com.example.chaeum.chaeum_be.service;

public interface PublicToHouseImportService {
    // PublicHouse 전체를 House로 1회 이식하고, 이식된 건수를 반환
    int importOnce();
}
