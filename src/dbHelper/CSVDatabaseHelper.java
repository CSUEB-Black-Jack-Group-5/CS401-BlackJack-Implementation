package dbHelper;

import java.util.Scanner;
import java.io.*;
import java.util.UUID;

public class CSVDatabaseHelper {


    private static final String playerFile = System.getProperty("user.dir") + "/res/db/allPlayers/allPlayers.csv";

    // Synchronized method to add player to our "db"
    public synchronized static boolean addPlayer(String username, String password) {

        if(playerExists(username))return false;

        // generating uid can change if needed
        String uid = UUID.randomUUID().toString();
        double startingFunds = 1000.0;

        try{
            FileWriter myWriter = new FileWriter(playerFile,true);
            myWriter.write(username + "," + password + "," +  uid + "," + startingFunds +"\n");
            myWriter.close();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        // create folder and files based on player
        createPlayerFiles(username);
        return true;
    }

    public static boolean playerExists(String username) {
        File file = new File(System.getProperty("user.dir") + "/res/db/allPlayers/allPlayers.csv");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] userName = line.split(",");

                if (userName.length > 0 && userName[0].equals(username)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
    public static boolean dealerExists(String dealerName) {
        File file = new File(System.getProperty("user.dir") + "/res/db/allDealers.csv");
        try (Scanner scanner = new Scanner(file)){
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] userName = line.split(",");
                if (userName.length > 0 && userName[0].equals(dealerName)) {
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
