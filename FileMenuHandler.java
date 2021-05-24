import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 *  Implements ActionListener to respond to user actions
 *  to open a file chooser to read in the selected file
 *
 *  @author Lloyd Empedrado
 */
public class FileMenuHandler implements ActionListener 
{
    protected GUI gui;
    protected JFileChooser fc = new JFileChooser("./categories");
   
    public FileMenuHandler (GUI gui) 
    {
        this.gui = gui;
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }//FileMenuHandler constructor

    /**
     *  Responds to the user event performed in the JFrame's file menu
     *
     *  @param event the event the user performs in the JFrame
     */
    public void actionPerformed(ActionEvent event) 
    {
        String menuName = event.getActionCommand();

        //opens a JFileChooser for the user to choose a file to read in
        if (menuName.equals("Open"))
        {
            //opens the file finder
            fc.showOpenDialog(null);

            //gets the path to the selected file
            String file = fc.getSelectedFile().getName();
            
            //sets the title of the GUI
            String title = file.substring(0, file.indexOf("."));
            gui.setTitle(title.toUpperCase());
            
            //call display method off the GUI to display file contents
            File f = new File(fc.getSelectedFile().getAbsolutePath());
            gui.display(f);
        }
    } //actionPerformed

}//FileMenuHandler class
