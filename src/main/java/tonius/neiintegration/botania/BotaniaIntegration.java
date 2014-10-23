package tonius.neiintegration.botania;

import tonius.neiintegration.IntegrationBase;
import cpw.mods.fml.common.Loader;

public class BotaniaIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Botania";
    }
    
    @Override
    public boolean isValid() {
        return Loader.isModLoaded("Botania");
    }
    
    @Override
    public void loadConfig() {
        registerHandler(new RecipeHandlerElvenTrade());
        registerHandler(new RecipeHandlerManaPool());
        registerHandler(new RecipeHandlerPetalApothecary());
        registerHandler(new RecipeHandlerRunicAltar());
    }
    
}