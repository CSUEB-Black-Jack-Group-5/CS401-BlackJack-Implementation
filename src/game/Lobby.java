package game;

public class Lobby {
    // not sure whether to transfer this into an array list or keep it as an array
    private Table[] tables;
    private int tableCount;
    private static final int TABLE_LIMIT = 100;
    // private ConnectionHandler connections according to the UML
    // figure out how to integrate with server connections / message response later?

    /* CONSTRUCTOR */
    public Lobby(){
        this.tables = new Table[TABLE_LIMIT];
    }

    /* RETURNS THE LIST OF TABLES */
    public Table[] getTables() {
        return tables;
    }

    /* RETURNS THE NUMBER OF TABLES IN THE LOBBY */
    public int getTableCount() {
        return tableCount;
    }

    /* RETURNS THE LIMIT OF TABLES IN A LOBBY */
    // remove this if converting lobby into an array list
    public int getTableLimit() {
        return TABLE_LIMIT;
    }

    /* CREATES A NEW TABLE AT "THE END" OF THE ARRAY */
    public void addTable(Dealer dealer) {
        tables[tableCount] = new Table(dealer);
        tableCount++;
    }

    // identify by dealer username
    // or maybe i should do table id?
    public void removeTable(Dealer dealer) {

        // is the table in the collection?
        int match = -1;		// will hold the index for the table if found
        // goes through the entire array looking for a matching table
        for (int i = 0; i < tableCount; i++) {
            // if there is a matching dealer at the table
            if (tables[i].getDealer().getUsername().equals(dealer.getUsername())) {
                match = i;	// stores the index
                break;		// and exits the loop
            }
        }

        // if the table exists within the lobby
        if (match > -1) {
            // calls the close session method in the table class (for logging purposes?)
            tables[match].closeSession();
            // and makes the table inaccessible and available for garbage collection
            // overwrites the "deleted" element with the next one over
            for (int i = match; i < tableCount-1; i++) {
                tables[i] = tables[i + 1];
            }
            tableCount--;			// changes the table count to be accurate
            tables[tableCount] = null;	/* not that it matters much
             * since it's inaccessible from the public interface, but
             * deallocates the original last element of the past array */
        }

        // v THIS IS JUST HERE FOR TESTING PURPOSES, I'LL REMOVE THIS WHEN THE FINAL DESIGN IS COMPLETE!!
        // if there was no table removed
        if (match == -1) {
            System.out.print("Table Remove Operation Failed.");
        }
    }

    @Override
    public String toString() {
        String text = "LOBBY:\n\n";

        for (int i = 0; i < tableCount; i++) {
            text += tables[i].toString();
        }

        return text;
    }
}
