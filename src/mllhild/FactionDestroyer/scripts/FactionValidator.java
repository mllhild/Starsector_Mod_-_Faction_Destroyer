package mllhild.FactionDestroyer;

import com.fs.starfarer.api.Global;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import mllhild.FactionDestroyer.scripts.DataStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FactionValidator {

    // ===== CONFIG =====

    public final boolean permitCommonTech = true;
    public final boolean permitCommissionTech = true;
    public final boolean loseAccessToTechUponCommisionExit = false;

    // Optional: allow cross-faction compatibility
    public final Set<String> UNIVERSAL_WEAPONS = new HashSet<>();
    public final Set<String> UNIVERSAL_HULLMODS = new HashSet<>();
    public final Set<String> COMISSIONED_WEAPONS = new HashSet<>();
    public final Set<String> COMISSIONED_HULLMODS = new HashSet<>();


    public void debugPrintPermissionLists() {
        for (String id : UNIVERSAL_WEAPONS) {
            Global.getLogger(FactionValidator.class).info("UNIVERSAL_WEAPONS: " + id);
        }
        for (String id : UNIVERSAL_HULLMODS) {
            Global.getLogger(FactionValidator.class).info("UNIVERSAL_HULLMODS: " + id);
        }
        for (String id : COMISSIONED_WEAPONS) {
            Global.getLogger(FactionValidator.class).info("COMISSIONED_WEAPONS: " + id);
        }
        for (String id : COMISSIONED_HULLMODS) {
            Global.getLogger(FactionValidator.class).info("COMISSIONED_HULLMODS: " + id);
        }
    }

    //  ===== BUILD PERMISSION LIST =====
    public DataStorage ds = new DataStorage();



    public void permitVanillaTech(){
        for(int i = 0;i<ds.vanillaFactions.size(); i++){
            addWeaponsToUniversalByFaction(ds.vanillaFactions.get(i));
        }
        for(int i = 0;i<ds.vanillaFactions.size(); i++){
            addHullmodsToUniversalByFaction(ds.vanillaFactions.get(i));
        }
        for(int i = 0;i<ds.vanillaWeapons.size(); i++){
            UNIVERSAL_WEAPONS.add(ds.vanillaWeapons.get(i));
            Global.getLogger(FactionValidator.class).info("addWeaponsToUniversal: " + ds.vanillaWeapons.get(i));
        }
        for(int i = 0;i<ds.vanillaHullmods.size(); i++){
            UNIVERSAL_HULLMODS.add(ds.vanillaHullmods.get(i));
            Global.getLogger(FactionValidator.class).info("addWeaponsToUniversal: " + ds.vanillaHullmods.get(i));
        }
    }
    public void permitVanillaAdjacentTech(){
        for(int i = 0;i<ds.vanillaModPacks.size(); i++)
            addHullmodsToUniversalByPrefix(ds.vanillaModPacks.get(i));
        for(int i = 0;i<ds.vanillaModPacks.size(); i++)
            addWeaponsToUniversalByPrefix(ds.vanillaModPacks.get(i));
    }
    public void permitHullmodPackTech(){
        for(int i = 0;i<ds.hullmodModPacks.size(); i++)
            addHullmodsToUniversalByPrefix(ds.vanillaModPacks.get(i));
        for(int i = 0;i<ds.hullmodModPacks.size(); i++)
            addWeaponsToUniversalByPrefix(ds.vanillaModPacks.get(i));
    }
    public void enterCommission(String faction){
        // adds all the nice new tech your faction grants you
        String prefix = getFactionFromId(faction);
        addWeaponsToUniversalByPrefix(prefix);
        addHullmodsToUniversalByPrefix(prefix);
    }
    public void exitCommission(){
        if(!loseAccessToTechUponCommisionExit) return;
        COMISSIONED_HULLMODS.clear();
        COMISSIONED_WEAPONS.clear();
        drmLockCommissionTech();
    }
    public void drmLockCommissionTech(){}
    public void permitCommissionTech(){}

    public void addWeaponsToUniversalByFaction(String factionId) {
        if (factionId == null) return;
        factionId = factionId.toLowerCase();
        for (WeaponSpecAPI spec : Global.getSettings().getAllWeaponSpecs()) {
            if (spec == null) continue;
            String weaponId = spec.getWeaponId();
            String weaponFaction = getWeaponFaction(weaponId);
            if (factionId.equals(weaponFaction)) {
                UNIVERSAL_WEAPONS.add(weaponId);
                Global.getLogger(FactionValidator.class).info("addWeaponsToUniversalByFaction: " + factionId + " " + weaponId);
            }
        }
    }
    public void addWeaponsToUniversalByPrefix(String prefix) {
        if (prefix == null) return;

        prefix = prefix.toLowerCase();

        Global.getLogger(FactionValidator.class).info("addWeaponsToUniversalByPrefix: " + prefix);

        for (WeaponSpecAPI spec : Global.getSettings().getAllWeaponSpecs()) {
            if (spec == null) continue;

            String weaponId = spec.getWeaponId().toLowerCase();

            if (weaponId.startsWith(prefix + "_")) {
                UNIVERSAL_WEAPONS.add(spec.getWeaponId());
                Global.getLogger(FactionValidator.class).info("addWeaponsToUniversalByPrefix: " + prefix + " " + weaponId);
            }
        }
    }

    public void addHullmodsToUniversalByFaction(String factionId) {
        if (factionId == null) return;
        factionId = factionId.toLowerCase();
        for (HullModSpecAPI spec : Global.getSettings().getAllHullModSpecs()) {
            if (spec == null) continue;
            String hullmodId = spec.getId();
            String modFaction = getHullmodFaction(spec);
            if (factionId.equals(modFaction)) {
                UNIVERSAL_HULLMODS.add(hullmodId);
                Global.getLogger(FactionValidator.class).info("addHullmodsToUniversalByFaction: " + factionId + " " + hullmodId);
            }
        }
    }
    public void addHullmodsToUniversalByPrefix(String prefix) {
        if (prefix == null) return;

        prefix = prefix.toLowerCase();

        Global.getLogger(FactionValidator.class).info("addHullmodsToUniversalByPrefix: " + prefix);

        for (HullModSpecAPI spec : Global.getSettings().getAllHullModSpecs()) {
            if (spec == null) continue;

            String id = spec.getId().toLowerCase();

            if (id.startsWith(prefix + "_")) {
                UNIVERSAL_HULLMODS.add(spec.getId());
                Global.getLogger(FactionValidator.class).info("addHullmodsToUniversalByPrefix: " + prefix + " " + id);
            }
        }
    }

    private String getHullmodFaction(HullModSpecAPI spec) {

//        // Strategy 1: manufacturer (if defined)
//        if (spec.getManufacturer() != null && !spec.getManufacturer().isEmpty()) {
//            return spec.getManufacturer().toLowerCase();
//        }
//
//        // Strategy 2: UI tags (some mods use these)
//        for (String tag : spec.getTags()) {
//            if (isLikelyFactionTag(tag)) {
//                return tag.toLowerCase();
//            }
//        }

        // Strategy 3: fallback to ID prefix
        return getFactionFromId(spec.getId());
    }

    private boolean isLikelyFactionTag(String tag) {
        // You can expand this later
        return !tag.equals("common") &&
                !tag.equals("no_drop") &&
                !tag.equals("base_bp") &&
                !tag.equals("rare_bp");
    }

    {
        // Example:
        // UNIVERSAL_WEAPONS.add("heavymauler");
    }

    // ===== ENTRY POINTS =====

    public void Init(){
        permitVanillaTech();
        permitVanillaAdjacentTech();
        applyToPlayerFleet();
        checkCommission();
    };

    public void validatePlayerFleet() {
        Init();

        CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();
        if (fleet == null) return;

        for (FleetMemberAPI member : fleet.getFleetData().getMembersListCopy()) {
            validateFleetMember(member);
        }
    }


    public void validateShip(ShipAPI ship) {
        if (ship == null) return;
        validateFleetMember(ship.getFleetMember());
    }

    public void validateFleetMember(FleetMemberAPI member) {
        if (member == null) return;

        ShipVariantAPI variant = member.getVariant().clone();
        variant.setSource(VariantSource.REFIT);
        String shipFaction = getShipFactionFromVariant(variant);

        Global.getLogger(FactionValidator.class).info("validateFleetMember: " + variant.getDisplayName());

        boolean changed = false;

        // weapons
        List<String> slots = new ArrayList<>(variant.getFittedWeaponSlots());

        for (String slotId : slots) {

            String weaponId = variant.getWeaponId(slotId);
            Global.getLogger(FactionValidator.class).info("validateFleetMember - Slot: " + slotId);
            Global.getLogger(FactionValidator.class).info("validateFleetMember - Weapon: " + weaponId);
            if (weaponId == null) continue;

            if (!isWeaponAllowed(shipFaction, weaponId)) {

                if(variant.getSlot(slotId).isWeaponSlot())
                {
                    //variant.addWeapon(slotId,"mininglaser");
                    variant.clearSlot(slotId);
                    //Global.getSector().getPlayerFleet().getCargo().addWeapons(weaponId, 1);

                    Global.getLogger(FactionValidator.class).info("validateFleetMember - Removed Weapon: " + weaponId);
                    changed = true;
                }
            }
        }



        // hullmods

        Set<String> toRemove = new HashSet<>();

        for (String hullmodId : variant.getHullMods()) {

            // Skip built-ins (important!)
            if (variant.getHullSpec().isBuiltInMod(hullmodId)) continue;

            // Skip our own enforcement mod
            if (hullmodId.equals("fd_faction_lock")) continue;

            if (!isHullmodAllowed(shipFaction, hullmodId)) {
                toRemove.add(hullmodId);
                Global.getLogger(FactionValidator.class).info("validateFleetMember - Removed Hullmod: " + hullmodId);
                changed = true;
            }
        }

        for (String id : toRemove) {
            variant.removeMod(id);
        }

        if (changed) {
            member.setVariant(variant, false, true);
            member.updateStats();

        }
    }


    // ===== RULES =====

    private boolean isWeaponAllowed(String shipFaction, String weaponId) {
        if(permitCommonTech)
            if (UNIVERSAL_WEAPONS.contains(weaponId)) return true;
        if(permitCommissionTech)
            if (UNIVERSAL_WEAPONS.contains(weaponId)) return true;

        String weaponFaction = getWeaponFaction(weaponId);
        return shipFaction.equals(weaponFaction);
    }

    private boolean isHullmodAllowed(String shipFaction, String hullmodId) {
        if(permitCommonTech)
            if (UNIVERSAL_HULLMODS.contains(hullmodId)) return true;
        if(permitCommissionTech)
            if (COMISSIONED_HULLMODS.contains(hullmodId)) return true;

        String modFaction = getFactionFromId(hullmodId);
        return shipFaction.equals(modFaction);
    }

    // ===== FACTION RESOLUTION =====

    public String getShipFaction(ShipAPI ship) {
        return getFactionFromId(ship.getHullSpec().getHullId());
    }

    public String getShipFactionFromVariant(ShipVariantAPI variant) {
        return getFactionFromId(variant.getHullSpec().getHullId());
    }

    public String getWeaponFaction(String weaponId) {
        WeaponSpecAPI spec = Global.getSettings().getWeaponSpec(weaponId);

        // Strategy 1: manufacturer (if mod uses it properly)
        if (spec.getManufacturer() != null && !spec.getManufacturer().isEmpty()) {
            return spec.getManufacturer().toLowerCase();
        }

        // Strategy 2 fallback: ID prefix
        return getFactionFromId(weaponId);
    }

    /**
     * Generic fallback: extract prefix before first underscore
     * Example:
     *   "hegemony_lasercannon" → "hegemony"
     *   "diableavionics_xyz" → "diableavionics"
     */
    public String getFactionFromId(String id) {
        if (id == null) return "unknown";

        int index = id.indexOf("_");
        if (index == -1) return id.toLowerCase();

        return id.substring(0, index).toLowerCase();
    }



    public void applyToPlayerFleet() {
        CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();

        if (fleet == null) return;

        for (FleetMemberAPI member : fleet.getFleetData().getMembersListCopy()) {
            ShipVariantAPI variant = member.getVariant();

            if (!variant.hasHullMod("fd_faction_lock")) {
                variant.addPermaMod("fd_faction_lock", false);
            }
        }
    }



    // ===== COMMISSION CHANGE =====

    public String lastCommissionFaction = null;
    public void checkCommission() {

        String current = Global.getSector()
                .getMemoryWithoutUpdate()
                .getString("$playerCommissionFaction");

        if (current != null) current = current.toLowerCase();


        // No change → do nothing
        if ((current == null && lastCommissionFaction == null) ||
                (current != null && current.equals(lastCommissionFaction))) {
            return;
        }

        boolean isVanilla = false;
        for(int i = 0;i<ds.vanillaFactions.size(); i++)
            if(ds.vanillaFactions.get(i) == current)
                isVanilla = true;
        if(isVanilla){
            exitCommission();
            addWeaponsToUniversalByFaction(current);
            addHullmodsToUniversalByFaction(current);
        } else {
            exitCommission();
            enterCommission(current);
        }

        lastCommissionFaction = current;

        // Optional: revalidate fleet immediately
        validatePlayerFleet();


    }


}