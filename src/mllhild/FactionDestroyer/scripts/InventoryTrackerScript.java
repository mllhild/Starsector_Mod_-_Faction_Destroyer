package mllhild.FactionDestroyer.scripts;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.Global;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;

import java.util.HashMap;
import java.util.Map;

public class InventoryTrackerScript implements EveryFrameScript {
    private Map<String, Float> previousCargo = new HashMap<>();

    @Override
    public void advance(float amount) {
//        // Only check in the campaign map
//        if (Global.getSector().isPaused()) return;
//
//        CargoAPI currentCargo = Global.getSector().getPlayerFleet().getCargo();
//        currentCargo.
//
//        // Check for new items or increased quantities
//        for (CargoStackAPI stack : currentCargo.getStacksCopy()) {
//            stack.getCargo().
//            String id = stack.getCargo()..getItemId(); // Or getWeaponId(), getShipHullSpec().getHullId(), etc.
//            float currentQty = stack.getSize();
//            float prevQty = previousCargo.containsKey(id) ? previousCargo.get(id) : 0;
//
//            if (currentQty > prevQty) {
//                float diff = currentQty - prevQty;
//                // LOGIC HERE: Player got 'diff' amount of 'id'
//                Global.getLogger(this.getClass()).info("Player gained " + diff + " of " + id);
//            }
//        }
//
//        // Update the reference map for the next frame
//        previousCargo.clear();
//        for (CargoStackAPI stack : currentCargo.getStacksCopy()) {
//            previousCargo.put(stack.getItemId(), stack.getSize());
//        }
    }

    @Override
    public boolean isDone() { return false; }
    @Override
    public boolean runWhilePaused() { return false; }
}
