import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *  Implements ItemListener to handle JCheckBox item updates
 *  
 *  @author Lloyd Empedrado
 */
public class BoxHandler implements ItemListener
{
    /** The GUI window  */
    protected GUI gui;

    public BoxHandler(GUI gui)
    {
        this.gui = gui;
    }//BoxListener constructor

    /**
     *  Handles JCheckBox being checked or unchecked
     *
     *  @param ItemEvent 
     */
    public void itemStateChanged(ItemEvent e)
    {
        int state = e.getStateChange();

        //if an item is unchecked
        if(state == 2)
            gui.move((JCheckBox)e.getItemSelectable(), 1);
    }

}
