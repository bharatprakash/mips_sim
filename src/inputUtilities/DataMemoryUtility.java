package inputUtilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

public class DataMemoryUtility
{
    public static final DataMemoryUtility instance = new DataMemoryUtility();

    private DataMemoryUtility()
    {

    }

    private Map<Integer, Integer> memoryDataMap = new TreeMap<Integer, Integer>();

    
    public void setValueToAddress(int address, int data)
    {
        memoryDataMap.put(address, data);
    }
    
    public int getValueFromAddress(int address) throws Exception
    {
        if (!memoryDataMap.containsKey(address))
            throw new Exception("Mem Add Not Found " + address);

        return memoryDataMap.get(address);
    }

    
    public Map<Integer, Integer> getMemoryBlockOfAddress(int address)
            throws Exception
    {
        return getMemoryBlockOfAddress(address, 4);
    }

    
    public Map<Integer, Integer> getMemoryBlockOfAddress(int address,
            int blocksize) throws Exception
    {
        Map<Integer, Integer> returnMap = new TreeMap<Integer, Integer>();

        if (!isPowerOf2(blocksize))
            throw new Exception("block size "
                    + blocksize);
        int temp = address - (address % blocksize);

        for (int i = temp; i < temp + blocksize; i++)
        {
            if (!memoryDataMap.containsKey(i))
                System.out.println("not found : " + i);
            else
                returnMap.put(i, memoryDataMap.get(i));

        }
        return returnMap;
    }

    
    /*public void dumpAllMemory()
    {
        String leftAlignFormat = "| %-5d | %-10d |%n";
        for (Integer key : memoryDataMap.keySet())
        {
            System.out.format(leftAlignFormat, key, memoryDataMap.get(key));
        }
    }*/
    
	
    public static void parseMemoryFile(String fileName) throws Exception
    {
        BufferedReader bfread = null;
        try
        {

            bfread = new BufferedReader(new FileReader(new File(fileName)));

            String line = null;
            int count = 0;
            int initialAddress = 0x100;

            while ((line = bfread.readLine()) != null)
            {
                line = line.trim();
                if (line.length() == 0)
                    break; // break on the first empty line
                int value = Integer.parseInt(line, 2);

                DataMemoryUtility.instance.setValueToAddress(initialAddress++,
                        value);

                count++;
            }
            //System.out.println("Total Number of memory locations = " + count);
        }
        finally
        {
            if (bfread != null)
                bfread.close();
        }

    }

    public static boolean isPowerOf2(int val)
    {
        return (val > 0) && (val & (val - 1)) == 0;
    }

    
    
}
