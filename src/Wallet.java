public class Wallet {
    private float funds;

    public Wallet() {
        this.funds = 0.0f;
    }

    /**
     * @param delta How much money to add to the wallet
     * @apiNote delta is expected to be greater than or equal to 0
     */
    void addFunds(float delta) {
        assert delta >= 0;          // NOTE: I'm not sure if this is absolutely necessary - Riley Fischer
        this.funds += delta;
    }

    /**
     * @param delta How much money to remove from the wallet
     * @apiNote delta is expected to be less than or equal to 0
     */
    void removeFunds(float delta) {
        assert delta <= 0;          // NOTE: I'm not sure if this is absolutely necessary - Riley Fischer
        this.funds += delta;
    }

    /**
     * @return The total funds amount of this wallet
     */
    float getFunds() {
        return funds;
    }
}
