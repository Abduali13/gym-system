//package com.company.workload.service;
//
//import com.company.workload.model.WorkloadMonthlySummary;
//import com.company.workload.model.WorkloadUpdateRequest;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class WorkloadServiceTest {
//
//    @Test
//    void applyUpdate_and_query_methods_work() {
//        WorkloadService service = new WorkloadService();
//
//        WorkloadUpdateRequest add = new WorkloadUpdateRequest();
//        add.setTrainerUsername("trainer1");
//        add.setTrainerFirstName("John");
//        add.setTrainerLastName("Doe");
//        add.setActive(true);
//        add.setTrainingDate(LocalDate.of(2024, 10, 5));
//        add.setTrainingDuration(60);
//        add.setAction(WorkloadUpdateRequest.ActionType.ADD);
//
//        service.applyUpdate(add, "tx-1");
//
//        int total = service.getMonthly("trainer1", 2024, 10);
//        assertThat(total).isEqualTo(60);
//
//        WorkloadUpdateRequest del = new WorkloadUpdateRequest();
//        del.setTrainerUsername("trainer1");
//        del.setTrainerFirstName("John");
//        del.setTrainerLastName("Doe");
//        del.setActive(true);
//        del.setTrainingDate(LocalDate.of(2024, 10, 6));
//        del.setTrainingDuration(30);
//        del.setAction(WorkloadUpdateRequest.ActionType.DELETE);
//
//        service.applyUpdate(del, "tx-2");
//
//        int afterDel = service.getMonthly("trainer1", 2024, 10);
//        assertThat(afterDel).isEqualTo(30);
//
//        WorkloadMonthlySummary summary = service.getSummary("trainer1");
//        assertThat(summary).isNotNull();
//        assertThat(summary.getTrainerFirstName()).isEqualTo("John");
//        assertThat(summary.getYears().get(2024).get(10)).isEqualTo(30);
//    }
//}
