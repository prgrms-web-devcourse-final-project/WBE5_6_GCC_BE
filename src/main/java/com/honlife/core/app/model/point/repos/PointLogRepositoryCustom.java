package com.honlife.core.app.model.point.repos;

import com.honlife.core.app.model.point.code.PointLogType;
import java.time.LocalDateTime;

public interface PointLogRepositoryCustom {

    Integer getTotalPointByDate(LocalDateTime localDateTime, LocalDateTime localDateTime1, PointLogType pointLogType, String userEmail);
}
