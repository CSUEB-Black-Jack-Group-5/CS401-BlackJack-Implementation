package dbHelper;

import java.util.Scanner;
import java.io.*;


public class CSVDatabaseHelper {


    private static final String allPlayerFile = System.getProperty("user.dir") + "/res/db/allPlayers/allPlayers.csv";

    // Synchronized method to add player to our "db"
    public synchronized static boolean addPlayer(String username, String password) {

        if(playerExists(username,password))return false;


        double startingFunds = 1000.0;

        try{
            FileWriter myWriter = new FileWriter(allPlayerFile,true);
            myWriter.write(username + "," + password + "," + startingFunds +"\n");
            myWriter.close();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        // create folder and files based on player
        createPlayerFiles(username);
        return true;
    }
    // this does auth
    public static boolean playerExists(String username,String password) {
        File file = new File(System.getProperty("user.dir") + "/res/db/allPlayers/allPlayers.csv");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] dbLineContent = line.split(",");
                if (dbLineContent.length > 0 && dbLineContent[0].equals(username) && dbLineContent[1].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
    public static boolean dealerExists(String dealerName,String password) {
        File file = new File(System.getProperty("user.dir") + "/res/db/allDealers.csv");
        try (Scanner scanner = new Scanner(file)){
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] dbLineContent = line.split(",");
                if (dbLineContent.length > 0 && dbLineContent[0].equals(dealerName) && dbLineContent[1].equals(password)) {
                    return true;
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return false;

    }
    // creates a new dir for each new player created to keep track of their data
    public static void createPlayerFiles(String userName) {

        File userDir = new File(System.getProperty("user.dir") + "/res/db/Players/" + userName);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        try {
            new File(userDir, userName + "_data.txt").createNewFile();
            new File(userDir, userName + "_history.txt").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
