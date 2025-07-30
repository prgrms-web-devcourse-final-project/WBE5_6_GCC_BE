package com.honlife.core.app.model.notification.domain;

import com.honlife.core.app.model.common.BaseEntity;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.notification.code.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotifyList extends BaseEntity {
  @Id
  @Column(nullable = false, updatable = false)
  @SequenceGenerator(
          name = "notify_list_sequence",
          sequenceName = "notify_list_sequence",
          allocationSize = 1,
          initialValue = 10000
  )
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "notify_list_sequence"
  )
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  private String name;

  @Enumerated(EnumType.STRING)
  private NotificationType type;


  @Column(name = "is_read")
  @Builder.Default
  private Boolean isRead = false;
}