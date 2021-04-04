import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Main class for the Translator project, containing the GUI
 * @author Team 2 AC12002 2020-2021
 * @version 1.0
 */
public class Application {

    // Declaration of several UI components that need to be called on by several methods
    private Translator translator;
    private JFrame frame;
    private JLabel statusText;
    private JMenuItem saveDictButton;
    private JMenuItem editDictButton;
    private JButton changeDirButton;
    private JButton translateButton;
    private JCheckBox addUserTranslation;
    private JTextPane inputArea;
    private JTextPane outputArea;
    private JLabel translationText;
    private JFrame editorFrame;

    /**
     * Main method to initialise and start the Graphic User Interface on the EDT
     * @param args Not used
     */
    public static void main(String[] args) {
        Application app = new Application();

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
//            System.out.println("Could not set LookAndFeel to system default.");
//        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app.createWindow();
            }
        });
    }

    /**
     * Default constructor for the Application class, initialising the Translator
     */
    public Application(){

        translator = new Translator();

    }

    /**
     * Method creates the main window for the program and makes it visible
     */
    public void createWindow(){

        // Initialise frame
        frame = new JFrame("Translator");

        // Initialise a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Add "Menu" dropdown to menu bar
        JMenu mainMenu = new JMenu("Menu");
        // Add User Manual button and ActionListener
        JMenuItem aboutButton = new JMenuItem("Open User Manual");
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUserManual();
            }
        });
        // Add Close button and ActionListener
        JMenuItem closeButton = new JMenuItem("Close program");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Frame f: Frame.getFrames()) {
                    f.dispose();
                }
            }
        });
        mainMenu.add(aboutButton); mainMenu.add(closeButton);

        // Add "Dictionary" dropdown to menu bar
        JMenu dictMenu = new JMenu("Dictionary");
        // Add New Dictionary button and ActionListener
        JMenuItem newDictButton = new JMenuItem("New Dictionary");
        newDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newDictionaryPrompt();
            }
        });
        // Add Import Dictionary from Text File button and ActionListener
        JMenuItem newDictFileButton = new JMenuItem("Import Dictionary from Text File");
        newDictFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newDictionaryFilePrompt();
            }
        });
        // Add Edit Dictionary button and ActionListener
        editDictButton = new JMenuItem("View/Edit Dictionary");
        editDictButton.setEnabled(false);
        editDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditorFrame();
            }
        });
        // Add Save Dictionary button and ActionListener
        saveDictButton = new JMenuItem("Save Dictionary");
        saveDictButton.setEnabled(false);
        saveDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDictionary();
            }
        });
        // Add Load Dictionary button and ActionListener
        JMenuItem loadDictButton = new JMenuItem("Load Dictionary");
        loadDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDictionary();
            }
        });
        dictMenu.add(newDictButton); dictMenu.add(newDictFileButton); dictMenu.add(editDictButton); dictMenu.add(saveDictButton); dictMenu.add(loadDictButton);

        // Add "Translation" dropdown to menu bar
        JMenu transMenu = new JMenu("Translation");
        // Add Import Text button and ActionListener
        JMenuItem importTextButton = new JMenuItem("Import .txt file");
        importTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importTxtFile();
            }
        });
        // Add Export Text button and ActionListener
        JMenuItem exportTextButton = new JMenuItem("Export as .txt file");
        exportTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportTxtFile();
            }
        });
        transMenu.add(importTextButton); transMenu.add(exportTextButton);

        // Add dropdowns to menu bar and menu bar to frame
        menuBar.add(mainMenu); menuBar.add(dictMenu); menuBar.add(transMenu);
        frame.setJMenuBar(menuBar);

        // Create main panel and set layout manager
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());

        // Create status bar as GridBag container
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;

        // Add components to status bar
        statusText = new JLabel("No current dictionary selected...");
        statusBar.add(statusText,c);

        c.gridx = 1;
        changeDirButton = new JButton("Change direction");
        changeDirButton.setEnabled(false);
        changeDirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeDir();
            }
        });
        statusBar.add(changeDirButton,c);

        c.gridx = 0;
        c.gridy = 1;
        addUserTranslation = new JCheckBox("Add missing translations");
        statusBar.add(addUserTranslation,c);

        c.gridx = 1;
        translateButton = new JButton("Translate");
        translateButton.setEnabled(false);
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translateText();
            }
        });
        statusBar.add(translateButton,c);

        // Create inout panel as GridBag container
        JPanel inout = new JPanel();
        inout.setLayout(new GridBagLayout());

        // Add components to inout panel
        c.insets = new Insets(10,4,4,4);
        c.gridx = 0;
        c.gridy = 0;
        JLabel originalText = new JLabel("Original");
        inout.add(originalText,c);

        c.gridx = 1;
        translationText = new JLabel("Translation");
        inout.add(translationText,c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(4,4,4,4);

        c.gridx = 0;
        c.gridy = 1;
        inputArea = new JTextPane();
        JScrollPane inputScroll = new JScrollPane(inputArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inout.add(inputScroll,c);

        c.gridx = 1;
        outputArea = new JTextPane();
        outputArea.setEditable(false);
        outputArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        outputArea.setContentType("text/html");
        JScrollPane outputScroll = new JScrollPane(outputArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inout.add(outputScroll,c);

        main.add(statusBar, BorderLayout.NORTH);
        main.add(inout, BorderLayout.CENTER);

        // Finalise frame and set visible
        frame.setContentPane(main);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(720,480);
        frame.setMinimumSize(new Dimension(400,300));
        frame.setVisible(true);
    }

    /**
     * Opens "New Dictionary" prompt, creates and loads a new dictionary for the Translator
     */
    public void newDictionaryPrompt(){

        // Create new panel for dialog window
        JPanel dialog = new JPanel();
        dialog.setLayout(new GridLayout(3,2));
        dialog.setPreferredSize(new Dimension(300,75));

        // Add components to dialog panel
        JLabel name = new JLabel("Dictionary name: ");
        dialog.add(name);
        JTextField nameInput = new JTextField();
        dialog.add(nameInput);

        JLabel lang1 = new JLabel("Language 1: ");
        dialog.add(lang1);
        JTextField lang1Input = new JTextField();
        dialog.add(lang1Input);

        JLabel lang2 = new JLabel("Language 2: ");
        dialog.add(lang2);
        JTextField lang2Input = new JTextField();
        dialog.add(lang2Input);

        // Show dialog modal with custom panel
        JOptionPane.showMessageDialog(frame, dialog, "New dictionary...", JOptionPane.QUESTION_MESSAGE);

        // Validate input
        if(nameInput.getText().isBlank() || lang1Input.getText().isBlank() || lang2Input.getText().isBlank()){
            JOptionPane.showMessageDialog(frame, "Some of the fields were empty. No dictionary created.", "Error creating translation...", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // If successful, create new dictionary and set current, then update status bar
        Dictionary d = new Dictionary(nameInput.getText(),lang1Input.getText(),lang2Input.getText());
        translator.setDict(d);
        updateStatus();
    }

    /**
     * Opens "New Dictionary from Text File" prompt, creates and loads a new dictionary for the Translator based on txt file
     */
    public void newDictionaryFilePrompt() {

        // Creates panel for custom dialog
        JPanel dialog = new JPanel();
        dialog.setLayout(new GridLayout(3,2));
        dialog.setPreferredSize(new Dimension(300,75));

        // Add components to dialog panel
        JLabel name = new JLabel("Dictionary name: ");
        dialog.add(name);
        JTextField nameInput = new JTextField();
        dialog.add(nameInput);

        JLabel lang1 = new JLabel("Language 1: ");
        dialog.add(lang1);
        JTextField lang1Input = new JTextField();
        dialog.add(lang1Input);

        JLabel lang2 = new JLabel("Language 2: ");
        dialog.add(lang2);
        JTextField lang2Input = new JTextField();
        dialog.add(lang2Input);

        // Show dialog modal with custom panel
        JOptionPane.showMessageDialog(frame, dialog, "New dictionary...", JOptionPane.QUESTION_MESSAGE);

        // Validate input
        if(nameInput.getText().isBlank() || lang1Input.getText().isBlank() || lang2Input.getText().isBlank()){
            JOptionPane.showMessageDialog(frame, "Some of the fields were empty. No dictionary created.", "Error creating translation...", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Initialise File Chooser and set extension filter
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Formatted .txt files", "txt"));

        int returnVal = fc.showOpenDialog(frame);

        // Create dictionary from txt file
        if(returnVal == JFileChooser.APPROVE_OPTION){
            File file = fc.getSelectedFile();
            try {
                Dictionary d = new Dictionary(file, nameInput.getText(),lang1Input.getText(),lang2Input.getText());
                translator.setDict(d);
                updateStatus();
            } catch (InvalidFileFormatException e) {
                JOptionPane.showMessageDialog(frame, "You selected an incompatible file. The dictionary was not created.", "Error creating dictionary...", JOptionPane.WARNING_MESSAGE);
            }
        }
        // Show error message
        else{
            JOptionPane.showMessageDialog(frame, "You did not select a file. The dictionary was not created.", "Error creating dictionary...", JOptionPane.WARNING_MESSAGE);
        }

    }

    /**
     * Updates the information displayed in the status bar and unlocks some buttons
     */
    public void updateStatus(){

        // If no dictionary is selected, deactivate buttons
        if(translator.getDict() == null){
            statusText.setText("No current dictionary selected...");
            changeDirButton.setEnabled(false);
            translateButton.setEnabled(false);
            editDictButton.setEnabled(false);
            saveDictButton.setEnabled(false);
        }
        // Else, update status message and eneable buttons
        else{
            if(translator.getDir() == 0){
                statusText.setText("Translating from " + translator.getDict().getLanguageA() + " to " + translator.getDict().getLanguageB() + "...");
            }
            else{
                statusText.setText("Translating from " + translator.getDict().getLanguageB() + " to " + translator.getDict().getLanguageA() + "...");
            }
            changeDirButton.setEnabled(true);
            translateButton.setEnabled(true);
            editDictButton.setEnabled(true);
            saveDictButton.setEnabled(true);
        }

    }

    /**
     * Saves the current dictionary file as a serialised object using the .mydict file extension
     */
    public void saveDictionary(){

        // Show error if no dictionary is current
        if(translator.getDict() == null){
            JOptionPane.showMessageDialog(frame, "There is no current dictionary selected.", "Error saving dictionary...",  JOptionPane.WARNING_MESSAGE);
        }
        else{
            // Open new thread to improve GUI performance
            Thread t = new Thread(){
                @Override
                public void run(){
                    // Get dictionary
                    Dictionary d = translator.getDict();

                    // Create File Chooser and get save location from user
                    JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File(translator.getDict().getName() + ".mydict"));
                    int returnVal = fc.showSaveDialog(frame);

                    // Validate extension and write file to save location, show errors as needed
                    if(returnVal == JFileChooser.APPROVE_OPTION){
                        File file = fc.getSelectedFile();
                        String ext = file.getName().substring(file.getName().lastIndexOf("."));

                        if(ext.equals(".mydict")){
                            FileOutputStream fileOut;
                            ObjectOutputStream objOut;

                            try {
                                fileOut = new FileOutputStream(file);
                                objOut = new ObjectOutputStream(fileOut);
                                objOut.writeObject(d);
                                objOut.close();
                                fileOut.close();
                            } catch (FileNotFoundException e) {
                                JOptionPane.showMessageDialog(frame, "The selected path could not be found. Dictionary could not be saved.", "Error saving dictionary...", JOptionPane.WARNING_MESSAGE);
                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(frame, "Non-fatal error while saving. Dictionary could not be saved.", "Error saving dictionary...", JOptionPane.WARNING_MESSAGE);
                            }

                        }
                        else{
                            JOptionPane.showMessageDialog(frame, "This file extension is invalid. Please use the .mydict extension.", "Error saving dictionary...", JOptionPane.WARNING_MESSAGE);
                        }

                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "You did not select a file. The dictionary was not saved.", "Error saving dictionary...", JOptionPane.WARNING_MESSAGE);
                    }
                }
            };
            // Start the thread
            t.start();
        }

    }

    /**
     * Loads a dictionary from a .mydict file containing the serialised object
     */
    public void loadDictionary(){

        // Create new thread to improve GUI performance
        Thread t = new Thread(){
            @Override
            public void run(){
                // Create File Chooser and extension filter
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter(".mydict files", "mydict"));

                // Get file from user
                int returnVal = fc.showOpenDialog(frame);

                if(returnVal == JFileChooser.APPROVE_OPTION){

                    File file = fc.getSelectedFile();
                    String ext = file.getName().substring(file.getName().lastIndexOf("."));

                    // Validate extension and read in dictionary, then update status. Show errors where appropriate.
                    if(ext.equals(".mydict")){

                        FileInputStream fileIn;
                        ObjectInputStream objIn;

                        try {
                            fileIn = new FileInputStream(file);
                            objIn = new ObjectInputStream(fileIn);

                            Dictionary d = (Dictionary) objIn.readObject();
                            translator.setDict(d);
                            updateStatus();
                        } catch (FileNotFoundException e) {
                            JOptionPane.showMessageDialog(frame, "The selected path could not be found. Dictionary could not be loaded.", "Error loading dictionary...", JOptionPane.WARNING_MESSAGE);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(frame, "Non-fatal error while loading. Dictionary could not be loaded.", "Error loading dictionary...", JOptionPane.WARNING_MESSAGE);
                        } catch (ClassNotFoundException e) {
                            JOptionPane.showMessageDialog(frame, "Fatal error while loading. Your program might be corrupted.", "Error loading dictionary...", JOptionPane.WARNING_MESSAGE);
                        }


                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "This file extension is invalid. Please select .mydict files.", "Error loading dictionary...", JOptionPane.WARNING_MESSAGE);
                    }

                }
                else{
                    JOptionPane.showMessageDialog(frame, "You did not select a file. The dictionary was not loaded.", "Error loading dictionary...", JOptionPane.WARNING_MESSAGE);
                }
            }
        };
        // Start the thread
        t.start();

    }

    /**
     * Change the translation direction on the Translator
     */
    public void changeDir(){

        translator.toggleDir();
        updateStatus();

    }

    /**
     * Translates the user input with the options specified by the user
     */
    public void translateText(){
        // Create a new thread to improve GUI performance
        Thread t = new Thread(){
            @Override
            public void run(){
                // Get user input
                String original = inputArea.getText();
                String translation;

                // If live editing is disabled, translate the text and time the execution using Instants
                if(!addUserTranslation.isSelected()){
                    Instant start = Instant.now();
                    translation = translator.translate(original);
                    Instant end = Instant.now();
                    long ms = ChronoUnit.MILLIS.between(start,end);
                    translationText.setText("<html>Translation <i>"+ms+" ms elapsed</i></html>");
                }
                // If live editing is enabled, deactivate the timer
                else{
                    translation = translator.translateAskUser(original);
                    translationText.setText("<html>Translation <i>Timer disabled</i></html>");
                }
                // Update output area
                outputArea.setText("<html>" + translation + "</html>");
            }
        };
        //Start the thread
        t.start();

    }

    /**
     * Create and show the Edit Dictionary window
     */
    public void showEditorFrame(){

        // Create new thread to improve performance
        Thread t = new Thread(){
            @Override
            public void run(){
                // Initialise frame
                editorFrame = new JFrame("View / Edit dictionary");

                // Set panel for frame
                JPanel editorPanel = new JPanel();
                editorPanel.setLayout(new BorderLayout());

                // Create and setup scroll pane
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.getVerticalScrollBar().setUnitIncrement(20);
                // Get content for scrollPane
                scrollPane.setViewportView(getDictPanel());

                // Create and setup info bar
                JPanel infoBar = new JPanel();
                infoBar.setLayout(new FlowLayout(FlowLayout.LEADING,25,10));
                infoBar.setBackground(Color.DARK_GRAY);

                // Add label to info bar
                JLabel infoText = new JLabel("Showing translations from " + translator.getDict().getLanguageA() + " to " + translator.getDict().getLanguageB());
                infoText.setForeground(Color.WHITE);
                infoBar.add(infoText);

                // Add "Add translation" button to info bar
                JButton addTranslationButton = new JButton("Add translation");
                addTranslationButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addTranslationPrompt();
                        scrollPane.setViewportView(getDictPanel());
                    }
                });
                infoBar.add(addTranslationButton);

                // Add infoBar and scroll pane to respective viewports on editorPanel
                editorPanel.add(infoBar,BorderLayout.NORTH);
                editorPanel.add(scrollPane, BorderLayout.CENTER);

                // Finalise frame setup and set visible
                editorFrame.setContentPane(editorPanel);
                editorFrame.setSize(500,400);
                editorFrame.setMinimumSize(new Dimension(500,400));
                editorFrame.setVisible(true);
            }
        };
        // Start the thread
        t.start();

    }

    /**
     * Show prompt to add a translation to the dictionary
     */
    public void addTranslationPrompt() {

        // Create custom dialog pane and add components
        JPanel dialog = new JPanel();
        dialog.setLayout(new GridLayout(2,2));
        dialog.setPreferredSize(new Dimension(300,75));

        JLabel lang1 = new JLabel(translator.getDict().getLanguageA());
        dialog.add(lang1);
        JTextField lang1Input = new JTextField();
        dialog.add(lang1Input);

        JLabel lang2 = new JLabel(translator.getDict().getLanguageB());
        dialog.add(lang2);
        JTextField lang2Input = new JTextField();
        dialog.add(lang2Input);

        // Show dialog modal with custom panel
        JOptionPane.showMessageDialog(editorFrame, dialog, "New translation...", JOptionPane.QUESTION_MESSAGE);

        // Validate input
        if(lang1Input.getText().isBlank() || lang2Input.getText().isBlank()){
            JOptionPane.showMessageDialog(editorFrame, "Some of the fields were empty. No translation added.", "Error adding translation...", JOptionPane.WARNING_MESSAGE);
        }
        else{
            translator.getDict().add(lang1Input.getText(),lang2Input.getText());
        }

    }

    /**
     * Creates a JPanel that shows each translation stored in a dictionary
     * @return The JPanel displaying the dictionary contents
     */
    public JPanel getDictPanel() {

        // Initialise JPanel and set layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Iterate Dictionary for each item
        for (DictionaryItem i:translator.getDict()) {
            // Create FlowLayout panel showing the original, translation and a button to remove the item from the dictionary
            JPanel translationPanel = new JPanel();
            translationPanel.setLayout(new FlowLayout(FlowLayout.LEADING,25,10));
            JLabel langA = new JLabel(i.getOriginal());
            JLabel langB = new JLabel(i.getTranslation());
            JButton deleteButton = new JButton("X");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    translator.getDict().remove(i.getOriginal());
                    // Remove the GUI component from the mainPanel
                    mainPanel.remove(translationPanel);
                    mainPanel.revalidate();
                }
            });
            translationPanel.add(langA);
            translationPanel.add(langB);
            translationPanel.add(deleteButton);
            mainPanel.add(translationPanel);
        }

        // Return the completed panel
        return mainPanel;

    }

    /**
     * Prompts the user to select a .txt file to translate
     */
    public void importTxtFile(){
        // Create new thread to improve GUI performance
        Thread t = new Thread(){
            @Override
            public void run(){
                // Create File Chooser and extension filter
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt files", "txt");

                // Get file from user
                int returnVal = fc.showOpenDialog(frame);

                // Validate input, translate the text and show appropriate error modals if needed
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File input = fc.getSelectedFile();
                    String ext = input.getName().substring(input.getName().lastIndexOf("."));
                    if(ext.equals(".txt")){
                        try {
                            String text = Files.readString(Path.of(input.getAbsolutePath()));
                            inputArea.setText(text);
                            translateText();
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(frame, "Something went wrong reading the .txt file.", "Error importing text...", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "You need to provide a .txt file.", "Error importing text...", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        };
        // Start the thread
        t.start();

    }

    /**
     * Exports the translation into a user-specified .txt file
     */
    public void exportTxtFile(){

        // Check if there is a translation
        if(outputArea.getText().isBlank()){
            JOptionPane.showMessageDialog(frame, "There is nothing to be saved.", "Error exporting translation...",  JOptionPane.WARNING_MESSAGE);
        }
        else{
            // Create new thread to improve GUI performance
            Thread t = new Thread(){
                @Override
                public void run(){
                    // Create file chooser and get file from user
                    JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File("translation.txt"));
                    int returnVal = fc.showSaveDialog(frame);

                    // Validate user input and write String to .txt file, show error modals as needed
                    if(returnVal == JFileChooser.APPROVE_OPTION){
                        File file = fc.getSelectedFile();
                        String ext = file.getName().substring(file.getName().lastIndexOf("."));

                        if(ext.equals(".txt")){
                            try {
                                Files.writeString(Path.of(file.getAbsolutePath()),outputArea.getDocument().getText(0,outputArea.getDocument().getLength()));
                            } catch (IOException | BadLocationException e) {
                                JOptionPane.showMessageDialog(frame, "A non-fatal error occurred while exporting the translation and it may not have saved.", "Error exporting translation...", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(frame, "This file extension is invalid. Please use the .txt extension.", "Error exporting translation...", JOptionPane.WARNING_MESSAGE);
                        }

                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "You did not select a file. The translation was not saved.", "Error exporting translation...", JOptionPane.WARNING_MESSAGE);
                    }
                }
            };
            // Start the thread
            t.start();
        }

    }

    /**
     * Open the user manual pdf file in the default PDF viewer
     */
    public void openUserManual() {
        // Show warning message
        int returnVal = JOptionPane.showConfirmDialog(frame, "This will open the PDF User Manual located in the same directory as the program's .jar executable file.", "Open User Manual", JOptionPane.OK_CANCEL_OPTION);

        // Validate user input and attempt to open PDF
        if(returnVal == JOptionPane.OK_OPTION){
            try {
                File userMan = new File("UserManual.pdf");
                Desktop.getDesktop().open(userMan);
            } catch (IOException | IllegalArgumentException e) {
                JOptionPane.showMessageDialog(frame, "Could not locate PDF file.", "Error opening User Manual...", JOptionPane.WARNING_MESSAGE);
            }
        }

    }

}
