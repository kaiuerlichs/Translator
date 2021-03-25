import javax.swing.*;

/**
 * Main class for the Translator project, containing the GUI
 * @author Team 2
 * @version 1.0
 */
public class Application {



//    public static void main(String[] args) {
//        Application app = new Application();
//        javax.swing.SwingUtilities.invokeLater(new Runnable(){
//            public void run(){
//                app.runApplication();
//            }
//        });
//    }

    public Application(){

    }

    public void runApplication(){
        createFrame();
    }

    public void createFrame(){

        JFrame window = new JFrame();
        window.setTitle("Translator");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500,400);

        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Menu");
        JMenu dictMenu = new JMenu("Dictionary");

        JMenuItem m1 = new JMenuItem("About");
        JMenuItem m2 = new JMenuItem("Close program");
        mainMenu.add(m1);
        mainMenu.add(m2);

        JMenuItem d1 = new JMenuItem("New dictionary");
        JMenuItem d2 = new JMenuItem("Save dictionary");
        JMenuItem d3 = new JMenuItem("Load dictionary");
        JMenuItem d4 = new JMenuItem("Edit dictionary");
        dictMenu.add(d1);
        dictMenu.add(d2);
        dictMenu.add(d3);
        dictMenu.add(d4);

        menuBar.add(mainMenu);
        menuBar.add(dictMenu);
        window.setJMenuBar(menuBar);
        window.setVisible(true);

    }

}
