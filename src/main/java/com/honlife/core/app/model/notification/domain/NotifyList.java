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
  @SequenceGenerator(
      name = "notify_sequence",
      sequenceName = "notify_sequence",
      allocationSize = 1,
      initialValue = 10000
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "notify_sequence"
  )
  @Column(nullable = false, updatable = false)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  @Column
  private String name;
  

  @Column(name = "is_read", nullable = false)
  @Builder.Default
  private Boolean isRead = false;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;
}
