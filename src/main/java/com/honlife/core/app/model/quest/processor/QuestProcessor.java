package com.honlife.core.app.model.quest.processor;

import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.infra.event.CommonEvent;

public interface QuestProcessor{

    void process(CommonEvent event, Long progressId, QuestDomain questDomain);
}
