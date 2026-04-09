import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.campaign.listeners.CargoGainedListener;
import com.fs.starfarer.api.campaign.listeners.CargoScreenListener;
import com.fs.starfarer.api.combat.MutableStat;

public class MyItemListener implements  CargoScreenListener {

    @Override
    public void reportCargoScreenOpened() {

    }

    @Override
    public void reportPlayerLeftCargoPods(SectorEntityToken entity) {

    }

    @Override
    public void reportPlayerNonMarketTransaction(PlayerMarketTransaction transaction, InteractionDialogAPI dialog) {

    }

    @Override
    public void reportSubmarketOpened(SubmarketAPI submarket) {

    }
}