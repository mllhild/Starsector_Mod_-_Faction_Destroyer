package mllhild.FactionDestroyer.scripts;

public class DestructionOptions{
    public String factionId;
    public String deathOption; // destroy, decimate, become pirate, become independent
    public int maxColonySizeCutOff;
    public int minColonySizeCutOff;
    public int maxColonyCount;
    public boolean destroyFleets;

    public DestructionOptions(String factionId, String deathOption, int maxColonySizeCutOff, int minColonySizeCutOff, int maxColonyCount, boolean destroyFleets){
        this.factionId = factionId;
        this.deathOption = deathOption;
        this.maxColonySizeCutOff = maxColonySizeCutOff;
        this.minColonySizeCutOff = minColonySizeCutOff;
        this.maxColonyCount = maxColonyCount;
        this.destroyFleets = destroyFleets;
    }
    public DestructionOptions(String factionId, String deathOption){
        this.factionId = factionId;
        this.deathOption = deathOption;
        this.maxColonySizeCutOff = 10;
        this.minColonySizeCutOff = 0;
        this.maxColonyCount = 10;
        this.destroyFleets = true;
    }
}
