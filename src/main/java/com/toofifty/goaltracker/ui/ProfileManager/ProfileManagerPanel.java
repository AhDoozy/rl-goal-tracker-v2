
package com.toofifty.goaltracker.ui.ProfileManager;

import net.runelite.client.ui.PluginPanel;

import com.toofifty.goaltracker.models.Profile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ProfileManagerPanel extends PluginPanel
{
    private final DefaultListModel<Profile> profileListModel = new DefaultListModel<>();
    private final JList<Profile> profileList = new JList<>(profileListModel);
    private Consumer<Profile> onProfileSelected;

    public ProfileManagerPanel(List<Profile> profiles)
    {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Select a Profile");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        add(titleLabel, BorderLayout.NORTH);

        profileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        profileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Profile selected = profileList.getSelectedValue();
                    if (selected != null && onProfileSelected != null) {
                        onProfileSelected.accept(selected);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(profileList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(Box.createHorizontalGlue());

        JButton newButton = new JButton("New");
        JButton deleteButton = new JButton("Delete");
        JButton loadButton = new JButton("Load");

        newButton.addActionListener(e -> createNewProfile());
        deleteButton.addActionListener(e -> deleteSelectedProfile());
        loadButton.addActionListener(e -> {
            Profile selected = profileList.getSelectedValue();
            if (selected != null && onProfileSelected != null)
            {
                onProfileSelected.accept(selected);
            }
        });

        buttonPanel.add(newButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(loadButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setProfiles(profiles);
    }

    public void setProfiles(List<Profile> profiles)
    {
        profileListModel.clear();
        for (Profile profile : profiles)
        {
            profileListModel.addElement(profile);
        }
    }

    public void onProfileSelected(Consumer<Profile> callback)
    {
        this.onProfileSelected = callback;
    }

    private void createNewProfile()
    {
        String name = JOptionPane.showInputDialog(this, "Enter new profile name:");
        if (name != null && !name.trim().isEmpty())
        {
            Profile newProfile = new Profile(name.trim());
            profileListModel.addElement(newProfile);
            com.toofifty.goaltracker.ui.ProfileManager.ProfileManager.getInstance().addProfile(newProfile);
        }
    }

    private void deleteSelectedProfile()
    {
        Profile selected = profileList.getSelectedValue();
        if (selected != null)
        {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete profile \"" + selected.getName() + "\"?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION)
            {
                profileListModel.removeElement(selected);
                com.toofifty.goaltracker.ui.ProfileManager.ProfileManager.getInstance().removeProfile(selected);
            }
        }
    }

    public void refreshProfiles()
    {
        setProfiles(com.toofifty.goaltracker.ui.ProfileManager.ProfileManager.getInstance().getProfiles());
    }
}
