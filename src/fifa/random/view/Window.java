/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fifa.random.view;

import fifa.random.FifaRandom;
import fifa.random.Player;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

/**
 *
 * @author esteban.duran
 */
public class Window extends JFrame {

    PanelPlayers pnlPlayers;
    PanelConsole pnlConsole;
    PanelSettings pnlSettings;
    public String url;

    FifaRandom league;

    public static void main(String[] args) {
        String path = "resources";
        JFileChooser fc = new JFileChooser(path);
        int result = fc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            path = fc.getSelectedFile().getAbsolutePath();
        }
        if (path != null && !path.isEmpty()) {
            Window window = new Window(new FifaRandom(path));
            window.url = path;
        }
    }

    public Window(FifaRandom league) {
        this.league = league;
        setup();
    }

    public void setup() {
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        this.setTitle("Rival Finder");

        GridBagConstraints gbc = new GridBagConstraints();
//        league = new FifaRandom(url);
//        url = "resources\\players.txt";

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = 2;
        pnlPlayers = new PanelPlayers(league);
        this.add(pnlPlayers, gbc);

        gbc.gridy = 1;
        pnlConsole = new PanelConsole(league, pnlPlayers);
        this.add(pnlConsole, gbc);

        gbc.gridy = 2;
        pnlSettings = new PanelSettings(league, this);
        this.add(pnlSettings, gbc);

        this.pack();
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            league.persist();
        }
        super.processWindowEvent(e);
    }

}

class PanelPlayers extends JPanel {

    FifaRandom league;

    public PanelPlayers(FifaRandom league) {
        this.league = league;
        setup();
    }

    public void setup() {
//        this.add(new JLabel("-------------- PANEL PLAYERS --------------"));
        this.setVisible(true);
        this.setBorder(new TitledBorder(" Players "));
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridx = 0;
        for (int i = 0; i < league.players.size(); i++) {
            if (i % 5 == 0) {
                gbc.gridx += 2;
                gbc.gridy = 0;
            }
            gbc.gridy += 1;
            gbc.anchor = GridBagConstraints.WEST;
            Player pj = league.players.get(i);
            this.add(pj.getLabel(), gbc);

            gbc.gridx += 1;
            pj.getChkBox().setSelected(pj.isSelected());
            pj.getChkBox().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pj.setSelected(pj.getChkBox().isSelected());
//                    System.out.println("Clicked check box at " + pj.getName()
//                            + ", plays set to " + pj.isPlays());
                }
            });
            this.add(pj.getChkBox(), gbc);
            gbc.gridx -= 1;

        }
//        this.add(this, gbc);
    }

    public void clean() {
        for (int i = 0; i < league.players.size(); i++) {
            Player pj = league.players.get(i);
            pj.setSelected(false);
            pj.getChkBox().setSelected(false);
        }
        this.repaint();
    }

    public void selectAll() {
        for (int i = 0; i < league.players.size(); i++) {
            Player pj = league.players.get(i);
            if (!pj.isPlayed()) {
                pj.getChkBox().setSelected(true);
                pj.setSelected(true);
            }
        }
        this.repaint();
    }

    public void reset() {
        for (int i = 0; i < league.players.size(); i++) {
            Player pj = league.players.get(i);
            pj.getChkBox().setEnabled(true);
            pj.getLabel().setOpaque(false);
            pj.setPlayed(false);
        }
        this.repaint();
    }
}

class PanelConsole extends JPanel {

    FifaRandom league;
    PanelPlayers pnlPlayers;

    public PanelConsole(FifaRandom league, PanelPlayers pnlPlayers) {
        this.league = league;
        this.pnlPlayers = pnlPlayers;
        setup();
    }

    public void setup() {
//        this.add(new JLabel("-------------- PANEL CONSOLE --------------"));
        this.setVisible(true);
        this.setBorder(new TitledBorder(" Options "));
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 3, 5, 3);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton btnFindRival = new JButton("Find Rival");
        btnFindRival.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player rival = league.findRival();
                if (!rival.getName().equals("You must select a player!")) {
                    rival.getLabel().setBackground(Color.YELLOW);
                    rival.getLabel().setOpaque(true);
                    rival.getChkBox().setEnabled(false);
                    rival.getChkBox().setSelected(false);
                    rival.setSelected(false);
                }
                JOptionPane.showMessageDialog(PanelConsole.this, rival.getName(),
                        "Rival Found", JOptionPane.INFORMATION_MESSAGE);
                pnlPlayers.repaint();
            }
        });
        this.add(btnFindRival, gbc);

        gbc.gridx = 1;
        JButton btnClean = new JButton("Clean");
        btnClean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlPlayers.clean();
            }
        });
        this.add(btnClean, gbc);

        gbc.gridx = 2;
        JButton btnSelAll = new JButton("Select All");
        btnSelAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlPlayers.selectAll();
            }
        });
        this.add(btnSelAll, gbc);

        gbc.gridx = 3;
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlPlayers.reset();
            }
        });
        this.add(btnReset, gbc);
    }
}

class PanelSettings extends JPanel {

    String path;
    FifaRandom league;
    Window window;

    public PanelSettings(FifaRandom league, Window w) {
        this.league = league;
        this.window = w;
        path = w.url;
        setup(path);
    }

    public void refreshWindow(String url) {
        league.persist();
        path = url;
        league = new FifaRandom(url);
        window.dispose();
        window = new Window(league);
        window.url = path;
    }

    public void setup(String url) {
        this.setVisible(true);
        this.setBorder(new TitledBorder(" Settings "));
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 3, 5, 3);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JFileChooser fc = new JFileChooser(url);
        JButton btnLoad = new JButton("Load File");
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fc.showOpenDialog(PanelSettings.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    path = fc.getSelectedFile().getAbsolutePath();
                    refreshWindow(league.getUrl());
                }
            }
        });
        this.add(btnLoad, gbc);

        gbc.gridx = 1;
        JButton btnAdd = new JButton("Add Player");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(window,
                        "Insert the name of the new player:", "Add player",
                        JOptionPane.INFORMATION_MESSAGE);
                if (input != null && input.length() > 0) {
                    league.addPlayer(input);
                    refreshWindow(league.getUrl());
                }
            }
        });
        this.add(btnAdd, gbc);

        gbc.gridx = 2;
        JButton btnRemove = new JButton("Remove Player");
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                league.removePlayers();
                refreshWindow(league.getUrl());
            }
        });
        this.add(btnRemove, gbc);
    }
}
