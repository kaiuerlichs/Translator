public class Test {

    /**
     * Main method of the program, starting the application flow
     * @param args not used
     */
    public static void main(String[] args) {

        Dictionary d = new Dictionary("Test", "a", "b");

        //adding items to dictionary test
        boolean result = d.add("a"  , "b");
        boolean result2 = d.add ("c", "d");
        boolean result3 = d.add ("a","b");

        System.out.println("Adding item successful: " + result);
        System.out.println("Adding item successful: " + result2);
        System.out.println("Adding item successful: " + result3);

        //boolean findResult = d.find("a");

        //removing item from dictionary test
        boolean removeResult = d.remove ("a");
        boolean removeResult2 = d.remove ("a");

        System.out.println("Removing item successful: " + removeResult);
        System.out.println("Removing item successful: " + removeResult2);

    }


}
