package icu.ripley.system.shared;

import icu.ripley.system.shared.EventTypes.ServerEventType;

public class EventMessageUtils {

    public static String to(ServerEventType eventType, SpigotServer serverInfo){
        String result = "";

        switch(eventType){
            case OH_GOD_I_AM_IN_SO_MUCH_PAIN_PLEASE_REMOVE_AND_ALERT_ADMINS_OH_GOD_PLEASE_IT_HURTS_SO_BAD:
            case DESTROY:
                result = eventType + " " + serverInfo.getServerName();
                break;
            case CREATE:
                result = "{} {} {} {} {}".format(
                        String.valueOf(eventType),
                        serverInfo.getServerIP(),
                        serverInfo.getServerPort(),
                        serverInfo.getServerName(),
                        serverInfo.isServerPrivate()
                );

                break;
        }

        return result;
    }

    public static SpigotServer from(String[] args){
        // breaking down each arg because fuck my life this was awful to make.
        // 1 -> The server's IP address
        // 2 -> The server's port
        // 3 -> The server's name
        // 4 -> If the server is private
        // 5 -> The server's node
        return new SpigotServer(args[3], args[5], args[1], Integer.parseInt(args[2]), args[4].equals("true"));
    }

}
