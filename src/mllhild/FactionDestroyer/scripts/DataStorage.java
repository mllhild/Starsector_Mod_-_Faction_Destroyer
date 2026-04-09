package mllhild.FactionDestroyer.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;

import java.util.Arrays;
import java.util.List;

public class DataStorage {

    public DataStorage( ){
        // get list of all mod factions
        if(Global.getSector() == null)
            return;
        List<FactionAPI> allFactions = Global.getSector().getAllFactions();
        FactionAPI playerFaction = Global.getSector().getPlayerFaction();
        for(int i=allFactions.size()-1;i>=0;i--){
            if(vanillaFactions.contains(allFactions.get(i).getId()));
                allFactions.remove(i);
        }
        if(allFactions.contains(playerFaction))
            allFactions.remove(playerFaction);
        for(int i=allFactions.size()-1;i>=0;i--){
            modfactions.add(allFactions.get(i).getId());
        }
    }


    public List<String> vanillaFactions = Arrays.asList(
            "neutral","pirates","hegemony","independent","tritachyon","sindrian_diktat",
            "lions_guard","knights_of_ludd","luddic_church","luddic_path","persean_league","derelict",
            "remnants","omega","threat","dweller","scavengers","sleeper","poor","mercenary");

    public List<String> vanillaWeapons = Arrays.asList(
            "lightmg", "lightdualmg", "lightmortar", "lightmortar_fighter", "vulcan", "lightac",
            "lightdualac", "lightag", "fragbomb", "clusterbomb", "bomb", "lightneedler", "railgun",
            "shredder", "heavymg", "heavymortar", "flak", "arbalest", "dualflak", "chaingun", "heavymauler",
            "heavyac", "hveldriver", "heavyneedler", "gauss", "hephag", "mark9", "devastator", "mjolnir",
            "hellbore", "multineedler", "reaper", "atropos", "atropos_single", "hammer", "hammer_single",
            "swarmer", "swarmer_fighter", "annihilator", "annihilator_fighter", "heatseeker", "harpoon",
            "harpoon_single", "breach", "sabot", "sabot_single", "sabot_fighter", "gorgon", "gorgon_payload",
            "gazer", "gazer_payload", "harpoonpod", "dragon", "dragon_payload", "gorgonpod", "gazerpod", "breachpod",
            "sabotpod", "salamanderpod", "annihilatorpod", "pilum", "phasecl", "phasecl_bomber", "jackhammer",
            "typhoon", "cyclone", "pilum_large", "hurricane", "dragonpod", "hydra",
            "hydra_payload", "squall", "locust", "hammerrack", "mininglaser", "pdlaser", "taclaser", "ioncannon",
            "ioncannon_fighter", "irpulse", "irpulse_fighter", "lrpdlaser", "pdburst", "pdburst_fighter", "amblaster",
            "phasebeam", "gravitonbeam", "irautolance", "pulselaser", "miningblaster", "heavyblaster", "kineticblaster",
            "heavyburst", "ionpulser", "ionbeam", "plasma", "hil", "autopulse", "guardian", "tachyonlance",
            "gigacannon", "minipulser", "shockrepeater", "riftlance", "riftlance_minelayer", "riftbeam",
            "riftbeam_minelayer", "cryoflux", "cryoblaster", "disintegrator", "riftcascade", "riftcascade_minelayer",
            "vpdriver", "realitydisruptor", "amsrm", "resonatormrm", "rifttorpedo", "swarm_launcher", "seeker_fragment",
            "kinetic_fragments", "unstable_fragment", "devouring_swarm", "voltaic_discharge", "neutron_torpedo",
            "light_mass_driver", "heavy_mass_driver", "neoferric_quadcoil", "voltaic_cannon", "voidblaster",
            "pseudoparticle_jet", "assaying_rift", "rift_lightning", "rift_lightning_minelayer", "abyssal_glare",
            "inimical_emanation", "vortex_launcher", "interdictorbeam", "canister_flak", "flarelauncher1",
            "flarelauncher2", "flarelauncher21", "flarelauncher3", "motelauncher", "motelauncher_hf", "minelayer1",
            "minelayer2", "od_bomblauncher", "nb_bomblauncher", "tpc", "heavy_adjudicator", "terminator_missile",
            "pusherplate_lt", "pusherplate_lt_small", "pusherplate_nova", "sensordish", "lidardish", "novalaser",
            "blinker_red", "blinker_green", "lights_buffalo", "lights_hound", "targetinglaser1", "targetinglaser2",
            "targetinglaser3");

    public List<String> vanillaHullmods = Arrays.asList(
            "ablative_armor", "adaptive_coils", "adaptiveshields", "additional_berthing", "advanced_ground_support",
            "advancedcore", "advancedoptics", "advancedshieldemitter", "always_detaches", "anchorrotation",
            "andrada_mods", "armoredweapons", "assault_package", "augmentedengines", "automated", "autorepair",
            "auxiliary_fuel_tanks", "auxiliarythrusters", "axialrotation", "ballistic_rangefinder", "bdeck",
            "blast_doors", "chassis_storage", "civgrade", "coherer", "comp_armor", "comp_hull", "comp_storage",
            "comp_structure", "converted_bay", "converted_fighterbay", "converted_hangar", "damaged_deck",
            "damaged_mounts", "dedicated_targeting_core", "defective_manufactory", "defensive_targeting_array",
            "degraded_drive_field", "degraded_engines", "degraded_life_support", "degraded_shields", "delicate",
            "design_compromises", "destroyed_mounts", "distributed_fire_control", "do_not_back_off",
            "do_not_fire_through", "drive_field_stabilizer", "dweller_hullmod", "eccm", "ecm", "efficiency_overhaul",
            "erratic_injector", "escort_package", "ex_phase_coils", "expanded_cargo_holds", "expanded_deck_crew",
            "extendedshieldemitter", "faulty_auto", "faulty_grid", "fluxbreakers", "fluxcoil", "fluxdistributor",
            "fluxshunt", "fourteenth", "fragile_subsystems", "fragment_coordinator", "fragment_swarm", "frontemitter",
            "frontshield", "glitched_sensors", "ground_support", "hardened_subsystems", "hardenedshieldemitter",
            "hbi", "heavyarmor", "heg_militarized", "high_frequency_attractor", "high_maintenance", "high_scatter_amp",
            "hiressensors", "ill_advised", "increased_maintenance", "insulatedengine", "magazines",
            "malfunctioning_comms", "militarized_subsystems", "missile_autoloader", "missile_reload", "missleracks",
            "nav_relay", "neural_integrator", "neural_interface", "never_detaches", "no_weapon_flux",
            "operations_center", "pdintegration", "phase_anchor", "phasefield", "pointdefenseai", "recovery_shuttles",
            "reduced_explosion", "reinforcedhull", "repair_gantry", "rugged", "safetyoverrides",
            "secondary_fabricator", "shard_spawner", "shared_flux_sink", "shield_always_on", "shield_shunt",
            "shielded_holds", "shrouded_lens", "shrouded_mantle", "shrouded_thunderhead", "solar_shielding",
            "stabilizedshieldemitter", "stealth_minefield", "stealth_minefield_lt", "supercomputer",
            "surveying_equipment", "targetingunit", "terminator_core", "threat_hullmod", "tow_cable",
            "turretgyros", "unstable_coils", "unstable_injector", "vast_hangar", "vastbulk");

    public List<String> vanillaModPacks = Arrays.asList("swp");
    public List<String> hullmodModPacks = Arrays.asList("ugh");
    public List<String> modfactions = Arrays.asList();


}
