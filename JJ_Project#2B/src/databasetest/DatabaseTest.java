package databasetest;

import org.json.simple.*;

import java.sql.*;

public class DatabaseTest {

    public JSONArray getJSONData() {

        JSONArray data = new JSONArray();

        Connection conn = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;

        String query;

        boolean hasresults;
        int resultCount, columnCount = 0;

        try {

            /* Identify the Server */

            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "001208117";
            System.out.println("Connecting to " + server + "...");


            /* Load the MySQL JDBC Driver */

            Class.forName("com.mysql.jdbc.Driver").newInstance();

            /* Open Connection */

            conn = DriverManager.getConnection(server, username, password);

            /* Test Connection */

            if (conn.isValid(0)) {

                /* Connection Open! */

                System.out.println("Connected Successfully!");

                /* Prepare Select Query */

                query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query);

                /* Execute Select Query */

                System.out.println("Submitting Query ...");

                hasresults = pstSelect.execute();

                /* Get Results */

                System.out.println("Getting Results ...");

                while (hasresults || pstSelect.getUpdateCount() != -1) {

                    if (hasresults) {

                        /* Get ResultSet Metadata */

                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();

                        /* Get Data; Print as Table Rows */

                        while (resultset.next())
                        {
                            JSONObject thisPerson = new JSONObject();

                            for (int i = 2; i <= columnCount; i++)
                            {
                                thisPerson.put(metadata.getColumnLabel(i), resultset.getString(i));
                            }

                            data.add(thisPerson);
                        }

                    }

                    else
                        {

                        resultCount = pstSelect.getUpdateCount();

                        if (resultCount == -1)
                        {
                            break;
                        }

                    }

                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();

                }

            }

            System.out.println();

            /* Close Database Connection */

            conn.close();

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        /* Close Other Database Objects */ finally {

            if (resultset != null) {
                try {
                    resultset.close();
                    resultset = null;
                } catch (Exception e) {
                }
            }

            if (pstSelect != null) {
                try {
                    pstSelect.close();
                    pstSelect = null;
                } catch (Exception e) {
                }
            }

            if (pstUpdate != null) {
                try {
                    pstUpdate.close();
                    pstUpdate = null;
                } catch (Exception e) {
                }
            }

        }

        return data;
    }
}



