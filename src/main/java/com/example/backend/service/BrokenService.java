package com.example.backend.service;

import com.example.backend.dto.request.BrokenRequest;
import com.example.backend.dto.response.BrokenByTime;
import com.example.backend.dto.response.BrokenResponse;
import com.example.backend.enums.BrokenStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BrokenService {
    BrokenResponse reportBroken(BrokenRequest brokenRequest);
    Page<BrokenResponse> getAllBrokenReports(Pageable pageable);
    BrokenResponse updateBrokenStatus(int id, BrokenStatus status);
    Page<BrokenResponse> getBrokenReports(Integer year, String month, String quarter, Pageable pageable);
    List<BrokenByTime> getBrokenByTime(LocalDateTime startDate, LocalDateTime endDate, String groupBy);
    Page<BrokenResponse> searchBrokenByEquipmentName(String equipmentName, Pageable pageable);
}
