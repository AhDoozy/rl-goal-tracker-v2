package com.ahdoozy.goaltrackerv2.models.task;

import com.ahdoozy.goaltrackerv2.models.enums.TaskType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.runelite.api.Skill;

@Getter
@Setter
@SuperBuilder
public class SkillXpTask extends Task
{
    private Skill skill;
    private int xp;

    @Override
    public String toString()
    {
        return String.format("%d %s XP", xp, skill.getName());
    }

    @Override
    public TaskType getType()
    {
        return TaskType.SKILL_XP;
    }
}
