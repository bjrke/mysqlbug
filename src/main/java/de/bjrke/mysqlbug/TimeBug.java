package de.bjrke.mysqlbug;

import com.mysql.cj.jdbc.util.BaseBugReport;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeBug extends BaseBugReport {

    @Override
    public void setUp() throws SQLException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        try (final var stm =
                prepare("CREATE TABLE IF NOT EXISTS timebug (time TIME PRIMARY KEY)")) {
            stm.execute();
        }
        try (final var stm = prepare("REPLACE INTO timebug (time) VALUES (?)")) {
            stm.setTime(1, new Time(TimeUnit.DAYS.toMillis(1) - 250));
            stm.execute();
        }
    }

    private PreparedStatement prepare(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    @Override
    public void tearDown() throws SQLException {
        try (final var stm = prepare("DROP TABLE timebug")) {
            stm.execute();
        }
    }

    @Override
    public void runTest() throws SQLException {
        try (final var stm = prepare("SELECT time FROM timebug");
                final var rs = stm.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getTime("time"));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new TimeBug().run();
    }
}
