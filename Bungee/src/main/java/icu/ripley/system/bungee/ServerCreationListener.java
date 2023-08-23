package icu.ripley.system.bungee;

import icu.ripley.system.shared.EventMessageUtils;
import icu.ripley.system.shared.EventTypes.ServerEventType;
import icu.ripley.system.shared.SpigotServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import redis.clients.jedis.JedisPubSub;

import java.net.InetSocketAddress;
import java.util.Arrays;

public class ServerCreationListener extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        System.out.println("Messsage recieved! > " + message + " on channel " + channel);
        if(!channel.equals("ServerEvents")) return;
        System.out.println("Passed ServerEvents channel check, attempting to get arguments and event type.");

        String[] args = message.split(" ");
        System.out.println("args > " + Arrays.toString(args));

        ServerEventType eventType = ServerEventType.valueOf(args[0]);
        System.out.println("event type > " + eventType);
//        String ip = args[1];
//        int port = Integer.parseInt(args[2]);
//        String serverName = args[3];

        switch (eventType){
            case CREATE:
                SpigotServer spigotServer = EventMessageUtils.from(args);
                System.out.println("attempting server creation of " + spigotServer.getServerName() + ".");

                ServerInfo info = ProxyServer.getInstance().constructServerInfo(
                        spigotServer.getServerName(),
                        spigotServer.getAddressAsSocketAddress(),
                        "[automanaged] [started] " + spigotServer.getServerName(),
                        spigotServer.isServerPrivate());
                ProxyServer.getInstance().getServersCopy().put(spigotServer.getServerName(), info);
                break;
            case DESTROY:
            case OH_GOD_I_AM_IN_SO_MUCH_PAIN_PLEASE_REMOVE_AND_ALERT_ADMINS_OH_GOD_PLEASE_IT_HURTS_SO_BAD: // TODO: discord webhooks
                System.out.println("destroying server " + args[1] + '.');
                String serverToRemove = args[1];
                ProxyServer.getInstance().getServersCopy().remove(serverToRemove);
                break;
        }

    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }

}
