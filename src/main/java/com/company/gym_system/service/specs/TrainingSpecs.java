package com.company.gym_system.service.specs;

import com.company.gym_system.entity.Training;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TrainingSpecs {

    public static Specification<Training> byTraineeUsername(String username) {
        return (root, query, cb) ->
            cb.equal(root.get("trainee").get("user").get("username"), username);
    }

    public static Specification<Training> byTrainerUsername(String username) {
        return (root, query, cb) ->
            cb.equal(root.get("trainer").get("user").get("username"), username);
    }

    public static Specification<Training> byDateRange(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from != null && to != null)
                return cb.between(root.get("trainingDate"), from, to);
            if (from != null)
                return cb.greaterThanOrEqualTo(root.get("trainingDate"), from);
            if (to != null)
                return cb.lessThanOrEqualTo(root.get("trainingDate"), to);
            return cb.conjunction();
        };
    }

    public static Specification<Training> byTrainerName(String trainerName) {
        return (root, query, cb) -> trainerName == null ? cb.conjunction()
                : cb.like(
                    cb.concat(
                        cb.concat(root.get("trainer").get("firstName"), " "),
                        root.get("trainer").get("lastName")
                    ),
                    "%" + trainerName + "%"
                );
    }

    public static Specification<Training> byTraineeName(String traineeName) {
        return (root, query, cb) -> traineeName == null ? cb.conjunction()
                : cb.like(
                    cb.concat(
                        cb.concat(root.get("trainee").get("firstName"), " "),
                        root.get("trainee").get("lastName")
                    ),
                    "%" + traineeName + "%"
                );
    }

    public static Specification<Training> byTrainingType(String trainingType) {
        return (root, query, cb) -> trainingType == null ? cb.conjunction()
                : cb.like(root.get("trainingType").get("name"), "%" + trainingType + "%");
    }
}
