package net.noahvolson.rpgmod.entity.skill.warrior;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.Vec3;
import net.noahvolson.rpgmod.effect.ModEffects;
import net.noahvolson.rpgmod.entity.skill.Skill;
import net.noahvolson.rpgmod.particle.ModParticles;

import java.util.ArrayList;
import java.util.List;

public class DefianceSkill implements Skill {
    private final int DURATION = 30;
    private final int RADIUS = 4;

    public DefianceSkill(ServerPlayer player) {
    }

    @Override
    public void use(ServerPlayer player) {
        if (player.level instanceof ServerLevel serverLevel) {
            List<LivingEntity> list = player.level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(RADIUS, RADIUS, RADIUS));
            for (LivingEntity target : list) {
                target.setSecondsOnFire(3);
                target.knockback(1.25D, player.getX() - target.getX(), player.getZ() - target.getZ());
            }
            ArrayList<Vec3> points = getSpherePoints(1500, RADIUS);
            for (Vec3 point : points) {
                Vec3 shifted = point.add(player.position());
                serverLevel.sendParticles(ModParticles.SHELL_PARTICLES.get(), shifted.x, shifted.y + 1, shifted.z, 1, 0, 0, 0, 0);
            }
            player.addEffect(new MobEffectInstance(ModEffects.SHELL.get(), DURATION, 0, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, DURATION, 4, false, false, false));
        }
    }

    private ArrayList<Vec3> getSpherePoints(int samples, int r) {
        ArrayList<Vec3> points = new ArrayList<>();
        double phi = Math.PI * (Math.sqrt(5.) - 1.);        // golden angle in radians

        for (int i = 0; i < samples; i++) {
            float y = 1 - (i / (float)(samples - 1)) * 2;   // y goes from 1 to -1
            double radius = Math.sqrt(1 - y * y);           // radius at y
            double theta = phi * i;
            double x = Math.cos(theta) * radius;
            double z = Math.sin(theta) * radius;
            points.add(new Vec3(x * r, y * r, z * r));
        }
        return points;
    }

    @Override
    public void useTurnover(ServerPlayer player) {

    }

    @Override
    public boolean canUseTurnover(ServerPlayer player) {
        return false;
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
