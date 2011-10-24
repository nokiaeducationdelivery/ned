package org.ned.server.nedcatalogtool2.statistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatisticsReader {

    public static LinkedList<StatisticsEntry> parseStatistics( InputStream input) {
        LinkedList<StatisticsEntry> retval = new LinkedList<StatisticsEntry>();
        try {
            BufferedReader reader  = new BufferedReader( new InputStreamReader( input ) );
            String entryString = null;
            while ( (entryString = reader.readLine()) != null ) {
                String[] details = entryString.split( "," );
                String eventType = details[0];
                String detail =  details[1];
                String time = details[2];
                retval.add( new StatisticsEntry( eventType, detail, time ) );
            }
        } catch (IOException ex) {
            Logger.getLogger(StatisticsReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(StatisticsReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retval;
    }
}
