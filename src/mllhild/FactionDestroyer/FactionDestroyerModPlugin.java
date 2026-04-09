package mllhild.FactionDestroyer;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

import mllhild.FactionDestroyer.FactionValidator;
import mllhild.FactionDestroyer.MyRefitListener;
import mllhild.FactionDestroyer.scripts.FactionDestroyer;
import mllhild.FactionDestroyer.scripts.InventoryTrackerScript;

public class FactionDestroyerModPlugin extends BaseModPlugin {


    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
    }

    @Override
    public void onNewGame() {
        super.onNewGame();
    }
    @Override
    public void onNewGameAfterProcGen(){}
    @Override
    public void onNewGameAfterTimePass(){}
    @Override
    public void onNewGameAfterEconomyLoad(){}

    @Override
    public void onGameLoad(boolean newGame) {
        FactionDestroyer fd = new FactionDestroyer();
        if(newGame){
            fd.LoadLunaLibSettings();
            fd.LoadLunaLibSettings();
        }
        fd.CullColonies();
    }


    public FactionValidator factionValidator = new FactionValidator();
    private void AddMyRefitListener(){ Global.getSector().getListenerManager().addListener(new MyRefitListener(), true); }
    private void AddInventoryTrackerScript(){ Global.getSector().getScripts().add(new InventoryTrackerScript()); }




}
