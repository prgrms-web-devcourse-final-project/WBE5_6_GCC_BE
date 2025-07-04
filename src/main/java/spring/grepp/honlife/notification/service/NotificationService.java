package spring.grepp.honlife.notification.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.member.domain.Member;
import spring.grepp.honlife.member.repos.MemberRepository;
import spring.grepp.honlife.notification.domain.Notification;
import spring.grepp.honlife.notification.model.NotificationDTO;
import spring.grepp.honlife.notification.repos.NotificationRepository;
import spring.grepp.honlife.util.NotFoundException;
import spring.grepp.honlife.util.ReferencedWarning;


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

    public NotificationDTO get(final Integer id) {
        return notificationRepository.findById(id)
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final NotificationDTO notificationDTO) {
        final Notification notification = new Notification();
        mapToEntity(notificationDTO, notification);
        return notificationRepository.save(notification).getId();
    }

    public void update(final Integer id, final NotificationDTO notificationDTO) {
        final Notification notification = notificationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(notificationDTO, notification);
        notificationRepository.save(notification);
    }

    public void delete(final Integer id) {
        notificationRepository.deleteById(id);
    }

    private NotificationDTO mapToDTO(final Notification notification,
            final NotificationDTO notificationDTO) {
        notificationDTO.setId(notification.getId());
        notificationDTO.setEmail(notification.getEmail());
        notificationDTO.setRoutine(notification.getRoutine());
        notificationDTO.setChallenge(notification.getChallenge());
        return notificationDTO;
    }

    private Notification mapToEntity(final NotificationDTO notificationDTO,
            final Notification notification) {
        notification.setEmail(notificationDTO.getEmail());
        notification.setRoutine(notificationDTO.getRoutine());
        notification.setChallenge(notificationDTO.getChallenge());
        return notification;
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Notification notification = notificationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Member notificationMember = memberRepository.findFirstByNotification(notification);
        if (notificationMember != null) {
            referencedWarning.setKey("notification.member.notification.referenced");
            referencedWarning.addParam(notificationMember.getId());
            return referencedWarning;
        }
        return null;
    }

}
