import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: Vlad
 * Date: 21.10.13
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
public class Server {
    private Socket socket;
    public DataInputStream streamIn;
    public DataOutputStream streamOut;

    public Server( ) throws IOException {
            socket = new Socket( "127.0.0.1", Code.PORT );
            socket.setTcpNoDelay( true );
            streamIn = new DataInputStream( new BufferedInputStream( socket.getInputStream() ) );
            streamOut = new DataOutputStream( socket.getOutputStream() );
    }

    public void sendData( String code, float x, float y, float currentAngle ) {
        StringBuilder sb = new StringBuilder(  );
        try {
            sb.append( code ).append( ";" ).
               append( x ).append( ";" ).
               append( y ).append( ";" ).
               append( currentAngle );
            streamOut.writeUTF( sb.toString() );
        } catch ( IOException ioEx ) {
            System.err.println( "Can't send new coordinates of ship" );
        }
    }

    public void sendShell( String code, String data ) {
        StringBuilder sb = new StringBuilder(  );
        try {
            sb.append( code ).append( ";" ).append( data );
            streamOut.writeUTF( sb.toString() );
        } catch ( IOException ioEx ) {
            System.err.println( "Can't send new coordinates of ship" );
        }
    }
}
