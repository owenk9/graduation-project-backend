package com.example.backend.service.Impl;

import com.example.backend.dto.request.BrokenRequest;
import com.example.backend.dto.response.BrokenByTime;
import com.example.backend.dto.response.BrokenResponse;
import com.example.backend.entity.Broken;
import com.example.backend.entity.EquipmentItem;
import com.example.backend.entity.Users;
import com.example.backend.enums.BrokenStatus;
import com.example.backend.enums.EquipmentItemStatus;
import com.example.backend.mapper.BrokenMapper;
import com.example.backend.repository.BrokenRepository;
import com.example.backend.repository.EquipmentItemRepository;
import com.example.backend.repository.UsersRepository;
import com.example.backend.service.BrokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BrokenServiceImpl implements BrokenService {
    BrokenRepository brokenRepository;
    EquipmentItemRepository equipmentItemRepository;
    UsersRepository usersRepository;
    BrokenMapper brokenMapper;
    @Override
    public BrokenResponse reportBroken(BrokenRequest brokenRequest) {
        EquipmentItem equipmentItem = equipmentItemRepository.findById(brokenRequest.getEquipmentItemId())
                .orElseThrow(() -> new RuntimeException("Equipment item not found"));
        Users user = usersRepository.findById(brokenRequest.getUsersId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (EquipmentItemStatus.BROKEN.equals(equipmentItem.getStatus())) {
            throw new RuntimeException("This equipment item is already reported as broken");
        }
        Broken broken = brokenMapper.toBroken(brokenRequest);
        broken.setEquipmentItem(equipmentItem);
        broken.setUsers(user);
        broken.setStatus(BrokenStatus.PENDING);
        broken = brokenRepository.save(broken);

        equipmentItem.setStatus(EquipmentItemStatus.BROKEN);
        equipmentItemRepository.save(equipmentItem);

        return brokenMapper.toBrokenResponse(broken);    }

    @Override
    public Page<BrokenResponse> getAllBrokenReports(Pageable pageable) {
        Page<Broken> brokenPage = brokenRepository.findAll(pageable);
        return brokenPage.map(brokenMapper::toBrokenResponse);
    }

    @Override
    public BrokenResponse updateBrokenStatus(int id, BrokenStatus status) {
        Broken broken = brokenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Broken report not found"));
        broken.setStatus(status);
        broken = brokenRepository.save(broken);
        return brokenMapper.toBrokenResponse(broken);
    }

    @Override
    public Page<BrokenResponse> getBrokenReports(Integer year, String month, String quarter, Pageable pageable) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (year != null) {
            if (month != null && !month.isEmpty()) {
                startDate = LocalDateTime.of(year, Integer.parseInt(month), 1, 0, 0);
                endDate = startDate.plusMonths(1).minusSeconds(1);
            } else if (quarter != null && !quarter.isEmpty()) {
                int startMonth = (Integer.parseInt(quarter) - 1) * 3 + 1;
                startDate = LocalDateTime.of(year, startMonth, 1, 0, 0);
                endDate = startDate.plusMonths(3).minusSeconds(1);
            } else {
                startDate = LocalDateTime.of(year, 1, 1, 0, 0);
                endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59);
            }
        }

        if (startDate == null || endDate == null) {
            return brokenRepository.findAll(pageable).map(brokenMapper::toBrokenResponse);
        }

        return brokenRepository.findByBrokenDateBetween(startDate, endDate, pageable)
                .map(brokenMapper::toBrokenResponse);
    }

    @Override
    public List<BrokenByTime> getBrokenByTime(LocalDateTime startDate, LocalDateTime endDate, String groupBy) {
        List<Object[]> rawResults;
        if ("month".equalsIgnoreCase(groupBy)) {
            rawResults = brokenRepository.countBrokenByMonth(startDate, endDate);
        } else if ("quarter".equalsIgnoreCase(groupBy)) {
            rawResults = brokenRepository.countBrokenByQuarter(startDate, endDate);
        } else {
            rawResults = brokenRepository.countBrokenByMonth(startDate, endDate);
        }
        return rawResults.stream()
                .map(result -> new BrokenByTime(
                        (String) result[0],
                        (Long) result[1]
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Page<BrokenResponse> searchBrokenByEquipmentName(String equipmentName, Pageable pageable) {
        Page<Broken> brokenPage = brokenRepository.findByEquipmentNameContainingIgnoreCase(equipmentName.trim(), pageable);
        return brokenPage.map(brokenMapper::toBrokenResponse);
    }

}
