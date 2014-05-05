package stages;

public class CPU
{
    public static int CLOCK           = 0;

    public static int PROGRAM_COUNTER = 0;

    public static RUN RUN_TYPE        = RUN.MEMORY; //  RUN.PIPELINE; // DEFAULT

    public enum RUN
    {
        PIPELINE, MEMORY
    };

}
