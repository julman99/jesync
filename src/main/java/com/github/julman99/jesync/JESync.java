package com.github.julman99.jesync;

import java.util.Arrays;
import java.util.Collection;
import com.github.julman99.jesync.net.Server;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 *
 * @author julio
 */
public class JESync {

    
    static Collection<String> a(String... strs) {
        return Arrays.asList(strs);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        OptionParser parser=new OptionParser();
        parser.acceptsAll(a("p","port"),"Port to listen on").withRequiredArg().ofType(Integer.class).describedAs("Port").defaultsTo(11400);
        parser.acceptsAll(a("h","?","help"),"Show help");
        parser.acceptsAll(a("v","version"),"Prints version number");
                 
        OptionSet options=parser.parse(args);
        
        if(options.has("h")){
            parser.printHelpOn(System.out);
        }else if(options.has("v")){
            System.out.println("JESync 0.8 RC");
            System.out.println("https://github.com/julman99/JESync");
            System.out.println("Copyright (c) 2012 Julio Viera");
        }else{
            int port=Integer.parseInt(options.valueOf("p").toString());
            System.out.println("JESync Server, port: "+port);
            new Server(port).run();
        }
    }
}
