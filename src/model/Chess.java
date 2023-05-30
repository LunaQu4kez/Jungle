package model;


public class Chess {

    private PlayerColor owner;

    private String name;
    public int rank;

    public Chess(PlayerColor owner, String name, int rank) {
        this.owner = owner;
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public PlayerColor getOwner() {
        return owner;
    }
}
