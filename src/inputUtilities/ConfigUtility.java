package inputUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ConfigUtility
{

    public static final ConfigUtility instance = new ConfigUtility();

    private ConfigUtility()
    {

    }

    public int     FPAdder     = 0;
    public int     FPMult      = 0;
    public int     FPDivide    = 0;

    public boolean FPAdderPipelined   = false;
    public boolean FPMultPipelined    = false;
    public boolean FPDividerPipelined = false;

    public int     Memory      = 0;
    public int     ICache      = 0;
    public int     DCache      = 0;

    /*public void dumpConfiguration()
    {
        String leftAlignFormat = "| %-20s | %-10s |%n";

        System.out.format(leftAlignFormat, "FPAdderLatency", FPAdder);
        System.out
                .format(leftAlignFormat, "FPAdderPipelined", FPAdderPipelined);

        System.out.format(leftAlignFormat, "FPMultLatency", FPMult);
        System.out.format(leftAlignFormat, "FPMultPipelined", FPMultPipelined);

        System.out.format(leftAlignFormat, "FPDividerPipelined",
                FPDivide);
        System.out.format(leftAlignFormat, "FPDividerPipelined",
                FPDividerPipelined);

        System.out.format(leftAlignFormat, "MemoryLatency", Memory);
        System.out.format(leftAlignFormat, "ICacheLatency", ICache);
        System.out.format(leftAlignFormat, "DCacheLatency", DCache);
    }
    */
    
    public static void parseConfigFile(String fileName) throws Exception
    {
        BufferedReader buff = null;
        try
        {

            buff = new BufferedReader(new FileReader(new File(fileName)));

            String line = null;
            int count = 0;

            while ((line = buff.readLine()) != null)
            {
                line = line.trim();
                if (line.length() == 0)
                    continue;
                count++;

                try
                {
                    parseConfigLine(line);
                }
                catch (Exception e)
                {
                    throw new Exception("Error : " + line);
                }
            }
            
        }
        finally
        {
            if (buff != null)
                buff.close();
        }

    }

    
    
    private static void parseConfigLine(String line)
    {
        String s[], s1[];
        line = line.trim();
        line = line.toLowerCase();

        s = line.split(":");

        String configItem = s[0].trim();

        switch (configItem)
        {
            case "fp adder":
                s1 = s[1].split(",");
                ConfigUtility.instance.FPAdder = Integer.parseInt(s1[0]
                        .trim());
                ConfigUtility.instance.FPAdderPipelined = s1[1].trim()
                        .toLowerCase().equals("yes");
                break;

            case "fp multiplier":
                s1 = s[1].split(",");
                ConfigUtility.instance.FPMult = Integer.parseInt(s1[0]
                        .trim());
                ConfigUtility.instance.FPMultPipelined = s1[1].trim()
                        .toLowerCase().equals("yes");
                break;

            case "fp divider":
                s1 = s[1].split(",");
                ConfigUtility.instance.FPDivide = Integer.parseInt(s1[0]
                        .trim());
                ConfigUtility.instance.FPDividerPipelined = s1[1].trim()
                        .toLowerCase().equals("yes");
                break;

            case "main memory":
                ConfigUtility.instance.Memory = Integer.parseInt(s[1]
                        .trim());
                break;

            case "i-cache":
                ConfigUtility.instance.ICache = Integer.parseInt(s[1]
                        .trim());
                break;

            case "d-cache":
                ConfigUtility.instance.DCache = Integer.parseInt(s[1]
                        .trim());
                break;
        }

    }
    
    
    
}
