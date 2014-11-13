package entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Casa on 12/11/2014.
 */
public class MessageWriter {
    Socket informer;
    DataInputStream is;
    DataOutputStream os;
    private static HashMap<String, MessageWriter> writers;

    public static MessageWriter instance(Server s) throws IOException {
        if (writers.get(s.getServerName()) == null) writers.put(s.getServerName(), new MessageWriter(s));
        return writers.get(s.getServerName());
    }

    public MessageWriter(Server s) throws IOException {
        informer = new Socket(s.getIp(), 7895);
        is = new DataInputStream(informer.getInputStream());
        os = new DataOutputStream(informer.getOutputStream());
    }

    public void WriteMessage(String Message) throws IOException {
        os.writeUTF(Message);
    }
}
