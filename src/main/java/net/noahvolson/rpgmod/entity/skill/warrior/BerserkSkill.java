package net.noahvolson.rpgmod.entity.skill.warrior;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.noahvolson.rpgmod.effect.ModEffects;
import net.noahvolson.rpgmod.entity.ModEntityTypes;
import net.noahvolson.rpgmod.entity.skill.Skill;
import net.noahvolson.rpgmod.entity.skill.rogue.VenomDaggerAttack;

public class BerserkSkill implements Skill {
    private final int DURATION = 100;

    public BerserkSkill(ServerPlayer player) {
    }

    @Override
    public void use(ServerPlayer player) {
        player.addEffect(new MobEffectInstance(ModEffects.BERSERK.get(), DURATION, 0, false, false, true));
    }

    @Override
    public void useTurnover(ServerPlayer player) {
        new BerserkAttack(ModEntityTypes.BERSERK.get(), player, player.level).use(player);
    }

    @Override
    public boolean canUseTurnover(ServerPlayer player) {
        return player.hasEffect(ModEffects.BERSERK.get());
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public boolean isInvisibleCausing() {
        return false;
    }

}
