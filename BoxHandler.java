import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *  Implements ItemListener to handle Checkbox item updates
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
     *  Handles Checkbox being checked or unchecked
     *
     *  @param ItemEvent 
     */
    public void itemStateChanged(ItemEvent e)
    {
        int state = e.getStateChange();
        
        if(state == 1)
            gui.move((Checkbox)e.getItemSelectable(), 2);
        else if(state == 2)
            gui.move((Checkbox)e.getItemSelectable(), 1);
    }

}
