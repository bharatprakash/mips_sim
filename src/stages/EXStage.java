package stages;

import instructions.FunctionalUnitType;
import instructions.Instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.collections4.ListUtils;

public class EXStage extends Stage
{

    public static volatile EXStage excuteObj =  new EXStage();


    private IntegerUnit iu;
    private MemoryUnit  mem;
    private FpAddUnit   fpadd;
    private FpMulUnit   fpmul;
    private FpDivUnit   fpdiv;
    private List<FunctionalUnit>        tieBreakerList;

    private EXStage()
    {
        super();

        this.stageType = StageType.EX;

        iu = IntegerUnit.instance;
        mem = MemoryUnit.instance;
        fpadd = FpAddUnit.instance;
        fpmul = FpMulUnit.instance;
        fpdiv = FpDivUnit.instance;

        tieBreakerList = new ArrayList<FunctionalUnit>();
        tieBreakerList.add(mem);
        tieBreakerList.add(fpadd);
        tieBreakerList.add(fpmul);
        tieBreakerList.add(fpdiv);
    }

    @Override
    public void execute() throws Exception
    {

        List<FunctionalUnit> readyList = new ArrayList<FunctionalUnit>();

        for (FunctionalUnit fu : tieBreakerList)
        {
            if (fu.stageCompleted())
                readyList.add(fu);
        }

        if (readyList.size() <= 1)
        {
            for (FunctionalUnit fu : tieBreakerList)
                fu.execute();
        }
        else
        {
            List<FunctionalUnit> winnerList = new ArrayList<FunctionalUnit>();
            winnerList.add(tieBreaker(readyList));

            if (winnerList.size() == 0)
                throw new Exception(
                        "ExStage: units said ready to send but winnerslist is empty");

            List<FunctionalUnit> losersList = ListUtils.subtract(readyList,
                    winnerList);

            List<FunctionalUnit> exeList = ListUtils.subtract(tieBreakerList,
                    losersList);

            // for all losers, run mark StructHazard
            for (FunctionalUnit fu : losersList)
            {
                fu.iQueue.peekFirst().STRUCT = true;
                // TODO for pipelined FPFunctionalUnit, move things 1 right

                if (fu instanceof FPFunctionalUnit)
                    ((FPFunctionalUnit) fu).rotatePipelineOnHazard();

            }
            // for exeList, execute
            for (FunctionalUnit fu : exeList)
                fu.execute();
        }

        iu.execute(); // Special Handling for this

    }

    // This method will be called by ID while executing and passing on the
    // instruction
    @Override
    public boolean acceptInstruction(Instruction instruction) throws Exception
    {
        // TODO Implement this method
        FunctionalUnit functionalUnit = getFunctionalUnit(instruction);
        if (!functionalUnit.isQFree(instruction))
            throw new Exception("EXSTAGE: Illegal state exception "
                    + instruction.toString());

        functionalUnit.acceptInstruction(instruction);

        return true;
    }

    // This method will be called by ID while executing and passing on the
    // instruction, and check for STRUCT hazard
    @Override
    public boolean isStageFree(Instruction instruction) throws Exception
    {
        FunctionalUnit functionalUnit = getFunctionalUnit(instruction);
        return functionalUnit.isQFree(instruction);
    }

    /**
     * 
     * @param instruction
     *            to find which FU to use
     * @return
     * @throws Exception
     *             defensive
     */
    @SuppressWarnings("incomplete-switch")
    private FunctionalUnit getFunctionalUnit(Instruction instruction)
            throws Exception
    {

        if (instruction.fu == FunctionalUnitType.UNKNOWN
                || instruction.fu == null)
            throw new Exception("EXSTAGE: Incorrect type"
                    + instruction.toString());

        switch (instruction.fu)
        {

            case FPADD:
                return fpadd;

            case FPDIV:
                return fpdiv;

            case FPMUL:
                return fpmul;

            case IU:
                return iu;
        }

        return null;
    }

    private FunctionalUnit tieBreaker(List<FunctionalUnit> tieList)
    {
        TreeMap<Integer, FunctionalUnit> fUMap = new TreeMap<Integer, FunctionalUnit>();

        for (FunctionalUnit fu : tieList)
        {

            if (fu instanceof MemoryUnit)
            {
                mergeFUMap(fu.cyclesRequired + 1, fu, fUMap);
                continue;
            }

            if (fu.isPipelined)
            {
                mergeFUMap(fu.cyclesRequired, fu, fUMap);
            }
            else
            {
                mergeFUMap(1000 + fu.cyclesRequired, fu, fUMap);
            }
        }

        return fUMap.get(fUMap.lastKey());
    }

    private void mergeFUMap(int calculatedKey, FunctionalUnit fu,
            TreeMap<Integer, FunctionalUnit> map)
    {

        if (map.containsKey(calculatedKey))
        {

            FunctionalUnit mapEntry = (FunctionalUnit) map.get(calculatedKey);
            int fuEntry = fu.iQueue.peekFirst().enterTime[StageType.IF.getId()];
            int localEntry = mapEntry.iQueue.peekFirst().enterTime[StageType.IF
                    .getId()];
            if (fuEntry < localEntry)
                map.put(calculatedKey, fu);

        }
        else
        {
            map.put(calculatedKey, fu);
        }
    }

}
