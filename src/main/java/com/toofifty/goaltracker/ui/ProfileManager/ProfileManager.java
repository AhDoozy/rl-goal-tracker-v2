package com.toofifty.goaltracker.ui.ProfileManager;

import com.toofifty.goaltracker.ui.ProfileManager.ProfileStorageService;

import com.toofifty.goaltracker.models.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileManager
{
    private static final ProfileManager instance = new ProfileManager();

    private final List<Profile> profiles = new ArrayList<>();
    private Profile activeProfile;

    private ProfileManager() {}

    public static ProfileManager getInstance()
    {
        return instance;
    }

    public void addProfile(Profile profile)
    {
        profiles.add(profile);
        ProfileStorageService.saveProfiles(profiles);
    }

    public void removeProfile(Profile profile)
    {
        profiles.remove(profile);
        ProfileStorageService.saveProfiles(profiles);
        if (activeProfile != null && activeProfile.equals(profile))
        {
            activeProfile = null;
        }
    }

    public List<Profile> getProfiles()
    {
        return Collections.unmodifiableList(profiles);
    }

    public void setActiveProfile(Profile profile)
    {
        this.activeProfile = profile;
    }

    public Profile getActiveProfile()
    {
        return activeProfile;
    }

    public boolean hasProfiles()
    {
        return !profiles.isEmpty();
    }

    public void load()
    {
        profiles.clear();
        profiles.addAll(ProfileStorageService.loadProfiles());
    }
}
