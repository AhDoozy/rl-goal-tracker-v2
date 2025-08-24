package com.toofifty.goaltracker.models.task;

import com.google.gson.annotations.SerializedName;
import com.toofifty.goaltracker.models.enums.TaskType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.runelite.api.Quest;

/**
 * Task representing completion of a quest.
 * Stores the RuneLite Quest reference and returns its name as label.
 */
@Getter
@Setter
@SuperBuilder
public final class QuestTask extends Task
{
    @SerializedName("quest_id")
    private Quest quest;

    @Override
    public String toString()
    {
        return quest.getName();
    }

    @Override
    public TaskType getType()
    {
        return TaskType.QUEST;
    }
}
