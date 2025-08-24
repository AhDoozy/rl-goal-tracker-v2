package com.ahdoozy.goaltrackerv2.models.task;

import com.ahdoozy.goaltrackerv2.models.enums.TaskType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.runelite.api.Skill;

@Setter
@Getter
@SuperBuilder
public class SkillLevelTask extends Task
{
    private Skill skill;
    private int level;

    @Override
    public String toString()
    {
        return String.format("%s %s", level, skill.getName());
    }

    @Override
    public TaskType getType()
    {
        return TaskType.SKILL_LEVEL;
    }
}
