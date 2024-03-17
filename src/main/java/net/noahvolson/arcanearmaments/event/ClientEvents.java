package net.noahvolson.arcanearmaments.event;

import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.noahvolson.arcanearmaments.ArcaneArmaments;
import net.noahvolson.arcanearmaments.client.ModHudOverlay;
import net.noahvolson.arcanearmaments.networking.ModMessages;
import net.noahvolson.arcanearmaments.networking.packet.AbilityC2SPacket;
import net.noahvolson.arcanearmaments.particle.ModParticles;
import net.noahvolson.arcanearmaments.particle.custom.*;
import net.noahvolson.arcanearmaments.util.KeyBinding;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = ArcaneArmaments.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.ABILITY_1_KEY.consumeClick()) {
                ModMessages.sendToServer(new AbilityC2SPacket(1));
            }
            if(KeyBinding.ABILITY_2_KEY.consumeClick()) {
                ModMessages.sendToServer(new AbilityC2SPacket(2));
            }
            if(KeyBinding.ABILITY_3_KEY.consumeClick()) {
                ModMessages.sendToServer(new AbilityC2SPacket(3));
            }
            if(KeyBinding.ABILITY_4_KEY.consumeClick()) {
                ModMessages.sendToServer(new AbilityC2SPacket(4));
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

    }

    @Mod.EventBusSubscriber(modid = ArcaneArmaments.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.ABILITY_1_KEY);
            event.register(KeyBinding.ABILITY_2_KEY);
            event.register(KeyBinding.ABILITY_3_KEY);
            event.register(KeyBinding.ABILITY_4_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerBelowAll("hud_moved_armor", ModHudOverlay.MOVED_ARMOR);
            event.registerBelowAll("hud_berserk", ModHudOverlay.HUD_BERSERK);
            event.registerBelowAll("hud_trinket_hotbar", ModHudOverlay.HUD_TRINKET_HOTBAR);
            event.registerBelowAll("hud_class_hotbar", ModHudOverlay.HUD_CLASS_HOTBAR);
            event.registerAboveAll("hud_venom", ModHudOverlay.HUD_VENOM);
            event.registerAboveAll("hud_cooldowns", ModHudOverlay.HUD_COOLDOWNS);
            event.registerAboveAll("hud_berserk_offhand", ModHudOverlay.HUD_BERSERK_OFFHAND);
        }

        @SubscribeEvent
        public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
            event.register(ModParticles.HIDDEN_PARTICLES.get(), HiddenParticle.Provider::new);
            event.register(ModParticles.BLINK_PARTICLES.get(), BlinkParticle.Provider::new);
            event.register(ModParticles.FIREBOLT_PARTICLES.get(), FireballParticle.Provider::new);
            event.register(ModParticles.ICEBOLT_PARTICLES.get(), BlizzardParticle.Provider::new);
            event.register(ModParticles.FREEZE_PARTICLES.get(), FreezeParticle.Provider::new);
            event.register(ModParticles.THUNDER_PARTICLES.get(), ThunderParticle.Provider::new);
            event.register(ModParticles.RUNE_PARTICLES.get(), RuneParticle.Provider::new);
            event.register(ModParticles.ZAPPED_PARTICLES.get(), ZappedParticle.Provider::new);
            event.register(ModParticles.VENOM_PARTICLES.get(), VenomParticle.Provider::new);
            event.register(ModParticles.BLOOD_PARTICLES.get(), BloodParticle.Provider::new);
            event.register(ModParticles.EXECUTE_PARTICLES.get(), ExecuteParticle.Provider::new);
            event.register(ModParticles.DAGGER_PARTICLES.get(), DaggerParticle.Provider::new);
            event.register(ModParticles.BERSERK_PARTICLES.get(), BerserkParticle.Provider::new);
            event.register(ModParticles.STUN_PARTICLES.get(), StunParticle.Provider::new);
            event.register(ModParticles.SHELL_PARTICLES.get(), ShellParticle.Provider::new);
            event.register(ModParticles.CHAIN_PARTICLES.get(), ChainParticle.Provider::new);
            event.register(ModParticles.BLESSED_BLADE_PARTICLES.get(), BlessedBladeParticle.Provider::new);
            event.register(ModParticles.SMITING_RAY_PARTICLES.get(), SmitingRayParticle.Provider::new);
            event.register(ModParticles.HOLY_SHIELD_PARTICLES.get(), HolyShieldParticle.Provider::new);
            event.register(ModParticles.HEAL_PARTICLES.get(), HealParticle.Provider::new);
        }
    }
}