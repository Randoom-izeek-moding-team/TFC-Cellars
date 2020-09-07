package randoom97.cellars.guis;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import randoom97.cellars.Cellars;
import randoom97.cellars.config.CellarsConfig;
import randoom97.cellars.containers.ContainerCellarShelf;
import randoom97.cellars.tiles.TileCellarShelf;

public class GuiCellarShelf extends GuiContainer {

	public static final int WIDTH = 175;
	public static final int HEIGHT = 165;
	
	private static final ResourceLocation background = new ResourceLocation(Cellars.MODID, "textures/gui/cellar_shelf.png");
	
	private TileCellarShelf tileCellarShelf;
	
	public GuiCellarShelf(TileCellarShelf tileEntity, ContainerCellarShelf container) {
		super(container);
		
		this.tileCellarShelf = tileEntity;
		
		xSize = WIDTH;
		ySize = HEIGHT;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void renderHoveredToolTip(int mx, int my) {
		if(mx >= guiLeft + 5 && mx <= guiLeft+15 && my >= guiTop+5 && my <= guiTop + 15) {
			List<String> infoText = new ArrayList<String>();
			infoText.add("Status: "+ (tileCellarShelf.getTimeToDisconnect() < 0 ? "disconnected" : "connected"));
			infoText.add("Cold at: "+String.format("%.2f", CellarsConfig.COLD_TEMP)+"°C");
			infoText.add("Frozen at: "+String.format("%.2f", CellarsConfig.FROZEN_TEMP)+"°C");
			this.drawHoveringText(infoText, mx, my);
		}
		super.renderHoveredToolTip(mx, my);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.fontRenderer.drawString("Cellar Shelf: "+String.format("%.2f", tileCellarShelf.getTemperature())+"°C", guiLeft+20, guiTop+7, 0x000000);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
}
