import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

/**
 *  Implements ActionListener to handle user input for adding
 *  new items to the selected file and displaying them
 *
 *  @author Lloyd Empedrado
 */
public class InputHandler implements ActionListener
{
    /** The GUI window */
    protected GUI gui;

    public InputHandler(GUI gui)
    {
        this.gui = gui;
    }

    /**
     *  Responds the user input from the GUI's TextField
     *
     *  @param event the user submitting the TextField content
     *
     *  @exception FileNotFoundException if the
     *  file to be read cannot be found
     */
    public void actionPerformed(ActionEvent event)    
    {
        try
        {
            File file = gui.file;
            String text = gui.input.getText() + System.lineSeparator();  //gets the content in the text field
            gui.input.setText("");  //clears the text field
            //input must meet a minimum length to be submitted
            if(text.length() > 3)
            {
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


}
