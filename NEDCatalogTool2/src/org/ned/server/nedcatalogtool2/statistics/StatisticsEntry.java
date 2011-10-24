package org.ned.server.nedcatalogtool2.statistics;

public class StatisticsEntry {

    public String mEventType;
    public String mDetails;
    public String mTime;

    public StatisticsEntry( String mEventType, String mDetails, String mTime) {
        this.mEventType = mEventType;
        this.mDetails = mDetails;
        this.mTime = mTime;
    }
}

