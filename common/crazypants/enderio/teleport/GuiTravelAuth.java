package crazypants.enderio.teleport;

import crazypants.gui.GuiContainerBase;
import crazypants.render.ColorUtil;
import crazypants.render.RenderUtil;
import crazypants.util.Lang;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiTravelAuth extends GuiContainerBase {

  private final String title;
  private final ITravelAccessable ta;
  private final String username;
  private boolean failed = false;

  public GuiTravelAuth(EntityPlayer player, ITravelAccessable te, World world) {
    super(new ContainerTravelAuth(player.inventory));
    this.ta = te;
    title = Lang.localize("gui.travelAccessable.enterCode");
    username = player.getUniqueID().toString();
  }

  @Override
  public void initGui() {
    super.initGui();
    int sx = (width - xSize) / 2;
    int sy = (height - ySize) / 2;
    String str = Lang.localize("gui.travelAccessable.ok");
    int strLen = getFontRenderer().getStringWidth(str);
    GuiButton okB = new GuiButton(0, width / 2 - (strLen / 2) - 5, sy + 50, strLen + 10, 20, str);
    buttonList.clear();
    buttonList.add(okB);
  }

  @Override
  protected void actionPerformed(GuiButton par1GuiButton) {
    ContainerTravelAuth poo = (ContainerTravelAuth) inventorySlots;
    if(ta.authoriseUser(username, poo.enteredPassword)) {
      TileEntity te = ((TileEntity) ta);
      //TODO: 1.7
      //PacketDispatcher.sendPacketToServer(te.getDescriptionPacket());

      this.mc.displayGuiScreen((GuiScreen) null);
      this.mc.setIngameFocus();
    } else {
      //      System.out.print("GuiTravelAuth.actionPerformed: Password is: ");
      //      for (ItemStack is : ta.getPassword()) {
      //        System.out.print((is == null ? is : is.getDisplayName()) + ",");
      //      }
      //      System.out.println();
      //      System.out.print("GuiTravelAuth.actionPerformed: I offered: ");
      //      for (ItemStack is : poo.enteredPassword) {
      //        System.out.print((is == null ? is : is.getDisplayName()) + ",");
      //      }
      //      System.out.println();
      //      System.out.println();
      failed = true;
      poo.dirty = false;
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderUtil.bindTexture("enderio:textures/gui/travelAuth.png");
    int sx = (width - xSize) / 2;
    int sy = (height - ySize) / 2;
    drawTexturedModalRect(sx, sy, 0, 0, this.xSize, this.ySize);

    int sw = getFontRenderer().getStringWidth(title);
    getFontRenderer().drawString(title, width / 2 - sw / 2, sy + 12, ColorUtil.getRGB(Color.red));

    ContainerTravelAuth poo = (ContainerTravelAuth) inventorySlots;
    if(poo.dirty) {
      poo.dirty = false;
      failed = false;
    }

    if(failed) {
      drawRect(sx + 43, sy + 27, sx + 43 + 90, sy + 27 + 18, ColorUtil.getARGB(new Color(1f, 0f, 0f, 0.5f)));
    }

  }

}
