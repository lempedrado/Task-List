import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 *  Implements ActionListener to handle buttons
 *  in the user operation panel
 *
 *  @author Lloyd Empedrado
 */
public class ButtonHandler implements ActionListener
{
    /** The GUI window */
    protected GUI gui;

    public ButtonHandler(GUI gui)
    {
        this.gui = gui;
    }

    /**
     *  Responds the buttons in the GUI
     *
     *  @param event the user submitting the TextField content
     *
     *  @exception FileNotFoundException if the
     *  file to be read cannot be found
     */
    public void actionPerformed(ActionEvent event)    
    {
        String name = event.getActionCommand();
        //add the text input to the file and display the updated file
        if(name.equals("Add"))
        {
            try
            {
                File file = gui.file;
                //get the content in the text field
                String text = gui.textField.getText() + System.lineSeparator();
                //input must meet a minimum length to be submitted
                if(text.length() > 3)
                {
                    //clears the text field
                    gui.textField.setText("");
                    //append input to the file
                    Path path = Paths.get(file.toString());
                    Files.write(path, text.getBytes(), StandardOpenOption.APPEND);

                    //call display method off the GUI to display the file contents
                    gui.display(file);
                }
            }
            catch(FileNotFoundException e)
            {
                System.out.println("File " + gui.file.getName() + " not found.");
            }
            catch(IOException ioe)
            {
                System.out.println("Error writing to file.");
            }
        }
        //displays a pop-up for each checked item to edit the label
        else if(name.equals("Edit"))
        {
            for(Component c : gui.left.getComponents())
                if(((Checkbox)(c)).getState())
                {
                    //get the new name for this item
                    String label = ((Checkbox)c).getLabel();
                    String newName = JOptionPane.showInputDialog("Enter a new name for " + label, label);
                    //if option pane is cancelled or closed make no changes
                    if(newName == null)
                        continue;
                    
                    //compile new file contents
                    String temp = "";
                    File file = gui.file;
                    try
                    {
                        Scanner reader = new Scanner(file);
                        while(reader.hasNextLine())
                        {
                            String line = reader.nextLine();
                            //replace the old name with the new name
                            if(line.equals(label))
                                temp += newName + System.lineSeparator();
                            //append the line to temp
                            else
                                temp += line + System.lineSeparator();
                        }
                        reader.close();
                        //write new content to the file
                        gui.write(temp);
                    }
                    catch(FileNotFoundException e)
                    {
                        System.out.println("File " + file.getName() + " could not be found");
                    }
                }
        }
        //removes the selected items
        else if(name.equals("Remove"))
        {
            //parse file and if line == label continue
            for(Component c : gui.left.getComponents())
                if(((Checkbox)(c)).getState())
                {
                    String temp = "";
                    File file = gui.file;
                    try
                    {
                        Scanner reader = new Scanner(file);
                        while(reader.hasNextLine())
                        {
                            String line = reader.nextLine();

                            //skip the line to be removed
                            if(line.equals(((Checkbox)c).getLabel()))
                                continue;

                            //append the line to temp
                            temp += line + System.lineSeparator();
                        }
                        reader.close();
                        gui.write(temp);
                    }
                    catch(FileNotFoundException e)
                    {
                        System.out.println("File " + file.getName() + " could not be found");
                    }
                }
        }
        //marks checked items as complete and moves it to the right panel
        else if(name.equals("Complete"))
        {
            //for each checkbox in left panel if status==true call gui.move()
            for(Component c : gui.left.getComponents())
                if(((Checkbox)(c)).getState())
                    gui.move((Checkbox)c, 2);
        }
        //chooses and displays a random item from the incomplete items
        else if(name.equals("Choose"))
        {
            int choice = (int)(Math.random() * gui.left.getComponentCount());
            String label = ((Checkbox)(gui.left.getComponent(choice))).getLabel();
            gui.decision.setText(label);
            gui.setVisible(true);
        }
    }//actionPerformed method

}//ButtonHandler class
