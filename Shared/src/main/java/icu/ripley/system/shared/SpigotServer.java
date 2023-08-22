package icu.ripley.system.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.InetSocketAddress;

@Getter
@AllArgsConstructor
public class SpigotServer {
    private String serverName;
    private String serverNode;
    private String serverIP;
    private int serverPort;
    private boolean serverPrivate;


    public InetSocketAddress getAddressAsSocketAddress(){
        return new InetSocketAddress(serverIP, serverPort);
    }
}
