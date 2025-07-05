package spring.grepp.honlife.app.model.notification.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {

    private Integer id;
    private Boolean email;
    private Boolean routine;
    private Boolean challenge;

}
