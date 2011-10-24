package org.ned.server.nedadminconsole.client.interfaces;

public interface NedUsernameReceiver {
    void receiveUsername(String received);
    void receivePassword(String user, String passowrd);
}