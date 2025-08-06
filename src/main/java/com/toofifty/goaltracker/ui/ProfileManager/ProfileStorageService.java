package com.toofifty.goaltracker.ui.ProfileManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toofifty.goaltracker.models.Profile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileStorageService
{
    private static final String FILE_PATH = System.getProperty("user.home") + "/.runelite/profiles.json";
    private static final Gson gson = new Gson();

    public static void saveProfiles(List<Profile> profiles)
    {
        try (FileWriter writer = new FileWriter(FILE_PATH))
        {
            gson.toJson(profiles, writer);
            System.out.println("Profiles saved successfully to: " + FILE_PATH);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static List<Profile> loadProfiles()
    {
        File file = new File(FILE_PATH);
        if (!file.exists())
        {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file))
        {
            Type listType = new TypeToken<List<Profile>>() {}.getType();
            List<Profile> profiles = gson.fromJson(reader, listType);
            System.out.println("Profiles loaded successfully from: " + FILE_PATH);
            return profiles;
        }
        catch (Exception e)
        {
            System.err.println("Failed to load profiles. Corrupt or invalid JSON?");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
