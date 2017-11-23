/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fifa.random;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

/**
 *
 * @author esteban.duran
 */
public class FifaRandom {

    public ArrayList<Player> players;
    File file;
    BufferedReader reader;
    BufferedWriter writer;
    List<String> lines;
    String url;
    public static final String ENCODE = "UTF-8";

    public Player findRival() {
        ArrayList<Player> copyPjs = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            Player pj = players.get(i);
            if (pj.isSelected() && !pj.isPlayed()) {
                copyPjs.add(pj);
            }
        }

        SecureRandom sr = new SecureRandom();
        int random = 0;
        if (copyPjs.size() > 1) {
            random = sr.nextInt(copyPjs.size());
        }
        if (copyPjs.isEmpty()) {
            return new Player("You must select a player!");
        } else {
            for (int i = 0; i < copyPjs.size(); i++) {
                Player pj = copyPjs.get(i);
                if (i == random) {
                    pj.setPlayed(true);
                    return pj;
                }
            }
        }
        return null;
    }

    public FifaRandom(String url) {
        this.url = url;
        reader = null;
        writer = null;
        try {
            file = new File(url);
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), ENCODE));

            players = new ArrayList<>();
            while (reader.ready()) {
                String line = reader.readLine();
                if (line != null && !line.isEmpty() && line.contains(";")) {
                    String[] split = line.split(";");
                    Player pj = new Player(split[0]);
                    pj.setPlayed(Boolean.parseBoolean(split[1]));
                    pj.setChkBox(new JCheckBox());
                    pj.setLabel(new JLabel(pj.getName()));
                    if(pj.isPlayed()){
                        pj.getChkBox().setEnabled(false);
                        pj.getLabel().setOpaque(true);
                        pj.getLabel().setBackground(Color.YELLOW);
                    }
                    players.add(pj);
                }
            }
            reader.close();
            players.sort(new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FifaRandom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FifaRandom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean addPlayer(String name) {
        if (name != null && !name.isEmpty()) {
            Player pj = new Player(name);
            return players.add(pj);
        }
        return false;
    }

    public void removePlayers() {
        for (int i = 0; i < players.size(); i++) {
            Player pj = players.get(i);
            if (pj.isSelected()) {
                players.remove(i);
                i--;
            }
        }
    }

    public void persist() {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), ENCODE));

            for (Player player : players) {
                writer.append(player.getName() + ";" + player.isPlayed() + "\r\n");
            }
            
            writer.close();
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            Logger.getLogger(FifaRandom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FifaRandom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
}
