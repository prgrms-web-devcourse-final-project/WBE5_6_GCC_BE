package com.honlife.core.app.model.quest.repos;

import com.honlife.core.app.model.quest.domain.EventQuestProgress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventQuestProgressRepository extends JpaRepository<EventQuestProgress, Long>, EventQuestProgressRepositoryCustom {

    List<EventQuestProgress> findAllByMember_EmailAndIsActive(String memberEmail, Boolean isActive);

    Optional<EventQuestProgress> findByMember_EmailAndId(String userEmail, Long progressId);

    List<EventQuestProgress> findAllByMember_EmailAndIsActiveAndIsDone(String userEmail, boolean b, boolean b1);

    boolean existsEventQuestProgressByEventQuest_Id(Long eventQuestId);
}
