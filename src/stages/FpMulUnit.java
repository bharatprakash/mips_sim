package stages;

import inputUtilities.ConfigUtility;
import stages.StageType;

public class FpMulUnit extends FPFunctionalUnit
{

    static volatile FpMulUnit instance = new FpMulUnit();;

    private FpMulUnit()
    {
        super();
        isPipelined = ConfigUtility.instance.FPMultPipelined;
        cyclesRequired = ConfigUtility.instance.FPMult;
        pipeSize = isPipelined ? ConfigUtility.instance.FPMult : 1;
        stage = StageType.EX;
        createPipelineQueue(pipeSize);
    }
}
