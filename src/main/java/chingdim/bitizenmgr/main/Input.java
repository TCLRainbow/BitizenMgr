package chingdim.bitizenmgr.main;

import chingdim.bitizenmgr.api.BitizenMgr;
import chingdim.bitizenmgr.api.Floor;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Input {
    private static final Scanner scanner = new Scanner(System.in);

    public static boolean inputBool(String prompt) {
        System.out.print(prompt + "? (Y/n) ");
        while (true) {
            try {
                char result = scanner.nextLine().charAt(0);
                if (result == 'Y' || result == 'y')
                    return true;
                if (result == 'N' || result == 'n')
                    return false;
                System.out.println("Invalid input!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Cannot input nothing in a y/n question!");
            }
        }
    }

    public static Integer inputIntAllowEmpty(String prompt) {
        System.out.print(prompt + ": ");
        while (true) {
            String result = scanner.nextLine();
            if (result.length() == 0)
                return null;
            try {
                return Integer.parseInt(result);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Input not integer!");
            }
        }
    }

    public static int inputInt(String prompt) {
        System.out.print(prompt + ": ");
        return ensureInt();
    }

    public static Integer inputIntRangeAllowEmpty(String prompt, int start, int end) {
        System.out.print(prompt + ": ");
        while (true) {
            String result = scanner.nextLine();
            if (result.length() == 0)
                return null;
            try {
                int i = Integer.parseInt(result);
                if (start <= i && i <= end)
                    return i;
                System.out.printf("ERROR: Input out of range (%d - %d)\n", start, end);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Input not integer!");
            }
        }
    }

    public static int inputIntRange(String prompt, int start, int end) {
        System.out.print(prompt + ": ");
        while (true) {
            int i = ensureInt();
            if (start <= i && i <= end)
                return i;
            System.out.printf("ERROR: Input out of range (%d - %d)\n", start, end);
        }
    }

    public static int[] inputRatings() {
        System.out.print("Please enter ratings for the bitizen: ");
        while (true) {
            String s = scanner.nextLine();
            if (s.matches("[0-9]{5}")) {
                int[] ratings = new int[5];
                for (int i = 0; i < ratings.length; i++)
                    ratings[i] = s.charAt(i) - 48;
                return ratings;
            }
            System.out.println("ERROR: Input is not 5 integers of range 1-9!");
        }
    }

    public static int inputIntChoice(String prompt, String[] choices) {
        System.out.println(prompt + ": ");
        for (int i = 0; i < choices.length; i++)
            System.out.printf("%d. %s\n", i+1, choices[i]);
        int result = ensureInt();
        while (result < 1 || result > choices.length) {
            System.out.printf("ERROR: Input out of range! (1 - %d)\n", choices.length);
            result = ensureInt();
        }
        return result;
    }

    public static int inputFloorType() {
        return inputIntChoice("Please enter floor type", BitizenMgr.getFloorTypes());
    }

    public static int inputWorkFloorType() {
        String[] workFloors = new String[BitizenMgr.getFloorTypes().length - 1];
        System.arraycopy(BitizenMgr.getFloorTypes(), 1, workFloors, 0, workFloors.length);
        return inputIntChoice("Please enter floor type", workFloors) + 1;
    }

    public static String inputStr(String prompt, boolean allowEmpty) {
        System.out.print(prompt + ": ");
        String result = scanner.nextLine();

        while (!allowEmpty && result.length() == 0) {
            System.out.println("ERROR: Input cannot be empty!");
            System.out.print(prompt + ": ");
            result = scanner.nextLine();
        }

        return result;
    }

    public static String inputStr(String prompt) {
        return inputStr(prompt, false);
    }

    public static Floor inputBuiltWorkFloor(String prompt) {
        System.out.print(prompt + ": ");
        while (true) {
            String floorName = scanner.nextLine().toUpperCase();
            if (floorName.isEmpty())
                return null;
            Floor f = BitizenMgr.getBuiltWorkFloor(floorName);
            if (f != null) {
                if (f.getName().equals(floorName))
                    return f;
                if (Input.inputBool("Do you mean: " + f.getName()))
                    return f;
            }
            System.out.println("ERROR: The floor doesn't exist or it isn't a built work floor!");
        }
    }

    public static Floor inputResidentialFloorByName(String prompt) {
        System.out.print(prompt + ": ");
        while (true) {
            String floorName = scanner.nextLine();
            Floor f = BitizenMgr.getResidentialFloorByName(floorName);
            if (f != null) {
                if (f.getName().equals(floorName))
                    return f;
                if (Input.inputBool("Do you mean: " + f.getName()))
                    return f;
            }
            System.out.println("ERROR: The floor doesn't exist or it isn't a residential floor!");
        }
    }

    public static Floor inputResidentialFloorByNumber(String prompt) {
        System.out.print(prompt + ": ");
        while (true) {
            int number = ensureInt();
            Floor f = BitizenMgr.getResidentialFloorByNumber(number);
            if (f != null)
                return f;
            System.out.println("ERROR: The floor doesn't exist or it isn't a residential floor!");
        }
    }

    public static Floor inputWorkFloor(String prompt) {
        System.out.print(prompt + ": ");
        while (true) {
            String floorName = scanner.nextLine().toUpperCase();
            Floor f = BitizenMgr.getWorkFloor(floorName);
            if (f != null) {
                if (f.getName().equals(floorName))
                    return f;
                if (Input.inputBool("Do you mean: " + f.getName()))
                    return f;
            }
            if (inputBool("The floor doesn't exist. Do you want to stub it")) {
                int floorType = inputWorkFloorType();
                if (BitizenMgr.addNotBuiltFloor(floorName, floorType))
                    return BitizenMgr.getWorkFloor(floorName);
                System.out.println("Error occurred when trying to add the floor, maybe floor name already exists?");
            } else
                System.out.println("ERROR: The floor doesn't exist!");
        }
    }

    public static String inputBitizenName() {
        System.out.print("Please enter bitizen name: ");
        while (true) {
            String name = scanner.nextLine().toUpperCase();
            if (name.matches("([A-Z].|[A-Z]+) [A-Z]+"))
                return name;
            System.out.println("ERROR: Invalid bitizen name!");
        }
    }

    private static int ensureInt() {
        boolean done = false;
        int result = 0;
        while (!done) {
            try {
                result = scanner.nextInt();
                scanner.nextLine();
                done = true;
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Input not integer!");
                scanner.nextLine();
            }
        }
        return result;
    }
}
