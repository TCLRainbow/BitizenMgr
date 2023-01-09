package chingdim.bitizenmgr.api;

public class Floor {
    private final String name;
    private final int number;
    private final int floorType;

    Floor(String name, int number, int floorType) {
        this.name = name;
        this.number = number;
        this.floorType = floorType;
    }


    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getFloorType() {
        return floorType;
    }
}
