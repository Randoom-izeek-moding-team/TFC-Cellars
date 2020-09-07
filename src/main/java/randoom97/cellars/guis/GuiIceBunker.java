package randoom97.cellars.guis;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import randoom97.cellars.Cellars;
import randoom97.cellars.containers.ContainerIceBunker;
import randoom97.cellars.tiles.TileIceBunker;

public class GuiIceBunker extends GuiContainer {

	public static final int WIDTH = 175;
	public static final int HEIGHT = 165;
	
	private static final ResourceLocation background = new ResourceLocation(Cellars.MODID, "textures/gui/block_ice_bunker.png");
	
	private TileIceBunker te;
	
	public GuiIceBunker(TileIceBunker tileEntity, ContainerIceBunker container) {
		super(container);
		
		this.te = tileEntity;
		
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
			// multiblock info here
			infoText.add("Staus: " + (te.getFormed() ? "formed" : "unformed"));
			if(te.getFormed()) {
				infoText.add("Doors: " + (te.getDoorsFound() ? (te.getDoorsOpen() ? "open" : "closed"): "not found"));
			}
			this.drawHoveringText(infoText, mx, my);
		}
		super.renderHoveredToolTip(mx, my);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.fontRenderer.drawString("Ice Bunker: "+String.format("%.2f", te.getTemperature())+"°C", guiLeft+20, guiTop+7, 0x000000);
		renderHoveredToolTip(mouseX, mouseY);
	}

}
