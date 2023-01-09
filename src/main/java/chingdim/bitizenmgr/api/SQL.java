package chingdim.bitizenmgr.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static chingdim.bitizenmgr.api.BitizenMgr.conn;

class SQL {
    static ResultSet exec(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    static int execInteger(String query) throws SQLException {
        try (ResultSet rs = exec(query)) {
            return rs.getInt(1);
        }
    }

    private SQL() {}
}
