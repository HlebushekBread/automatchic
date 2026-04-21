package net.softloaf.automatchic.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressSnapshotResponse {
    private Double totalScore;
    private Double totalWeight;
    private String gradingType;
    private String evaluationType;
    private Integer targetGrade;
    private Double gradingMax;
    private Double grading5;
    private Double grading4;
    private Double grading3;
    private Double gradingMin;
    private Instant timestamp;

    public ProgressSnapshotResponse(ProgressSnapshotResponse snapshot) {
        this.totalScore = snapshot.totalScore;
        this.totalWeight = snapshot.totalWeight;
        this.gradingType = snapshot.gradingType;
        this.evaluationType = snapshot.evaluationType;
        this.targetGrade = snapshot.targetGrade;
        this.gradingMax = snapshot.gradingMax;
        this.grading5 = snapshot.grading5;
        this.grading4 = snapshot.grading4;
        this.grading3 = snapshot.grading3;
        this.gradingMin = snapshot.gradingMin;
        this.timestamp = snapshot.timestamp;
    }
}
