package mllhild.FactionDestroyer;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.CoreUITabListener;
import com.fs.starfarer.api.campaign.CoreUITabId;

public class MyRefitListener implements CoreUITabListener {

    @Override
    public void reportAboutToOpenCoreTab(CoreUITabId tab, Object param) {
        // yes this looks stupid, but something is preserving the state of the ship while the refit screen is open.
        // and I didnt find any force UI update method.
        if (tab == CoreUITabId.REFIT) {
            //onRefitOpened();
        }
        else{
            onRefitOpened();
        }
    }

//    @Override
//    public void reportCoreTabOpened(CoreUITabId tab, Object param) {
//        // Optional: fires after opening instead of before
//    }
//
//    @Override
//    public void reportCoreTabClosed(CoreUITabId tab) {
//        // Optional
//    }

    private void onRefitOpened() {
        // Your logic here
        Global.getLogger(MyRefitListener.class).info("FactionDestroyerModPlugin: onRefitOpened");
        mllhild.FactionDestroyer.FactionValidator factionValidator = new mllhild.FactionDestroyer.FactionValidator();
        factionValidator.validatePlayerFleet();
    }
}