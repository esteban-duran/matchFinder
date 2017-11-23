/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fifa.random;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

public class Player{

    private String name;
    private boolean selected;
    private boolean played;
    private JCheckBox chkBox;
    private JLabel label;
    

    public Player(String name) {
        this.name = name;
        this.selected = false;
        this.played = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public JCheckBox getChkBox() {
        return chkBox;
    }

    public void setChkBox(JCheckBox chkBox) {
        this.chkBox = chkBox;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }
    
}
