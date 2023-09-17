package net.noahvolson.rpgmod.entity.skill.warrior;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.noahvolson.rpgmod.entity.skill.AbstractProjectileAbility;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MeatHookAttack extends AbstractProjectileAbility implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);

    public MeatHookAttack(EntityType<? extends AbstractProjectileAbility> entityType, Level world) {
        super((EntityType<AbstractProjectileAbility>) entityType, world);
    }

    public MeatHookAttack(EntityType<? extends AbstractProjectileAbility> entityType, LivingEntity shooter, Level world) {
        super((EntityType<AbstractProjectileAbility>) entityType, shooter, world, SoundEvents.METAL_HIT);
        this.setBaseDamage(0);
        this.setSpeed(2D);
    }

    @Override
    protected void doEffectsEntity(@NotNull EntityHitResult ray) {
        pullEntity(ray.getEntity());
    }

    protected void pullEntity(Entity target) {
        if (target != null && this.getOwner() != null) {
            Vec3 vec3 = (new Vec3(this.getOwner().getX() - target.getX(), this.getOwner().getY() - target.getY(), this.getOwner().getZ() - target.getZ())).scale(0.23D);
            target.setDeltaMovement(target.getDeltaMovement().add(vec3));
        }
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE; // No animations for projectile
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
