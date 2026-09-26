package com.mever.alchemy_aether_codex.block;

import com.mever.alchemy_aether_codex.AlchemyAetherCodex;
import com.mever.alchemy_aether_codex.block.DistillationCrucibleBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block DISTILLATION_CRUCIBLE = register(
        "distillation_crucible",
        new DistillationCrucibleBlock(AbstractBlock.Settings.create()
            .strength(3.0f)
            .requiresTool()
            // 必需：炼药锅模型是中空的。若保持默认 opaque=true,
            // MC 会把本方块当作完整不透明方块,光照无法进入内部空腔,
            // 锅内壁/内底会渲染成全黑。vanilla CauldronBlock 同样是 nonOpaque。
            .nonOpaque()
            // 以下两项为可选体验优化,不影响渲染正确性:
            .sounds(BlockSoundGroup.METAL)
            .mapColor(MapColor.IRON_GRAY)
        )
        );

    private static Block register(String name,Block block){
        Identifier id = AlchemyAetherCodex.id(name);
        
        Registry.register(Registries.BLOCK ,id ,block);
        Registry.register(
            Registries.ITEM,
            id,
            new BlockItem(block,new Item.Settings())
        );
        return block;
    }

    public static void register() {

    }
}
