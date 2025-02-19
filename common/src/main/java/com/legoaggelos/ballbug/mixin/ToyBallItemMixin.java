package com.legoaggelos.ballbug.mixin;

import com.github.teamfossilsarcheology.fossil.item.ToyBallItem;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import com.github.teamfossilsarcheology.fossil.entity.ModEntities;
import com.github.teamfossilsarcheology.fossil.entity.ToyBall;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;
import java.util.List;

import static com.legoaggelos.ballbug.Helper.getPlayerPOVHitResult;

@Mixin(ToyBallItem.class)
public class ToyBallItemMixin{
    @Shadow
    @Final
    private static Predicate<Entity> ENTITY_PREDICATE;
    @Shadow
    @Final
    private DyeColor color;


    @Overwrite
    public @NotNull InteractionResultHolder<ItemStack>  m_7203_(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        }
        Vec3 vec3 = player.getViewVector(1.0f);
        List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(5)).inflate(1), ENTITY_PREDICATE);
        if (!list.isEmpty()) {
            Vec3 eyePos = player.getEyePosition();
            for (Entity entity : list) {
                AABB aABB = entity.getBoundingBox().inflate(entity.getPickRadius());
                if (!aABB.contains(eyePos)) continue;
                return InteractionResultHolder.pass(itemStack);
            }
        }
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            if (level instanceof ServerLevel serverLevel) {
                ToyBall entity = ModEntities.TOY_BALL.get().create(serverLevel);
                if (entity == null) {
                    return InteractionResultHolder.fail(itemStack);
                }
                entity.setColor(color);
                entity.moveTo(hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 0, 0);
                level.addFreshEntity(entity);
            }
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }
        return InteractionResultHolder.pass(itemStack);
    }
}