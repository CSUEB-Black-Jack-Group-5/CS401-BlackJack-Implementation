package game;

public class User {
    private String username;
    private String password;
    private Wallet wallet;
    private Table parentTable;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallet = new Wallet();
        this.parentTable = null;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setParentTable(Table table) {
        this.parentTable = table;
    }

    public Table getParentTable() {
        return parentTable;
    }

    public void resetTable() {
        this.parentTable = null;
    }

    // TODO: Add more logic once Table and other classes are set up.
}
