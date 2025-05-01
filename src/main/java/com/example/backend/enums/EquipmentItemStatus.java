package com.example.backend.enums;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum EquipmentItemStatus {
    ACTIVE, BORROWED, MAINTENANCE, BROKEN
}

