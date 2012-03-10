package jesync;

import jesync.net.Server;

/**
 *
 * @author julio
 */
public class JESync {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port=11400;
        
        if(args.length>0)
            port=Integer.parseInt(args[0]);
        
        new Server(port).run();
    }
}
