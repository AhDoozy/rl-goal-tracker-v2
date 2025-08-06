package com.toofifty.goaltracker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile
{
    private final UUID id;
    private final String name;
    private final List<Goal> goals;

    public Profile(String name)
    {
        this.id = UUID.randomUUID();
        this.name = name;
        this.goals = new ArrayList<>();
    }

    public UUID getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public List<Goal> getGoals()
    {
        return goals;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
