package com.mever.alchemy_aether_codex.block;

import com.mever.alchemy_aether_codex.AlchemyAetherCodex;
import com.mever.alchemy_aether_codex.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.server.world.ServerWorld;

public class DistillationCrucibleBlock extends Block {

    // ===== 三个方块属性：记录坩埚里有什么 =====
    public static final BooleanProperty HAS_MERCURY = BooleanProperty.of("has_mercury");
    public static final BooleanProperty HAS_SULFUR  = BooleanProperty.of("has_sulfur");
    public static final BooleanProperty HAS_SALT    = BooleanProperty.of("has_salt");

    public DistillationCrucibleBlock(Settings settings) {
        super(settings);
        // 设置默认状态：三个属性都是 false
        setDefaultState(getStateManager().getDefaultState()
            .with(HAS_MERCURY, false)
            .with(HAS_SULFUR, false)
            .with(HAS_SALT, false));
    }

    // 注册属性
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_MERCURY, HAS_SULFUR, HAS_SALT);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, BlockHitResult hit) {

        // 客户端直接返回，逻辑只在服务端跑
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        ItemStack stack = player.getMainHandStack();

        // 空手：打印当前状态
        if (stack.isEmpty()) {
            AlchemyAetherCodex.LOGGER.info(
                "[AAC] 空手右键，当前状态：水银={}, 硫磺={}, 盐={}",
                state.get(HAS_MERCURY), state.get(HAS_SULFUR), state.get(HAS_SALT));
            return ActionResult.SUCCESS;
        }

        Item item = stack.getItem();
        BlockState newState = state;
        boolean consumed = false;

        if (item == ModItems.MERCURY_DROP) {
            if (state.get(HAS_MERCURY)) {
                AlchemyAetherCodex.LOGGER.info("[AAC] 坩埚里已经有水银了");
                return ActionResult.SUCCESS;
            }
            newState = state.with(HAS_MERCURY, true);
            consumed = true;
            AlchemyAetherCodex.LOGGER.info("[AAC] 投入了水银滴");

        } else if (item == ModItems.SULFUR_DUST) {
            if (state.get(HAS_SULFUR)) {
                AlchemyAetherCodex.LOGGER.info("[AAC] 坩埚里已经有硫磺了");
                return ActionResult.SUCCESS;
            }
            newState = state.with(HAS_SULFUR, true);
            consumed = true;
            AlchemyAetherCodex.LOGGER.info("[AAC] 投入了硫磺粉");

        } else if (item == ModItems.SALT_CRYSTAL) {
            if (state.get(HAS_SALT)) {
                AlchemyAetherCodex.LOGGER.info("[AAC] 坩埚里已经有盐了");
                return ActionResult.SUCCESS;
            }
            newState = state.with(HAS_SALT, true);
            consumed = true;
            AlchemyAetherCodex.LOGGER.info("[AAC] 投入了盐晶");

        } else {
            AlchemyAetherCodex.LOGGER.info("[AAC] 投入了无效物品：{}", item);
            return ActionResult.SUCCESS;
        }

        // 消耗一个（创造模式不消耗）
        if (consumed && !player.isCreative()) {
            stack.decrement(1);
        }

        // 检查三元素齐全
        if (newState.get(HAS_MERCURY) && newState.get(HAS_SULFUR) && newState.get(HAS_SALT)) {
            if(world instanceof ServerWorld serverworld) {
                triggherExplosion(serverworld, pos);
            }
            // 清空状态，允许重复使用
            newState = newState.with(HAS_MERCURY, false)
                               .with(HAS_SULFUR, false)
                               .with(HAS_SALT, false);
        }

        // 应用新状态
        world.setBlockState(pos, newState);
        return ActionResult.SUCCESS;
    }

    private void triggherExplosion(ServerWorld world,BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;
        
        //explosion
        world.spawnParticles(ParticleTypes.END_ROD,
            x, y, z, 20, 0.4, 0.2, 0.4, 0.05);
        world.spawnParticles(ParticleTypes.FLAME,
            x, y, z, 20, 0.4, 0.2, 0.4, 0.05);
        world.spawnParticles(ParticleTypes.CRIT,
            x, y, z, 20, 0.4, 0.2, 0.4, 0.05);

        //sound
        world.playSound(null, pos, 
            SoundEvents.ENTITY_GENERIC_EXPLODE.value(),SoundCategory.BLOCKS,0.6f,1.2f);
        world.playSound(null, pos,
            SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.8f, 1.0f);
        world.playSound(null, pos,
            SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 0.8f, 1.5f);
        //item spawn
        int count = 1 + world.random.nextInt(3);
        ItemStack gunpowder = new ItemStack(Items.GUNPOWDER,count);
        ItemEntity itementity = new ItemEntity(world,x,y,z,gunpowder);
        world.spawnEntity(itementity);
        AlchemyAetherCodex.LOGGER.info("[AAC] Boom!");

    }
}
