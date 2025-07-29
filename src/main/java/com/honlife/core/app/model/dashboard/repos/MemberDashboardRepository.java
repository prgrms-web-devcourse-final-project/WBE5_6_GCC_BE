package com.honlife.core.app.model.dashboard.repos;

import com.honlife.core.app.model.dashboard.domain.MemberDashboard;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDashboardRepository extends JpaRepository<MemberDashboard, Integer> {

    Optional<MemberDashboard> findByMember_EmailAndStartDate(String memberEmail, LocalDate startDate);
}
