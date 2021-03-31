import com.sun.java.accessibility.util.Translator;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Main class for the Translator project, containing the GUI
 * @author Team 2
 * @version 1.0
 */
public class Application {

    private Translator t;

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

        t = new Translator();

    }

    public void createWindow(){

        JFrame frame = new JFrame("Translator");

        JMenuBar menuBar = new JMenuBar();

        JMenu mainMenu = new JMenu("Menu");
        JMenuItem aboutButton = new JMenuItem("About");
        JMenuItem closeButton = new JMenuItem("Close program");
        mainMenu.add(aboutButton); mainMenu.add(closeButton);

        JMenu dictMenu = new JMenu("Dictionary");
        JMenuItem newDictButton = new JMenuItem("New Dictionary");
        JMenuItem editDictButton = new JMenuItem("View/Edit Dictionary");
        JMenuItem saveDictButton = new JMenuItem("Save Dictionary");
        JMenuItem loadDictButton = new JMenuItem("Load Dictionary");
        dictMenu.add(newDictButton); dictMenu.add(editDictButton); dictMenu.add(saveDictButton); dictMenu.add(loadDictButton);

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

        JLabel statusText = new JLabel("No current dictionary selected...");
        statusBar.add(statusText,c);

        c.gridx = 1;
        JButton changeDir = new JButton("Change direction");
        changeDir.setEnabled(false);
        statusBar.add(changeDir,c);

        c.gridx = 0;
        c.gridy = 1;
        JCheckBox addUserTranslation = new JCheckBox("Add missing translations");
        statusBar.add(addUserTranslation,c);

        c.gridx = 1;
        JButton translateButton = new JButton("Translate");
        translateButton.setEnabled(false);
        statusBar.add(translateButton,c);

        JPanel inout = new JPanel();
        inout.setLayout(new GridBagLayout());

        c.insets = new Insets(10,4,4,4);
        c.gridx = 0;
        c.gridy = 0;
        JLabel originalText = new JLabel("Original");
        inout.add(originalText,c);

        c.gridx = 1;
        JLabel translationText = new JLabel("Translation");
        inout.add(translationText,c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(4,4,4,4);

        c.gridx = 0;
        c.gridy = 1;
        JTextArea inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        JScrollPane inputScroll = new JScrollPane(inputArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inout.add(inputScroll,c);

        c.gridx = 1;
        JTextArea outputArea = new JTextArea();
        outputArea.setLineWrap(true);
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

    public void newDictionaryPrompt(){

    }

}
