import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.border.TitledBorder;

//checkboxes have too much space between them, i think they change to fill panel
//title border doesnt span the full panel width, need to double check


/**
 *  GUI
 *
 *  @author Lloyd Empedrado
 */
public class GUI extends JFrame
{
    /** GUI content */
    protected Container content;
    
    /** User control interface */
    protected JPanel top = new JPanel();
    
    /** Display interface */
    protected JPanel bottom = new JPanel();
    
    /** To-Do and Completed item displays */
    protected JPanel left = new JPanel();
    protected JPanel right = new JPanel();
    
    /** Item input field */
    protected JTextField input = new JTextField(25);
    protected File file;

    /**
     *  GUI constructor
     *  creates the JFrame that can open a file to display
     */
    public GUI()
    {
        //initalizes dimensions and location of the window
        setSize(950, 700);
        setLocation(100, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //adds a menu bar to open a file to read from
        JMenuBar menuBar = new JMenuBar();
        FileMenuHandler fmh = new FileMenuHandler(this);
        JButton open = new JButton("Open");
        open.addActionListener(fmh);
        menuBar.add(open);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    
    /**
     *  Initializes JPanels for user operations and to display file contents
     */
    public void initialize()
    { 
        //creates two sections, the top having user operations and the bottom having the separated file content
        content = getContentPane();
        content.setLayout(new FlowLayout());
        content.add(top);
        content.add(bottom);

        //user operation panel
        top.setPreferredSize(new Dimension((int)getSize().getWidth(), 100));
        InputHandler ih = new InputHandler(this);
        input.addActionListener(ih);
        top.add(input);
        
        //adds bottom panel for file contents and adds two more panels for the left and right
        bottom.setLayout(new BorderLayout());
        bottom.add(left, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);


        //panel titles
        TitledBorder title1 = new TitledBorder("TO-DO");
        title1.setTitleJustification(TitledBorder.CENTER);
        title1.setTitlePosition(TitledBorder.TOP);
        left.setBorder(title1);
        TitledBorder title2 = new TitledBorder("COMPLETED");
        title2.setTitleJustification(TitledBorder.CENTER);
        title2.setTitlePosition(TitledBorder.TOP);
        right.setBorder(title2);
        

        //to-do panel
        left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
        left.setAutoscrolls(true);
        
        //completed panel
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
        right.setAutoscrolls(true);

        setVisible(true);
    }

    /**
     *  Reads in the passed file and creates 
     *  Checkbox items to add to the dispay panels.
     *  
     *  @param f File to be read
     *
     *  @exception FileNotFoundException if the
     *  file to be read cannot be found
     */
    public void display(File f)
    {
        file = f;
        initialize();
        BoxHandler bl = new BoxHandler(this);
        try
        {
            //clears JPanels if try to display contents of a different file
            left.removeAll();
            right.removeAll();

            //opens file and reads it line by line
            Scanner reader = new Scanner(f);
            while(reader.hasNextLine())
            {
                String line = reader.nextLine();
                //creates a Checkbox from the line and adds it to the appropriate panel
                if(line.charAt(0) == '*')
                {
                    Checkbox box = new Checkbox(line.substring(1), true);
                    box.addItemListener(bl);
                    right.add(box);
                }
                else
                {
                    Checkbox box = new Checkbox(line, false);
                    box.addItemListener(bl);
                    left.add(box);
                }
            } 
            reader.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File " + f.getName() + " could not be found");
        }

        setVisible(true);
    }//display method

    /**
     *  Moves a marked Checkbox item to the Completed panel
     *  or moves an unmarked Checkbox item to the To-Do panel.
     *  Unmarking a Checkbox requires further verification before proceeding.
     *
     *  @param Checkbox the item to be moved
     *  @param to indicates which panel to move the Checkbox to
     */
    public void move(Checkbox box, int to)
    {
        
    }//move method

}//GUI class
