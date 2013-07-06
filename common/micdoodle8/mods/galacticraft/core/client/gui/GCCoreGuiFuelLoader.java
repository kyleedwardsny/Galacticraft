package micdoodle8.mods.galacticraft.core.client.gui;

import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreGuiFuelLoader extends GuiContainer
{
    private static final ResourceLocation fuelLoaderTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/fuel_loader.png");

    private final GCCoreTileEntityFuelLoader fuelLoader;

    private GuiButton buttonLoadFuel;

    public GCCoreGuiFuelLoader(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityFuelLoader par2TileEntityAirDistributor)
    {
        super(new GCCoreContainerFuelLoader(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.fuelLoader = par2TileEntityAirDistributor;
        this.ySize = 180;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 17, new Object[] { this.fuelLoader.xCoord, this.fuelLoader.yCoord, this.fuelLoader.zCoord }));
            break;
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.buttonList.add(this.buttonLoadFuel = new GuiButton(0, this.width / 2 + 2, this.height / 2 - 49, 76, 20, LanguageRegistry.instance().getStringLocalization("gui.button.loadfuel.name")));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.fuelLoader.getInvName(), 60, 10, 4210752);
        this.buttonLoadFuel.enabled = this.fuelLoader.disableCooldown == 0 && !(this.fuelLoader.fuelTank.getFluid() == null || this.fuelLoader.fuelTank.getFluid().amount == 0);
        this.buttonLoadFuel.displayString = !this.fuelLoader.getDisabled() ? LanguageRegistry.instance().getStringLocalization("gui.button.stoploading.name") : LanguageRegistry.instance().getStringLocalization("gui.button.loadfuel.name");
        this.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization("gui.message.status.name") + ": " + this.getStatus(), 28, 45 + 23 - 46, 4210752);
        this.fontRenderer.drawString(ElectricityDisplay.getDisplay(GCCoreTileEntityFuelLoader.WATTS_PER_TICK * 20, ElectricUnit.WATT), 28, 56 + 23 - 46, 4210752);
        this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.fuelLoader.getVoltage(), ElectricUnit.VOLTAGE), 28, 68 + 23 - 46, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 118 + 2 + 11, 4210752);
    }

    private String getStatus()
    {
        if (this.fuelLoader.fuelTank.getFluid() == null || this.fuelLoader.fuelTank.getFluid().amount == 0)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.nofuel.name");
        }

        if (this.fuelLoader.getStackInSlot(0) == null && this.fuelLoader.ueWattsReceived == 0 && this.fuelLoader.ic2Energy == 0 && this.fuelLoader.bcEnergy == 0)
        {
            return EnumColor.DARK_RED + LanguageRegistry.instance().getStringLocalization("gui.status.missingpower.name");
        }

        if (this.fuelLoader.getDisabled())
        {
            return EnumColor.ORANGE + LanguageRegistry.instance().getStringLocalization("gui.status.ready.name");
        }

        if (this.fuelLoader.ueWattsReceived > 0 || this.fuelLoader.ic2Energy > 0 || this.fuelLoader.bcEnergy > 0)
        {
            return EnumColor.DARK_GREEN + LanguageRegistry.instance().getStringLocalization("gui.status.active.name");
        }

        return EnumColor.ORANGE + LanguageRegistry.instance().getStringLocalization("gui.status.ready.name");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(GCCoreGuiFuelLoader.fuelLoaderTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 181);

        final int fuelLevel = this.fuelLoader.getScaledFuelLevel(38);
        this.drawTexturedModalRect((this.width - this.xSize) / 2 + 7, (this.height - this.ySize) / 2 + 17 + 54 - fuelLevel, 176, 38 - fuelLevel, 16, fuelLevel);
    }
}
