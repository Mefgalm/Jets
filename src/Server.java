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
    private DataInputStream streamIn;
    private DataOutputStream streamOut;

    public Server() {
        try {
            socket = new Socket( "127.0.0.1", Code.PORT );
            socket.setTcpNoDelay( true );

            streamIn = new DataInputStream( new BufferedInputStream( socket.getInputStream() ) );
            streamOut = new DataOutputStream( socket.getOutputStream() );
        } catch ( UnknownHostException uhe ) {
            System.err.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe ) {
            System.err.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void sendData( String code, float x, float y, float currentAngle ) {
        StringBuilder sb = new StringBuilder(  );
        try {
            sb.append( code ).append( ";" ).
               append( x ).append( ";" ).
               append( y ).append( ";" ).
               append( currentAngle ).append( ";" ).append( System.currentTimeMillis() );
            streamOut.writeUTF( sb.toString() );
            streamOut.flush();
        } catch ( IOException ioEx ) {
            System.err.println( "Can't send new coordinates of ship" );
        }
    }

    public void sendShell( String code, String data ) {
        StringBuilder sb = new StringBuilder(  );
        try {
            sb.append( code ).append( ";" ).append( data );
            streamOut.writeUTF( sb.toString() );
            streamOut.flush();
        } catch ( IOException ioEx ) {
            System.err.println( "Can't send new coordinates of ship" );
        }
    }

    public void send( String code ) {
        StringBuilder sb = new StringBuilder(  );
        try {
            sb.append( code );
            streamOut.writeUTF( sb.toString() );
            streamOut.flush();
        } catch ( IOException ioEx ) {
            System.err.println( "Can't send new coordinates of ship" );
        }
    }

    public void sendNewUser( String code, String name, int HP, float x, float y, float angle ) {
        StringBuilder sb = new StringBuilder(  );
        try {
            sb.append( code ).append( ";" ).append( name ).append( ";" ).
            append( String.valueOf( HP ) ).append( ";" ).append( String.valueOf( x ) ).
            append( ";" ).append( String.valueOf( y ) ).append( ";" ).append( String.valueOf( angle ) );
            streamOut.writeUTF( sb.toString() );
            streamOut.flush();
        } catch ( IOException ioEx ) {
            System.err.println( "Can't send new coordinates of ship" );
        }
    }


    public String relieveData() throws IOException {
        try {
            return streamIn.readUTF();
        } catch ( IOException ioEx ) {
            throw new IOException();
        }
    }
}
