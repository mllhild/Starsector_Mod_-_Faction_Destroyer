package mllhild.FactionDestroyer.scripts;

import com.fs.starfarer.api.Global;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.util.Misc;
import lunalib.lunaSettings.LunaSettings;
import mllhild.FactionDestroyer.FactionValidator;

import java.util.*;

public class FactionDestroyer {
    // get results from Lunalib settings
    public void LoadLunaLibSettings(){
        FactionAction("pirates",LunaSettings.getString("mllhild_factiondestroyer", "pirates").trim());
        FactionAction("independent",LunaSettings.getString("mllhild_factiondestroyer", "independent").trim());
        FactionAction("hegemony",LunaSettings.getString("mllhild_factiondestroyer", "hegemony").trim());
        FactionAction("tritachyon",LunaSettings.getString("mllhild_factiondestroyer", "tritachyon").trim());
        FactionAction("sindrian_diktat",LunaSettings.getString("mllhild_factiondestroyer", "sindrian_diktat").trim());
        FactionAction("lions_guard",LunaSettings.getString("mllhild_factiondestroyer", "lions_guard").trim());
        FactionAction("knights_of_ludd",LunaSettings.getString("mllhild_factiondestroyer", "knights_of_ludd").trim());
        FactionAction("luddic_church",LunaSettings.getString("mllhild_factiondestroyer", "luddic_church").trim());
        FactionAction("luddic_path",LunaSettings.getString("mllhild_factiondestroyer", "luddic_path").trim());

        FactionAction("persean_league",LunaSettings.getString("mllhild_factiondestroyer", "persean_league").trim());
        FactionAction("persean",LunaSettings.getString("mllhild_factiondestroyer", "persean_league").trim());

        ApplySizeLimit(
                LunaSettings.getInt("mllhild_factiondestroyer", "colonySizeMin"),
                LunaSettings.getInt("mllhild_factiondestroyer", "colonySizeMax")
        );

        for( var faction : Global.getSector().getAllFactions())
        {
            if(faction.getId().equals("independent")) continue;
            if(faction.getId().equals("pirates")) continue;
            ApplyMaxColonyCountPerFaction(
                    faction.getId(), LunaSettings.getInt("mllhild_factiondestroyer", "colonyCountMax")
            );
        }

    }

    public void CullColonies(){
        int maxColoniesCount = LunaSettings.getInt("mllhild_factiondestroyer", "colonyCountAllMax");
        if(maxColoniesCount<1)
            return;
        var sector = Global.getSector();
        List<MarketAPI> markets = new ArrayList<>();
        for (MarketAPI market : sector.getEconomy().getMarketsCopy()) {
            markets.add(market);
        }
        // Sort so we remove the least valuable first (smallest size first)
        markets.sort(Comparator.comparingInt(MarketAPI::getSize));

        int toRemove = markets.size() - maxColoniesCount;
        if(toRemove <= 0)
            return;

        for (int i = 0; i < toRemove; i++)
            RemoveMarketV2(markets.get(i));
    }

//    public boolean IsImportantNpc(String npcID){
//        var importantPeople = Global.getSector().getImportantPeople();
//        var list = importantPeople.getPeopleCopy();
//
//        return false;
//    }

    public void ApplySizeLimit(int sizeMin, int sizeMax){
        var sector = Global.getSector();
        // --- Damage markets ---
        for (var market : sector.getEconomy().getMarketsCopy()) {
            if (market.getSize() > sizeMax) {
                market.setSize(sizeMax);
            }
            if (market.getSize() < sizeMin) {
                // Remove market from economy
                sector.getEconomy().removeMarket(market);
            }
        }
    }

    public void ApplyMaxColonyCountPerFaction(String factionID, int count) {
        var sector = Global.getSector();

        // Collect faction markets (colonies only; skip hidden/temporary if desired)
        List<MarketAPI> factionMarkets = new ArrayList<>();
        for (MarketAPI market : sector.getEconomy().getMarketsCopy()) {
            if (factionID.equals(market.getFactionId())) {
                factionMarkets.add(market);
            }
        }

        if (factionMarkets.size() <= count) return;

        // Sort so we remove the least valuable first (smallest size first)
        factionMarkets.sort(Comparator.comparingInt(MarketAPI::getSize));

        int toRemove = factionMarkets.size() - count;

        for (int i = 0; i < toRemove; i++)
            RemoveMarketV2(factionMarkets.get(i));
    }



    public void FactionAction(String factionID, String actionID){
        switch (actionID){
            case "destroy":{
                DestroyFaction(factionID);
                break;
            }
            case "decimate":{
                DecimateFaction(factionID);
                break;
            }
            case "become Pirate":{
                TurnToFaction(factionID, "pirates");
                break;
            }
            case "become Independent":{
                TurnToFaction(factionID, "independent");
                break;
            }
            default:{break;}
        }
    }

