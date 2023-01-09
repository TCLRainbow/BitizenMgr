package chingdim.bitizenmgr.main;

import chingdim.bitizenmgr.api.BitizenMgr;
import chingdim.bitizenmgr.api.Floor;

public class Main {

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        while (true) {
            int choiceActionType = Input.inputIntChoice(
                    "Please choose an action type",
                    new String[]{"Floor", "Bitizen"}
            );

            switch (choiceActionType) {
                case 1:
                    floor();
                    break;
                case 2:
                    bitizen();
                    break;
            }
        }
    }

    private static void floor() {
        int choiceFloor = Input.inputIntChoice(
                "Please choose an action",
                new String[]{
                        "Add"
                }
        );

        switch (choiceFloor) {
            case 1:
                addFloor();
                break;
        }
    }

    private static void bitizen() {
        int choice = Input.inputIntChoice(
                "Please choose an action",
                new String[]{"Add"}
        );

        switch (choice) {
            case 1:
                addBitizen();
                break;
        }
    }

    private static void addFloor() {
        boolean cont = true;
        while (cont) {
            String name = Input.inputStr("Please enter floor name");
            int floorType = Input.inputFloorType();

            if (BitizenMgr.addBuiltFloor(name, floorType)) {
                System.out.println("Successfully added floor.");
                cont = false;
            } else
                // TODO: Add support for building null floors, recommend staff if floor type is work
                System.out.println("Error occurred. Maybe the floor already exists?");
        }
    }

    private static void addBitizen() {
        boolean cont = true;
        while (cont) {
            String name = Input.inputBitizenName();
            int[] ratings = Input.inputRatings();
            String homeFloor = Input.inputResidentialFloorByNumber("Please enter home floor number").getName();
            Floor currentWorkFloorF = Input.inputBuiltWorkFloor("Please enter current work floor name");
            String currentWorkFloor = currentWorkFloorF == null ? null : currentWorkFloorF.getName();
            String dreamWorkFloor = Input.inputWorkFloor("Please enter dream work floor name").getName();

            if (BitizenMgr.addBitizen(name, ratings, homeFloor, currentWorkFloor, dreamWorkFloor)) {
                System.out.println("Successfully added bitizen.");
                cont = false;
            } else
                System.out.println("Error occurred. Maybe the bitizen already exists?");
        }
    }
}
