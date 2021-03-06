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
    protected JPanel input = new JPanel();
    protected JTextField textField = new JTextField(25);
    protected File file;
    protected String fileContents;

    protected JPanel buttons = new JPanel();
    protected JTextPane decision = new JTextPane();

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
        //opens a JFileChooser to select a file to read from
        JButton open = new JButton("Open");
        open.addActionListener(fmh);
        menuBar.add(open);

        //create a new file to add items to
        JButton newFile = new JButton("New");
        newFile.addActionListener(fmh);
        menuBar.add(newFile);
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
        top.setLayout(new GridLayout(3, 0));
        //top.setPreferredSize(new Dimension((int)getSize().getWidth(), 100));
        InputHandler ih = new InputHandler(this);
        textField.addActionListener(ih);

        //handles user input
        top.add(input);
        input.add(textField);

        JButton button = new JButton("Add");
        ButtonHandler bh = new ButtonHandler(this);
        button.addActionListener(bh);
        input.add(button);

        //operation buttons
        top.add(buttons);
        button = new JButton("Edit");
        button.addActionListener(bh);
        buttons.add(button);

        button = new JButton("Remove");
        button.addActionListener(bh);
        buttons.add(button);

        button = new JButton("Complete");
        button.addActionListener(bh);
        buttons.add(button);

        button = new JButton("Choose");
        button.addActionListener(bh);
        buttons.add(button);

        //displays random choice from incomplete items
        top.add(decision);
        decision.setEditable(false);

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
     *  JCheckBox items to add to the dispay panels.
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
            left.repaint();
            right.repaint();

            //opens file and reads it line by line
            Scanner reader = new Scanner(f);
            //initialize fileContents
            fileContents = "";
            while(reader.hasNextLine())
            {
                String line = reader.nextLine();

                if(line.strip() == "")
                    continue;

                //creates a JCheckBox from the line and adds it to the appropriate panel
                if(line.charAt(0) == '*')
                {
                    //create a checkbox with a strikethrough label
                    JCheckBox box = new JCheckBox("<html><s>" + line.substring(1) + "</s></html>", true);
                    box.addItemListener(bl);
                    right.add(box);
                }
                else
                {
                    JCheckBox box = new JCheckBox(line, false);
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
     *  Moves a marked JCheckBox item to the Completed panel
     *  or moves an unmarked JCheckBox item to the To-Do panel.
     *  Unmarking a JCheckBox requires further verification before proceeding.
     *
     *  @param box the JCheckBox to be moved
     *  @param to indicates which panel to move the JCheckBox to
     */
    public void move(JCheckBox box, int to)
    {
        String name = box.getText();
        //unmarking a JCheckBox as incomplete/to-do
        if(to == 1)
        {
            //leave box marked until verification
            box.setEnabled(true);

            //remove html formatting from the JCheckBox label
            name = name.substring(name.indexOf("<s>") + 3, name.indexOf("</s>"));

            //request verification
            int confirmation = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove \"" + name + "\"?", "Confirmation", JOptionPane.YES_NO_OPTION);

            //JOptionPane.YES_OPTION == 0
            if(confirmation == 0)
            {
                box.setEnabled(false);
                //remove the * from the beginning of the line of the box's label
                String temp = fileContents.substring(0, fileContents.indexOf(name) - 1);
                temp += fileContents.substring(fileContents.indexOf(name));
                write(temp);
                System.out.println("Moved " + name + " to To-Do");
            }
        }
        //marking a JCheckBox as complete
        else if(to == 2)
        {
            //add a * to the beginning of the line of the box's label
            String temp = fileContents.substring(0, fileContents.indexOf(name));
            temp += "*" + fileContents.substring(fileContents.indexOf(name));
            write(temp);
            System.out.println("Moved " + box.getText() + " to Completed");
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
