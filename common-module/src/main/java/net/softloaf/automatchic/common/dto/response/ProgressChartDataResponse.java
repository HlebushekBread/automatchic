package net.softloaf.automatchic.common.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.softloaf.automatchic.common.enums.EvaluationType;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressChartDataResponse {
    @JsonIgnore
    ProgressSnapshotResponse snapshot;
    // Info
    private Double currentScoreReal;
    private Double targetScoreReal;
    // X
    private Instant timestampX;
    // Y
    private Double currentScoreY;
    private Double targetGradeY;
    private Double gradingMaxY;
    private Double grading5Y;
    private Double grading4Y;
    private Double grading3Y;
    private Double gradingMinY;

    public ProgressChartDataResponse(Instant timestamp, ProgressSnapshotResponse snapshot) {
        this.setSnapshot(snapshot);

        this.timestampX = timestamp;

        double p100 = snapshot.getGradingMax() > 0 ? snapshot.getGradingMax() : 1;
        double p0 = 0;

        double currentScore = snapshot.getTotalScore();
        if(EvaluationType.valueOf(snapshot.getEvaluationType()) == EvaluationType.AVERAGE) {
            if(snapshot.getTotalWeight() != 0) {
                currentScore /= snapshot.getTotalWeight();
            } else {
                currentScore = 0;
            }
        }
        this.currentScoreReal = currentScore;

        this.currentScoreY = currentScore / (p100 - p0);

        this.gradingMaxY = snapshot.getGradingMax() / (p100 - p0);
        this.grading5Y = snapshot.getGrading5() / (p100 - p0);
        this.grading4Y = snapshot.getGrading4() / (p100 - p0);
        this.grading3Y = snapshot.getGrading3() / (p100 - p0);
        this.gradingMinY = snapshot.getGradingMin() / (p100 - p0);

        switch (snapshot.getTargetGrade()) {
            case 4:
                this.targetScoreReal = snapshot.getGradingMax();
                this.targetGradeY = this.gradingMaxY;
                break;
            case 3:
                this.targetScoreReal = snapshot.getGrading5();
                this.targetGradeY = this.grading5Y;
                break;
            case 2:
                this.targetScoreReal = snapshot.getGrading4();
                this.targetGradeY = this.grading4Y;
                break;
            case 1:
                this.targetScoreReal = snapshot.getGrading3();
                this.targetGradeY = this.grading3Y;
                break;
            case 0:
                this.targetScoreReal = snapshot.getGradingMin();
                this.targetGradeY = this.gradingMinY;
                break;
        }
    }
}