    public void DestroyFaction(String factionID) {
        var sector = Global.getSector();

        // --- Remove fleets ---
        for (var loc : sector.getAllLocations()) {
            var fleets = new ArrayList<>(loc.getFleets());
            for (var fleet : fleets) {
                if (fleet.getFaction().getId().equals(factionID)) {
                    fleet.despawn();
                }
            }
        }

        // --- Remove markets (colonies & stations) ---
        var markets = new ArrayList<>(sector.getEconomy().getMarketsCopy());
        for (var market : markets) {
            //Global.getLogger(FactionValidator.class).info("market: " + market.getFaction().getId() + " " + market.getPrimaryEntity().getName());
            if (market.getFactionId().equals(factionID))
                RemoveMarketV2(market);
        }
        TurnToFaction(factionID,"pirates");
    }
    public void DecimateFaction(String factionID) {
        var sector = Global.getSector();

        // --- Cripple fleets ---
        for (var loc : sector.getAllLocations()) {
            for (var fleet : loc.getFleets()) {
                if (fleet.getFaction().getId().equals(factionID)) {

                    // Reduce combat readiness of all ships
                    fleet.getFleetData().getMembersListCopy().forEach(member -> {
                        member.getRepairTracker().setCR(0.1f);
                        member.getStatus().applyDamage(0.7f); // heavy damage
                    });

                    // Optional: reduce fleet strength
                    if (fleet.getFleetPoints() > 50) {
                        fleet.inflateIfNeeded();
                        fleet.getFleetData().removeFleetMember(
                                fleet.getFleetData().getMembersListCopy().get(0)
                        );
                    }
                }
            }
        }

        // --- Damage markets ---
        for (var market : sector.getEconomy().getMarketsCopy()) {
            if (market.getFactionId().equals(factionID)) {

                // Stability hit
                market.getStability().modifyFlat("decimated", -5);

                // Disrupt all industries
                market.getIndustries().forEach(ind -> {
                    ind.setDisrupted(120f); // ~120 days
                });

                // Optional: reduce size
                if (market.getSize() > 3) {
                    market.setSize(market.getSize() - 1);
                }
            }
        }
    }
    public void TurnToFaction(String factionIdOld, String factionIdNew) {
        var sector = Global.getSector();

        // --- Transfer fleets ---
        for (var loc : sector.getAllLocations()) {
            for (var fleet : loc.getFleets()) {
                if (fleet.getFaction().getId().equals(factionIdOld)) {
                    fleet.setFaction(factionIdNew, true);
                }
            }
        }

        // --- Transfer markets ---
        for (var market : sector.getEconomy().getMarketsCopy()) {
            if (market.getFactionId().equals(factionIdOld)) {

                // Change market faction
                market.setFactionId(factionIdNew);

                // Change entity faction (planet/station)
                if (market.getPrimaryEntity() != null) {
                    market.getPrimaryEntity().setFaction(factionIdNew);
                }
            }
        }

        // --- Entities ---
        for (var loc : sector.getAllLocations()) {
            for (var ent : loc.getAllEntities()) {
                if (ent.getFaction().getId().equals(factionIdOld)) {
                    ent.setFaction(factionIdNew);
                }
            }
        }


        // --- Optional: adjust relationships ---
        var oldFaction = sector.getFaction(factionIdOld);
        var newFaction = sector.getFaction(factionIdNew);

        if (oldFaction != null && newFaction != null) {
            for (var faction : sector.getAllFactions()) {
                float rel = oldFaction.getRelationship(faction.getId());
                newFaction.setRelationship(faction.getId(), rel);
            }
        }
    }

    public void RemoveMarketV2(MarketAPI market){
        String tags = "";
        for(var tag : market.getTags())
            tags = tags + " " + tag;
        //Global.getLogger(FactionValidator.class).info("market.getPrimaryEntity().getName() market tags: " + tags);

        for(var seToken : market.getConnectedEntities()){
            market.getConnectedEntities().remove(seToken);
            //Global.getLogger(FactionValidator.class).info("market.getPrimaryEntity().getName(): " + market.getPrimaryEntity().getName());
            tags = "";
            for(var tag : seToken.getTags())
                tags = tags + " " + tag;
            //Global.getLogger(FactionValidator.class).info("market.getPrimaryEntity().getName() entity tags: " + tags);
            seToken.clearTags();
            seToken.setMarket(null);
            seToken.clearTags();
            seToken.setExpired(true);
            Misc.fadeAndExpire(seToken, 0.1f);
            break;
        }
        for(var person : market.getPeopleCopy()){
            market.removePerson(person);
        }
        Global.getSector().getEconomy().removeMarket(market);
        //Global.getLogger(FactionValidator.class).info("market.getPrimaryEntity().getName(): " + market.getPrimaryEntity().getName());
        //Global.getLogger(FactionValidator.class).info("market.getPrimaryEntity().getId(): " + market.getPrimaryEntity().getId());
        var system = market.getStarSystem();
        market.clearTags();
        RemoveMarketLocation(market);
    }
    public void RemoveMarketV1(MarketAPI market){
        // --- Detach market from its entity (planet/station), but KEEP the entity ---

        SectorEntityToken entity = market.getPrimaryEntity();

        if (entity != null) {
            entity.setFaction("neutral");

            entity.setMarket(null);
        }
        // --- Optional: clear submarket references cleanly ---
        market.getConnectedEntities().forEach(e -> e.setMarket(null));
        market.getSubmarketsCopy().forEach(sub -> market.removeSubmarket(sub.getSpecId()));

        // --- Finally remove the market from the economy ---
        Global.getSector().getEconomy().removeMarket(market);
    }
    public void RemoveMarketLocation(MarketAPI market){
        for(var seToken : market.getConnectedEntities()){
            seToken.getContainingLocation().removeEntity(seToken);
        }
        if (market.getPrimaryEntity() != null) {
            SectorEntityToken entity = market.getPrimaryEntity();
            if (entity.getContainingLocation() != null) {
                entity.getContainingLocation().removeEntity(entity);
            }
        }

        // Remove market from economy
        Global.getSector().getEconomy().removeMarket(market);
    }

}

