package stages;

import inputUtilities.ConfigUtility;
import stages.StageType;

public class FpDivUnit extends FPFunctionalUnit
{

    static volatile FpDivUnit instance = new FpDivUnit();

    private FpDivUnit()
    {
        super();
        isPipelined = ConfigUtility.instance.FPDividerPipelined;
        cyclesRequired = ConfigUtility.instance.FPDivide;
        pipeSize = isPipelined ? ConfigUtility.instance.FPDivide : 1;
        stage = StageType.EX;
        createPipelineQueue(pipeSize);
    }
}
