package com.example.backend.dto.response;

import lombok.Data;

@Data
public class BrokenByTime {
    private String timePeriod;
    private Long count;

    public BrokenByTime(String timePeriod, Long count) {
        this.timePeriod = timePeriod;
        this.count = count;
    }

}
