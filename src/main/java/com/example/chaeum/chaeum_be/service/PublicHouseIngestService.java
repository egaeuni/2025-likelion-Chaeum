package com.example.chaeum.chaeum_be.service;

public interface PublicHouseIngestService {
    // 13개의 공공 API를 순회하며 DB에 적재
    int ingestAll();
}