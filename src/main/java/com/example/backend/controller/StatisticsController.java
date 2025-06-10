package com.example.backend.controller;

import com.example.backend.dto.response.*;
import com.example.backend.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/statistics")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatisticsController {
    EquipmentService equipmentService;
    EquipmentItemService equipmentItemService;
    MaintenanceService maintenanceService;
    BrokenService brokenService;
    ExcelExportService excelExportService;
    @GetMapping("/total_equipment")
    public ResponseEntity<Long> getTotalEquipment() {
        return ResponseEntity.ok(equipmentService.getTotalEquipment());
    }
    @GetMapping("/total_equipment_item")
    public ResponseEntity<Long> getTotalEquipmentItem() {
        return ResponseEntity.ok(equipmentItemService.getTotalEquipmentItem());
    }
    @GetMapping("/status_distribution")
    public ResponseEntity<List<StatusDistribution>> getStatusDistribution() {
        List<StatusDistribution> distribution = equipmentItemService.getStatusDistribution();
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/reports/maintenance")
    public ResponseEntity<Page<MaintenanceResponse>> getMaintenanceReports(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String quarter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "maintenanceDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MaintenanceResponse> reports = maintenanceService.getMaintenanceReports(year, month, quarter, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/reports/broken")
    public ResponseEntity<Page<BrokenResponse>> getBrokenReports(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String quarter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "brokenDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BrokenResponse> reports = brokenService.getBrokenReports(year, month, quarter, pageable);
        return ResponseEntity.ok(reports);
    }
    @GetMapping("/reports/maintenance/export")
    public ResponseEntity<byte[]> exportMaintenanceReportsToExcel(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String quarter) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<MaintenanceResponse> reports = maintenanceService.getMaintenanceReports(year, month, quarter, pageable);

        String[] headers = {"ID", "Equipment Name", "Serial Number", "Maintenance Date", "Description", "Technician", "Cost"};
        List<Function<MaintenanceResponse, Object>> fieldExtractors = Arrays.asList(
                MaintenanceResponse::getId,
                MaintenanceResponse::getEquipmentName,
                MaintenanceResponse::getSerialNumber,
                MaintenanceResponse::getMaintenanceDate,
                MaintenanceResponse::getDescription,
                MaintenanceResponse::getTechnician,
                MaintenanceResponse::getCost
        );

        byte[] excelBytes = excelExportService.exportToExcel(reports.getContent(), headers, "Maintenance Statistics", fieldExtractors);

        String fileName = "MaintenanceStatistics_" + (year != null ? year : "AllYears") +
                (month != null && !month.isEmpty() ? month : "") + (quarter != null && !quarter.isEmpty() ? quarter : "") + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelBytes);
    }

    @GetMapping("/reports/broken/export")
    public ResponseEntity<byte[]> exportBrokenReportsToExcel(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String quarter) throws Exception {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<BrokenResponse> reports = brokenService.getBrokenReports(year, month, quarter, pageable);

        String[] headers = {"ID", "Equipment Name", "Serial Number", "Reported By", "Broken Date", "Description", "Status"};
        List<Function<BrokenResponse, Object>> fieldExtractors = Arrays.asList(
                BrokenResponse::getId,
                BrokenResponse::getEquipmentName,
                BrokenResponse::getSerialNumber,
                BrokenResponse::getFullName,
                BrokenResponse::getBrokenDate,
                BrokenResponse::getDescription,
                BrokenResponse::getStatus
        );

        byte[] excelBytes = excelExportService.exportToExcel(reports.getContent(), headers, "Broken Statistics", fieldExtractors);

        String fileName = "BrokenStatistics_" + (year != null ? year : "AllYears") +
                (month != null && !month.isEmpty() ? month : "") + (quarter != null && !quarter.isEmpty() ? quarter : "") + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelBytes);
    }
    @GetMapping("/total_maintenance_cost")
    public ResponseEntity<Double> getTotalMaintenanceCost() {
        Double totalCost = maintenanceService.getTotalMaintenanceCost();
        return ResponseEntity.ok(totalCost);
    }

    @GetMapping("/maintenance_by_time")
    public ResponseEntity<List<MaintenanceByTime>> getMaintenanceByTime(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "month") String groupBy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate, formatter) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate, formatter) : null;
        return ResponseEntity.ok(maintenanceService.getMaintenanceByTime(start, end, groupBy));
    }

    @GetMapping("/broken_by_time")
    public ResponseEntity<List<BrokenByTime>> getBrokenByTime(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "month") String groupBy) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate, formatter) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate, formatter) : null;
        return ResponseEntity.ok(brokenService.getBrokenByTime(start, end, groupBy));
    }
}
