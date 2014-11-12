package rman;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by JoséPablo on 01/11/2014.
 */
public class RmanScripts {
    public void run() {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("cmd /c rman "
                    + "TARGET sys/gerardo4@xe CATALOG rman/gerardo4@xe "
                    + " script full_backup using 'full_backup' 'bkp'");
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
            while ((line = bre.readLine()) != null) {
                System.out.println(line);
            }
            bre.close();
            p.waitFor();
            System.out.println("Done.");
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
