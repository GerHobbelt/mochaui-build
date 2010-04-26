import com.polaropposite.mochaui.buid.BuildMochaUI;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args)
    {
        boolean doWatch = false;
        boolean force = false;
        
        String path = null;
        int size = args.length;
        if(size>0) {
            for(int i=0;i<size;i++) {
                if(args[i].equals("-w")) doWatch = true;
                if(args[i].equals("-f")) force = true;
                if(args[i].equals("-h") || args[i].equals("--help")) {
                    usage();
                    System.exit(1);
                }
                if(args[i].equals("-s")) {
                    if(i+1<size) {
                        path = args[i+1];
                        i++;
                    } else {
                        usage();
                        System.exit(1);                        
                    }
                }
            }
        }

        try {
            if(path==null || path.isEmpty())
                path = new File(".").getCanonicalPath();

            if(doWatch)
                BuildMochaUI.Watch(path,force);
            else
                BuildMochaUI.Rebuild(path,force);
        } catch(Exception e) {
             e.printStackTrace();
        }
    }

    private static void usage() {
        System.err.println(
                "\nUsage: java -jar mochaui-build-x.y.jar [options]\n\n"

                        + "Global Options\n"
                        + " -h, --help Displays this information\n"
                        + " -w watches path for changes and performs a partial update\n"
                        + " -f forces full overwrite (full build regardless of changes)\n"
                        + " -s <path> the root folder of the mochaui code base\n"

                        + "If no source folder file is specified, if not specified it uses current working directory folder'\n");
    }    
}
