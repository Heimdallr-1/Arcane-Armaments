package net.noahvolson.rpgmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.player.Player;
import net.noahvolson.rpgmod.RpgMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.noahvolson.rpgmod.client.ClientRpgClassData;
import net.noahvolson.rpgmod.rpgclass.RpgClass;

import javax.swing.text.AttributeSet;
import javax.swing.text.Style;

import static net.noahvolson.rpgmod.rpgclass.RpgClasses.*;
import static net.noahvolson.rpgmod.rpgclass.RpgClasses.CLERIC;

public class GemInfusingStationScreen extends AbstractContainerScreen<GemInfusingStationMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RpgMod.MOD_ID,"textures/gui/gem_infusing_station_gui.png");

    public GemInfusingStationScreen(GemInfusingStationMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight + 2);

        renderSkillInfo(pPoseStack, x, y, pMouseX, pMouseY);

        if (this.menu.renderPressedButton(0)) {
            blit(pPoseStack, x + 76, y + 8, 0, 202, 75, 16);
        }
        if (this.menu.renderPressedButton(1)) {
            blit(pPoseStack, x + 76, y + 25, 0, 202, 75, 16);
        }
        if (this.menu.renderPressedButton(2)) {
            blit(pPoseStack, x + 76, y + 42, 0, 202, 75, 16);
        }
        if (this.menu.renderPressedButton(3)) {
            blit(pPoseStack, x + 76, y + 59, 0, 202, 75, 16);
        }

        RpgClass rpgClass = this.menu.getRpgClass();
        if (rpgClass != null) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderTexture(0, rpgClass.getSkill1().getIcon());
            GuiComponent.blit(pPoseStack,x + 60, y + 9,0,0,15,15, 15,15);
            RenderSystem.setShaderTexture(0, rpgClass.getSkill2().getIcon());
            GuiComponent.blit(pPoseStack,x + 60, y + 26,0,0,15,15, 15,15);
            RenderSystem.setShaderTexture(0, rpgClass.getSkill3().getIcon());
            GuiComponent.blit(pPoseStack,x + 60, y + 43,0,0,15,15, 15,15);
            RenderSystem.setShaderTexture(0, rpgClass.getSkill4().getIcon());
            GuiComponent.blit(pPoseStack,x + 60, y + 60,0,0,15,15, 15,15);

            this.font.draw(pPoseStack,  rpgClass.getSkill1().getLabel(), x + 80, y + 12, 4537905);
            this.font.draw(pPoseStack, rpgClass.getSkill2().getLabel(), x + 80, y + 29, 4537905);
            this.font.draw(pPoseStack, rpgClass.getSkill3().getLabel(), x + 80, y + 46, 4537905);
            this.font.draw(pPoseStack, rpgClass.getSkill4().getLabel(), x + 80, y + 63, 4537905);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int p_98760_) {
        if (this.minecraft != null && this.minecraft.player != null && this.minecraft.gameMode != null) {
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;
            if (pMouseX >= x + 76 && pMouseX < x + 151) {
                if (pMouseY >= y + 8 && pMouseY <= y + 24 && this.menu.clickMenuButton(this.minecraft.player, 0)) {
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, 0);
                    return true;
                }
                else if (pMouseY >= y + 25 && pMouseY <= y + 41 && this.menu.clickMenuButton(this.minecraft.player, 1)) {
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, 1);
                    return true;
                }
                else if (pMouseY >= y + 42 && pMouseY <= y + 58 && this.menu.clickMenuButton(this.minecraft.player, 2)) {
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, 2);
                    return true;
                }
                else if (pMouseY >= y + 59 && pMouseY <= y + 75 && this.menu.clickMenuButton(this.minecraft.player, 3)) {
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, 3);
                    return true;
                }
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, p_98760_);
    }

    private void renderSkillInfo(PoseStack pPoseStack, int x, int y, int pMouseX, int pMouseY) {
        if (pMouseX >= x + 76 && pMouseX < x + 151) {
            if (pMouseY >= y + 8 && pMouseY <= y + 24) {
                blit(pPoseStack, x + 76, y + 8, 0, 185, 75, 16);
            }
            else if (pMouseY >= y + 25 && pMouseY <= y + 41) {
                blit(pPoseStack, x + 76, y + 25, 0, 185, 75, 16);
            }
            else if (pMouseY >= y + 42 && pMouseY <= y + 58) {
                blit(pPoseStack, x + 76, y + 42, 0, 185, 75, 16);
            }
            else if (pMouseY >= y + 59 && pMouseY <= y + 75) {
                blit(pPoseStack, x + 76, y + 59, 0, 185, 75, 16);
            }
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }
}