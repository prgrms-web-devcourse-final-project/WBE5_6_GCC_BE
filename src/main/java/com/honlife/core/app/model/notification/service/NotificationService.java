package com.honlife.core.app.model.notification.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.notification.domain.Notification;
import com.honlife.core.app.model.notification.dto.NotificationDTO;
import com.honlife.core.app.model.notification.repos.NotificationRepository;
import com.honlife.core.infra.util.NotFoundException;


@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public NotificationService(final NotificationRepository notificationRepository,
        final MemberRepository memberRepository) {
        this.notificationRepository = notificationRepository;
        this.memberRepository = memberRepository;
    }

    public List<NotificationDTO> findAll() {
        final List<Notification> notifications = notificationRepository.findAll(Sort.by("id"));
        return notifications.stream()
            .map(notification -> mapToDTO(notification, new NotificationDTO()))
            .toList();
    }

    public NotificationDTO get(final Long id) {
        return notificationRepository.findById(id)
            .map(notification -> mapToDTO(notification, new NotificationDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final NotificationDTO notificationDTO) {
        final Notification notification = new Notification();
        mapToEntity(notificationDTO, notification);
        return notificationRepository.save(notification).getId();
    }

    public void update(final Long id, final NotificationDTO notificationDTO) {
        final Notification notification = notificationRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(notificationDTO, notification);
        notificationRepository.save(notification);
    }

    public void delete(final Long id) {
        notificationRepository.deleteById(id);
    }

    private NotificationDTO mapToDTO(final Notification notification,
        final NotificationDTO notificationDTO) {
        notificationDTO.setId(notification.getId());
        notificationDTO.setIsEmail(notification.getIsEmail());
        notificationDTO.setIsRoutine(notification.getIsRoutine());
        notificationDTO.setIsBadge(notification.getIsBadge());
        notificationDTO.setMember(notification.getMember() == null ? null : notification.getMember().getId());
        return notificationDTO;
    }

    private Notification mapToEntity(final NotificationDTO notificationDTO,
        final Notification notification) {
        notification.setIsEmail(notificationDTO.getIsEmail());
        notification.setIsRoutine(notificationDTO.getIsRoutine());
        notification.setIsBadge(notificationDTO.getIsBadge());
        final Member member = notificationDTO.getMember() == null ? null : memberRepository.findById(notificationDTO.getMember())
            .orElseThrow(() -> new NotFoundException("member not found"));
        notification.setMember(member);
        return notification;
    }

    public boolean memberExists(final Long id) {
        return notificationRepository.existsByMemberId(id);
    }

}
