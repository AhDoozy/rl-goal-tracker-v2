package com.ahdoozy.goaltrackerv2;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.ahdoozy.goaltrackerv2.adapters.QuestAdapter;
import com.ahdoozy.goaltrackerv2.adapters.SkillAdapter;
import com.ahdoozy.goaltrackerv2.adapters.TaskAdapter;
import com.ahdoozy.goaltrackerv2.models.Goal;
import com.ahdoozy.goaltrackerv2.models.task.Task;
import com.ahdoozy.goaltrackerv2.utils.ReorderableList;
import net.runelite.api.Quest;
import net.runelite.api.Skill;

import java.util.List;

import javax.inject.Inject;

public class GoalSerializer
{	
    @Inject
	private Gson gson;

    public ReorderableList<Goal> deserialize(String serialized)
    {
        return ReorderableList.from(this.getBuilder().fromJson(serialized, Goal[].class));
    }

    public String serialize(List<Goal> goals)
    {
        return this.serialize(goals, false);
    }

    public String serialize(List<Goal> goals, boolean prettyPrinting)
    {
        return this.getBuilder(prettyPrinting).toJson(goals);
    }

    private Gson getBuilder() {
        return this.getBuilder(false);
    }

    private Gson getBuilder(boolean prettyPrinting) {
        var builder = gson.newBuilder()
            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Skill.class, new SkillAdapter())
            .registerTypeAdapter(Quest.class, new QuestAdapter());

        if (prettyPrinting) {
            builder.setPrettyPrinting();
        }

        return builder.create();
    }
}
