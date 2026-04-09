package mllhild.FactionDestroyer.hullmods;

import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import mllhild.FactionDestroyer.FactionValidator;

public class FactionLockHullmod extends BaseHullMod {

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        // We'll use this later for hullmod restrictions
        return true;
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        return null;
    }

    @Override
    public boolean canBeAddedOrRemovedNow(ShipAPI ship, MarketAPI marketOrNull, CampaignUIAPI.CoreUITradeMode mode) {
        return false;
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        //FactionValidator.validateShip(ship);

    }
}