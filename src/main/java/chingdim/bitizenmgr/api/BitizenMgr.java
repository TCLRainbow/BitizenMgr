package chingdim.bitizenmgr.api;

import org.sqlite.SQLiteConfig;

import java.sql.*;

public class BitizenMgr {
    static Connection conn;
    private static final String[] floorTypes;

    static {
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection("jdbc:sqlite:bitizenmgr.sqlite", config.toProperties());
            floorTypes = new String[SQL.execInteger("SELECT COUNT(*) FROM FloorType")];
            try (ResultSet rs = SQL.exec("SELECT * FROM FloorType")) {
                int i = 0;
                while (rs.next()) {
                    floorTypes[i] = rs.getString(1);
                    i++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addBuiltFloor(String name, int floorType) {
        name = name.toUpperCase();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Floor VALUES (?, " +
                            "(SELECT COUNT(*) FROM Floor WHERE Number IS NOT NULL) + 2, " +
                            "?)"
            );
            stmt.setString(1, name);
            stmt.setInt(2, floorType);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean addNotBuiltFloor(String name, int floorType) {
        name = name.toUpperCase();
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Floor(Name, FloorType) VALUES (?, ?)");
            stmt.setString(1, name);
            stmt.setInt(2, floorType);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean addBitizen(String name, int[] ratings, String home, String currentWork, String dreamWork) {
        name = name.toUpperCase();
        home = home.toUpperCase();
        if (currentWork != null)
            currentWork = currentWork.toUpperCase();
        dreamWork = dreamWork.toUpperCase();
        try {
            PreparedStatement stmt = conn.prepareStatement("""
                    INSERT INTO Bitizen SELECT ?, ?,?,?,?,?, ? as h, ? as c, ? as d
                    WHERE (SELECT COUNT(HomeFloor) FROM Bitizen WHERE HomeFloor = h) < 5
                    AND (SELECT COUNT(CurrentWork) FROM Bitizen WHERE CurrentWork = c) < 3
                    AND EXISTS(SELECT 1 FROM Floor WHERE Name = h AND Number IS NOT NULL AND FloorType = 1)
                    AND EXISTS(SELECT 1 FROM Floor WHERE Name = c AND Number is NOT NULL AND FloorType > 1)
                    AND EXISTS(SELECT 1 FROM Floor WHERE Name = d AND FloorType > 1)
                    """);
            stmt.setString(1, name);
            for (int i = 0; i < ratings.length; i++)
                stmt.setInt(i+2, ratings[i]);
            stmt.setString(7, home);
            stmt.setObject(8, currentWork, Types.VARCHAR);
            stmt.setString(9, dreamWork);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Floor getResidentialFloorByName(String search) {
        search = search.toUpperCase();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Floor WHERE Name LIKE ? AND FloorType = 1 LIMIT 1"
            );
            stmt.setString(1, search + '%');
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return new Floor(rs.getString(1), rs.getInt(2), rs.getInt(3));
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public static Floor getResidentialFloorByNumber(int number) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Floor WHERE Number = ? AND FloorType = 1 LIMIT 1"
            );
            stmt.setInt(1, number);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return new Floor(rs.getString(1), rs.getInt(2), rs.getInt(3));
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public static Floor getWorkFloor(String search){
        search = search.toUpperCase();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Floor WHERE Name LIKE ? AND FloorType > 1 LIMIT 1"
            );
            stmt.setString(1, search + '%');
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return new Floor(rs.getString(1), rs.getInt(2), rs.getInt(3));
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public static Floor getBuiltWorkFloor(String search) {
        search = search.toUpperCase();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Floor WHERE Name LIKE ? AND Number IS NOT NULL AND FloorType > 1 LIMIT 1"
            );
            stmt.setString(1, search + '%');
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return new Floor(rs.getString(1), rs.getInt(2), rs.getInt(3));
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public static String[] getFloorTypes() {
        return floorTypes;
    }

    private BitizenMgr() {}
}
