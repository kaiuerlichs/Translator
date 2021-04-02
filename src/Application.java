import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Main class for the Translator project, containing the GUI
 * @author Team 2
 * @version 1.0
 */
public class Application {

    private Translator translator;
    private JFrame frame;
    private JLabel statusText;
    private JButton changeDirButton;
    private JButton translateButton;
    JCheckBox addUserTranslation;
    JTextPane inputArea;
    JTextPane outputArea;
    JLabel translationText;

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

    public Application(){

        translator = new Translator();

    }

    public void createWindow(){

        frame = new JFrame("Translator");

        JMenuBar menuBar = new JMenuBar();

        JMenu mainMenu = new JMenu("Menu");
        JMenuItem aboutButton = new JMenuItem("About");
        JMenuItem closeButton = new JMenuItem("Close program");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        mainMenu.add(aboutButton); mainMenu.add(closeButton);

        JMenu dictMenu = new JMenu("Dictionary");
        JMenuItem newDictButton = new JMenuItem("New Dictionary");
        newDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newDictionaryPrompt();
            }
        });
        JMenuItem newDictFileButton = new JMenuItem("Import Dictionary from File");
        newDictFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newDictionaryFilePrompt();
            }
        });
        JMenuItem editDictButton = new JMenuItem("View/Edit Dictionary");
        JMenuItem saveDictButton = new JMenuItem("Save Dictionary");
        saveDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDictionary();
            }
        });
        JMenuItem loadDictButton = new JMenuItem("Load Dictionary");
        loadDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDictionary();
            }
        });
        dictMenu.add(newDictButton); dictMenu.add(newDictFileButton); dictMenu.add(editDictButton); dictMenu.add(saveDictButton); dictMenu.add(loadDictButton);

        JMenu transMenu = new JMenu("Translation");
        JMenuItem importTextButton = new JMenuItem("Import .txt file");
        JMenuItem exportTextButton = new JMenuItem("Export as .txt file");
        transMenu.add(importTextButton); transMenu.add(exportTextButton);

        menuBar.add(mainMenu); menuBar.add(dictMenu); menuBar.add(transMenu);
        frame.setJMenuBar(menuBar);

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());

        JPanel statusBar = new JPanel();
        statusBar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;

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

        JPanel inout = new JPanel();
        inout.setLayout(new GridBagLayout());

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

        frame.setContentPane(main);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        frame.setMinimumSize(new Dimension(400,300));
        frame.setVisible(true);

    }

    private void newDictionaryFilePrompt() {

        JPanel dialog = new JPanel();
        dialog.setLayout(new GridLayout(3,2));
        dialog.setPreferredSize(new Dimension(300,75));

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

        JOptionPane.showMessageDialog(frame, dialog, "New dictionary...", JOptionPane.QUESTION_MESSAGE);

        while(nameInput.getText().isBlank() || lang1Input.getText().isBlank() || lang2Input.getText().isBlank()){
            JOptionPane.showMessageDialog(frame, dialog, "The fields cannot be blank...", JOptionPane.QUESTION_MESSAGE);
        }

        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Formatted .txt files", "txt"));

        int returnVal = fc.showOpenDialog(frame);

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
        else{
            JOptionPane.showMessageDialog(frame, "You did not select a file. The dictionary was not created.", "Error creating dictionary...", JOptionPane.WARNING_MESSAGE);
        }

    }

    public void newDictionaryPrompt(){

        JPanel dialog = new JPanel();
        dialog.setLayout(new GridLayout(3,2));
        dialog.setPreferredSize(new Dimension(300,75));

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

        JOptionPane.showMessageDialog(frame, dialog, "New dictionary...", JOptionPane.QUESTION_MESSAGE);

        while(nameInput.getText().isBlank() || lang1Input.getText().isBlank() || lang2Input.getText().isBlank()){
            JOptionPane.showMessageDialog(frame, dialog, "The fields cannot be blank...", JOptionPane.QUESTION_MESSAGE);
        }

        Dictionary d = new Dictionary(nameInput.getText(),lang1Input.getText(),lang2Input.getText());
        translator.setDict(d);
        updateStatus();

    }

    public void updateStatus(){

        if(translator.getDict() == null){
            statusText.setText("No current dictionary selected...");
            changeDirButton.setEnabled(false);
            translateButton.setEnabled(false);
        }
        else{
            if(translator.getDir() == 0){
                statusText.setText("Translating from " + translator.getDict().getLanguageA() + " to " + translator.getDict().getLanguageB() + "...");
            }
            else{
                statusText.setText("Translating from " + translator.getDict().getLanguageB() + " to " + translator.getDict().getLanguageA() + "...");
            }
            changeDirButton.setEnabled(true);
            translateButton.setEnabled(true);
        }

    }

    public void saveDictionary(){
        if(translator.getDict() == null){
            JOptionPane.showMessageDialog(frame, "There is no current dictionary selected.", "Error saving dictionary...",  JOptionPane.WARNING_MESSAGE);
        }
        else{
            Thread t = new Thread(){
                @Override
                public void run(){
                    Dictionary d = translator.getDict();

                    JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File(translator.getDict().getName() + ".mydict"));
                    int returnVal = fc.showSaveDialog(frame);

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
            t.start();
        }

    }

    public void loadDictionary(){
        Thread t = new Thread(){
            @Override
            public void run(){
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter(".mydict files", "mydict"));

                int returnVal = fc.showOpenDialog(frame);

                if(returnVal == JFileChooser.APPROVE_OPTION){

                    File file = fc.getSelectedFile();
                    String ext = file.getName().substring(file.getName().lastIndexOf("."));

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
                    JOptionPane.showMessageDialog(frame, "You did not select a file. The dictionary was not loadad.", "Error loading dictionary...", JOptionPane.WARNING_MESSAGE);
                }
            }
        };
        t.start();
    }

    public void changeDir(){

        translator.toggleDir();
        updateStatus();

    }

    public void translateText(){
        Thread t = new Thread(){
            @Override
            public void run(){
                String original = inputArea.getText();
                String translation;

                if(!addUserTranslation.isSelected()){
                    Instant start = Instant.now();
                    translation = translator.translate(original);
                    Instant end = Instant.now();
                    long ms = ChronoUnit.MILLIS.between(start,end);
                    translationText.setText("<html>Translation <i>"+ms+" ms elapsed</i></html>");
                }
                else{
                    translation = translator.translateAskUser(original);
                    translationText.setText("<html>Translation <i>Timer disabled</i></html>");
                }
                outputArea.setText("<html>" + translation + "</html>");
            }
        };
        t.start();
    }

}
