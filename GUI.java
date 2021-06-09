import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.border.TitledBorder;

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
    protected JScrollPane leftScroll = new JScrollPane(left);
    protected JPanel right = new JPanel();
    protected JScrollPane rightScroll = new JScrollPane(right);
    
    /** Item input field */
    protected JTextField input = new JTextField(25);
    protected File file;
    protected String fileContents;

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

        initialize();
        content.setVisible(false);

        setVisible(true);
    }

    
    /**
     *  Initializes JPanels for user operations and to display file contents
     */
    public void initialize()
    { 
        //creates two sections, the top having user operations and the bottom having the separated file content
        content = getContentPane();
        content.setLayout(new GridLayout(2, 0));
        content.add(top);//, new Dimension(getWidth(), (int)(getHeight() * 0.2)));
        content.add(bottom);

        //user operation panel
        top.setLayout(new FlowLayout(FlowLayout.CENTER));
        //top.setPreferredSize(new Dimension((int)getSize().getWidth(), 100));
        InputHandler ih = new InputHandler(this);
        input.addActionListener(ih);
        top.add(input);

        JButton button = new JButton("Add");
        ButtonHandler bh = new ButtonHandler(this);
        button.addActionListener(bh);
        top.add(button);

        button = new JButton("Edit");
        button.addActionListener(bh);
        top.add(button);


        //adds bottom panel for file contents and adds two scrollpanes for the left and right panels
        bottom.setLayout(new GridLayout(0, 2));
        bottom.add(leftScroll);
        bottom.add(rightScroll);


        //panel titles
        TitledBorder title1 = new TitledBorder("TO-DO");
        title1.setTitleJustification(TitledBorder.CENTER);
        title1.setTitlePosition(TitledBorder.TOP);
        leftScroll.setBorder(title1);
        TitledBorder title2 = new TitledBorder("COMPLETED");
        title2.setTitleJustification(TitledBorder.CENTER);
        title2.setTitlePosition(TitledBorder.TOP);
        rightScroll.setBorder(title2);
        

        //to-do panel
        left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
        leftScroll.setLayout((new ScrollPaneLayout()));
        leftScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        leftScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        //completed panel
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
        rightScroll.setLayout(new ScrollPaneLayout());
        rightScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rightScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


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
        content.setVisible(true);
        BoxHandler bl = new BoxHandler(this);
        try
        {
            //clears JPanels if try to display contents of a different file
            left.removeAll();
            right.removeAll();

            //size of each checkbox
            Dimension size = new Dimension(left.getWidth(), 10);

            //opens file and reads it line by line
            Scanner reader = new Scanner(f);
            //initialize fileContents
            fileContents = "";
            while(reader.hasNextLine())
            {
                String line = reader.nextLine();
                //creates a Checkbox from the line and adds it to the appropriate panel
                if(line.charAt(0) == '*')
                {
                    Checkbox box = new Checkbox(line.substring(1), true);
                    box.setPreferredSize(size);
                    box.addItemListener(bl);
                    right.add(box);
                }
                else
                {
                    Checkbox box = new Checkbox(line, false);
                    box.setPreferredSize(size);
                    box.addItemListener(bl);
                    left.add(box);
                }

                //append the line to fileContents
                fileContents += line + System.lineSeparator();
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
        String name = box.getLabel();
        //unmarking a Checkbox as incomplete/to-do
        if(to == 1)
        {
            //leave box marked until verification
            box.setState(true);

            //request verification
            int confirmation = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove \"" + name + "\"?", "Confirmation", JOptionPane.YES_NO_OPTION);
            
            //JOPtionPane.YES_OPTION == 0
            if(confirmation == 0)
            {
                box.setState(false);
                //remove the * from the beginning of the line of the box's label
                String temp = fileContents.substring(0, fileContents.indexOf(name) - 1);
                temp += fileContents.substring(fileContents.indexOf(name));
                write(temp);
                System.out.println("Moved " + box.getLabel() + " to To-Do"); 
            }
        }
        //marking a Checkbox as complete
        else if(to == 2)
        {
            //add a * to the beginning of the line of the box's label
            String temp = fileContents.substring(0, fileContents.indexOf(name));
            temp += "*" + fileContents.substring(fileContents.indexOf(name));
            write(temp);
            System.out.println("Moved " + box.getLabel() + " to Completed");
        }
    }//move method

    /**
     *  Rewrites the contents of a file
     *
     *  @param content the content being written to the file
     */
    public void write(String content)
    {
        //write the new file contents to the file
        try
        {
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        }
        catch(IOException ioe)
        {
            System.out.println("Error writing to file");
        }

        //display updated file
        display(file);
    }//write method

}//GUI class
