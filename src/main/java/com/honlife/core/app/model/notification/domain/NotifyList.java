package com.honlife.core.app.model.notification.domain;
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
public class NotifyList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private NotificationType type;

  private String name;

  @Column(name = "is_read")
  @Builder.Default
  private Boolean isRead = false;

  @Column(name = "is_active")
  private Boolean isActive;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "member_id")
  private Member member;

}
