package com.honlife.core.app.model.quest.repos;

import com.honlife.core.app.model.quest.domain.WeeklyQuest;
import java.util.List;

public interface WeeklyQuestRepositoryCustom {

    List<WeeklyQuest> getRandomWeeklyQuest(int count);

}
