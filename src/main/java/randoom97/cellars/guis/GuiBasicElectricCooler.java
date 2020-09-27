package randoom97.cellars.guis;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import randoom97.cellars.Cellars;
import randoom97.cellars.containers.ContainerBasicElectricCooler;
import randoom97.cellars.tiles.TileBasicElectricCooler;

public class GuiBasicElectricCooler extends GuiContainer {

	public static final int WIDTH = 175;
	public static final int HEIGHT = 165;
	public static final ResourceLocation GUI_ELEMENTS = new ResourceLocation(Cellars.MODID,
			"textures/gui/elements.png");
	private static final ResourceLocation background = new ResourceLocation(Cellars.MODID,
			"textures/gui/basic_electric_cooler.png");

	private TileBasicElectricCooler te;

	public GuiBasicElectricCooler(TileBasicElectricCooler tileEntity, ContainerBasicElectricCooler container) {
		super(container);
		this.te = tileEntity;
		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		// Draw the energy bar
		mc.getTextureManager().bindTexture(GUI_ELEMENTS);

		int energyPixels = Math.round(60 * te.getEnergyStored() / (float) te.getEnergyCapacity());
		// int energyPixels = 45;
		int emptyPixels = 60 - energyPixels;

		drawTexturedModalRect(guiLeft + 10, guiTop + 15 + emptyPixels, 18, emptyPixels, 18, energyPixels);
		drawTexturedModalRect(guiLeft + 10, guiTop + 15, 0, 0, 18, emptyPixels);

	}

	@Override
	protected void renderHoveredToolTip(int mx, int my) {
		if (mx >= guiLeft + 5 && mx <= guiLeft + 15 && my >= guiTop + 5 && my <= guiTop + 15) {
			List<String> infoText = new ArrayList<String>();
			// multiblock info here
			infoText.add("Staus: " + (te.getFormed() ? "formed" : "unformed"));
			if (te.getFormed()) {
				infoText.add("Doors: " + (te.getDoorsFound() ? (te.getDoorsOpen() ? "open" : "closed") : "not found"));
				
			}
			this.drawHoveringText(infoText, mx, my);
		} else if (mx >= guiLeft + 10 && mx <= guiLeft + 28 && my >= guiTop + 15 && my <= guiTop + 75) {
			List<String> infoText = new ArrayList<String>();
			// multiblock info here
			infoText.add("RF: " + (te.getEnergyStored()));
			if (te.getFormed()) {
				infoText.add("RF/Tick: " + te.getEnergyPerTick());
				//Cellars.logger.info("RF PER TICK:"+te.getEnergyPerTick());
			}
			this.drawHoveringText(infoText, mx, my);
		}
		super.renderHoveredToolTip(mx, my);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		drawDefaultBackground();

		super.drawScreen(mouseX, mouseY, partialTicks);

		this.fontRenderer.drawString("Basic Electric Cooler: " + String.format("%.2f", te.getTemperature()) + "�C",
				guiLeft + 20, guiTop + 7, 0x000000);

		renderHoveredToolTip(mouseX, mouseY);
	}

}
