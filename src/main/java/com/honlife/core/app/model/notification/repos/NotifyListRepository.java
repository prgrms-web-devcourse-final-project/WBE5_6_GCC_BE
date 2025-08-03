package com.honlife.core.app.model.notification.repos;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.notification.domain.NotifyList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyListRepository  extends JpaRepository<NotifyList, Long> {

  NotifyList findByMember_EmailAndId(String email, Long notifyId);

  List<NotifyList> findByMember(Member member);

  List<NotifyList> findByMember_EmailAndIsReadFalse(String userEmail);


  @Modifying
  @Query("UPDATE NotifyList n SET n.isRead = true WHERE n.member = :member")
  void markAllAsReadByMember(@Param("member")  Member member);

  boolean existsByMemberIdAndIsReadFalse(Long memberId);
}
