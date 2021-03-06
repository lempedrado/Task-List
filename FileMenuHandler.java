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
        if(menuName.equals("Open"))
        {
            //opens the file finder
            int option = fc.showOpenDialog(null);

            if(option == JFileChooser.CANCEL_OPTION)
                System.out.println("File Chooser was cancelled");
            else if(option == JFileChooser.APPROVE_OPTION)
            {
                //gets the path to the selected file
                String file = fc.getSelectedFile().getName();
                
                //removes the file extension and sets it as the title of the GUI
                String title = file.substring(0, file.lastIndexOf("."));
                gui.setTitle(title.toUpperCase());
                
                //call display method off the GUI to display file contents
                File f = new File(fc.getSelectedFile().getAbsolutePath());
                gui.display(f);
            }
        }
        else if(menuName.equals("New"))
        {
            String fileName = JOptionPane.showInputDialog(null, "Enter a name for the file you want to create.");
            //add a file extension if there isn't one
            if(fileName.indexOf(".") == -1)
                fileName += ".txt";
            //create the new file
            File f = new File("./categories/" + fileName);
            try
            {
                f.createNewFile();
            }
            catch(IOException e)
            {
                System.out.println("An error occurred creating the file.\n" + e);
            }
            gui.setTitle(fileName.substring(0, fileName.lastIndexOf(".")).toUpperCase());
            gui.display(f);
        }
    } //actionPerformed

}//FileMenuHandler class
