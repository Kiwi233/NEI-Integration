package tonius.neiintegration.railcraft;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import mods.railcraft.api.crafting.IRockCrusherRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import tonius.neiintegration.Hacks;
import tonius.neiintegration.PositionedStackAdv;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;

public class RecipeHandlerRockCrusher extends RecipeHandlerBase {
    
    private static final int[][] RESULTS = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 }, { 0, 2 }, { 1, 2 }, { 2, 2 } };
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Hacks.getClass("mods.railcraft.client.gui.GuiRockCrusher");
        API.setGuiOffset(guiClass, -3, 11);
    }
    
    public class CachedRockCrusherRecipe extends CachedBaseRecipe {
        
        public PositionedStack input;
        public List<PositionedStack> outputs = new ArrayList<PositionedStack>();
        
        public CachedRockCrusherRecipe(IRockCrusherRecipe recipe) {
            this.input = new PositionedStack(recipe.getInput(), 12, 10);
            this.setResults(recipe.getOutputs());
        }
        
        private void setResults(List<Entry<ItemStack, Float>> outputs) {
            int i = 0;
            for (Entry<ItemStack, Float> output : outputs) {
                if (i > RESULTS.length) {
                    return;
                }
                this.outputs.add(new PositionedStackAdv(output.getKey(), 102 + RESULTS[i][0] * 18, 10 + RESULTS[i][1] * 18, output.getValue()));
                i++;
            }
        }
        
        @Override
        public PositionedStack getIngredient() {
            this.randomRenderPermutation(this.input, RecipeHandlerRockCrusher.this.cycleticks / 20);
            return this.input;
        }
        
        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.outputs;
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Rock Crusher";
    }
    
    @Override
    public String getRecipeID() {
        return "railcraft.rockcrusher";
    }
    
    @Override
    public String getGuiTexture() {
        return "railcraft:textures/gui/gui_crusher.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(68, 9, 29, 53), this.getRecipeID(), new Object[0]));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        changeTexture(this.getGuiTexture());
        drawTexturedModalRect(9, 0, 5, 11, 146, 65);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        this.drawProgressBar(68, 9, 176, 0, 29, 53, 48, 0);
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (IRockCrusherRecipe recipe : RailcraftCraftingManager.rockCrusher.getRecipes()) {
                this.arecipes.add(new CachedRockCrusherRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IRockCrusherRecipe recipe : RailcraftCraftingManager.rockCrusher.getRecipes()) {
            for (Entry<ItemStack, Float> output : recipe.getOutputs()) {
                if (NEIServerUtils.areStacksSameTypeCrafting(output.getKey(), result)) {
                    this.arecipes.add(new CachedRockCrusherRecipe(recipe));
                    break;
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IRockCrusherRecipe irecipe : RailcraftCraftingManager.rockCrusher.getRecipes()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getInput(), ingredient)) {
                this.arecipes.add(new CachedRockCrusherRecipe(irecipe));
            }
        }
    }
    
}
