package stages;

import inputUtilities.ConfigUtility;
import stages.StageType;

public class FpAddUnit extends FPFunctionalUnit
{

    static volatile FpAddUnit instance = new FpAddUnit();

    private FpAddUnit()
    {
        super();
        isPipelined = ConfigUtility.instance.FPAdderPipelined;
        cyclesRequired = ConfigUtility.instance.FPAdder;
        pipeSize = isPipelined ? ConfigUtility.instance.FPAdder : 1;
        stage = StageType.EX;
        createPipelineQueue(pipeSize);
    }
}
