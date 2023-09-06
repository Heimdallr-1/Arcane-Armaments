package net.noahvolson.rpgmod.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.noahvolson.rpgmod.RpgMod;
import net.noahvolson.rpgmod.effect.ModEffects;
import net.noahvolson.rpgmod.networking.ModMessages;
import net.noahvolson.rpgmod.networking.packet.RpgClassSyncS2CPacket;
import net.noahvolson.rpgmod.player.PlayerRpgClass;
import net.noahvolson.rpgmod.player.PlayerRpgClassProvider;
import net.noahvolson.rpgmod.rpgclass.RpgClass;

import java.util.Objects;

import static net.noahvolson.rpgmod.rpgclass.RpgClasses.*;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = RpgMod.MOD_ID)
    public static class ForgeEvents {

        private static void setPlayerRpgClassCapabilityTick(ServerPlayer player, RpgClass rpgClass) {
            if (player != null) {
                player.getCapability(PlayerRpgClassProvider.PLAYER_RPG_CLASS).ifPresent(curClass -> {
                    if (!curClass.getRpgClass().equals(rpgClass.getId())) {
                        player.sendSystemMessage(Component.literal("Swapping to " + rpgClass.getId()).withStyle(ChatFormatting.AQUA));
                        curClass.setRpgClass(rpgClass.getId());
                        ModMessages.sendToPlayer(new RpgClassSyncS2CPacket(rpgClass.getId()), player);
                    }
                });
            }
        }

        private static void setPlayerRpgClassCapabilityJoin(ServerPlayer player, RpgClass rpgClass) {
            if (player != null) {
                player.getCapability(PlayerRpgClassProvider.PLAYER_RPG_CLASS).ifPresent(curClass -> {
                    curClass.setRpgClass(rpgClass.getId());
                    ModMessages.sendToPlayer(new RpgClassSyncS2CPacket(rpgClass.getId()), player);
                });
            }
        }

        @SubscribeEvent
        public static void onPlayerJoin(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getAttribute(Attributes.MOVEMENT_SPEED);

                ItemStack offhand = player.getOffhandItem();
                if (offhand.is(MAGE.getClassItem())) {
                    setPlayerRpgClassCapabilityJoin(player, MAGE);
                } else if (offhand.is(ROGUE.getClassItem())) {
                    setPlayerRpgClassCapabilityJoin(player, ROGUE);
                } else if (offhand.is(WARRIOR.getClassItem())) {
                    setPlayerRpgClassCapabilityJoin(player, WARRIOR);
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.player.hasEffect(ModEffects.SHELL.get())) {
                //event.player.setDeltaMovement(0,0,0);
            }
            if (event.player instanceof ServerPlayer player) {
                ItemStack offhand = player.getOffhandItem();
                if (offhand.is(MAGE.getClassItem())) {
                    setPlayerRpgClassCapabilityTick(player, MAGE);
                } else if (offhand.is(ROGUE.getClassItem())) {
                    setPlayerRpgClassCapabilityTick(player, ROGUE);
                } else if (offhand.is(WARRIOR.getClassItem())) {
                    setPlayerRpgClassCapabilityTick(player, WARRIOR);
                }
            }
        }

        @SubscribeEvent
        public static void onRenderGuiOverlayEvent(RenderGuiOverlayEvent.Pre event) {
            if (event.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type()) {
                event.setCanceled(true);
            }
        }

        // Hide armor & drawn weapons
        @SubscribeEvent
        public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
            if (event.getEntity().hasEffect(MobEffects.INVISIBILITY)) {
                event.setCanceled(true);
            }
        }

        // Break invisibility on attack
        @SubscribeEvent
        public static void onAttackEntity(AttackEntityEvent event) {
            Player player = event.getEntity();
            if (player.hasEffect(MobEffects.INVISIBILITY)) {
                player.removeEffect(MobEffects.INVISIBILITY);
            }
        }

        // Take double damage when berserking
        @SubscribeEvent
        public static void onLivingHurt(LivingHurtEvent event) {
            if (event.getEntity() instanceof Player player && player.hasEffect(ModEffects.BERSERK.get())) {
                event.setAmount(event.getAmount() * 2);
            }
            if (event.getEntity() instanceof Player player && player.hasEffect(ModEffects.SHELL.get())) {
                event.setAmount(event.getAmount() / 4);
            }
        }


        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof Player) {
                if(!event.getObject().getCapability(PlayerRpgClassProvider.PLAYER_RPG_CLASS).isPresent()) {
                    event.addCapability(new ResourceLocation(RpgMod.MOD_ID, "properties"), new PlayerRpgClassProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            if(event.isWasDeath()) {
                event.getOriginal().getCapability(PlayerRpgClassProvider.PLAYER_RPG_CLASS).ifPresent(oldStore -> {
                    event.getOriginal().getCapability(PlayerRpgClassProvider.PLAYER_RPG_CLASS).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerRpgClass.class);
        }

    }
}