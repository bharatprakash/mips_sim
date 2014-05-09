package stages;

public enum StageType
{

    IF(0), ID(1), EX(2), WB(3);

    private int id;

    private StageType(int val)
    {
        this.id = val;
    }

    public int getId()
    {
        return id;
    }

}
