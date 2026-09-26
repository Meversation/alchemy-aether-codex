package com.mever.alchemy_aether_codex.client;

import com.mever.alchemy_aether_codex.block.ModBlocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class AlchemyAetherCodexClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// 必需：炼药锅模型依赖的 cauldron_top 与 cauldron_bottom 纹理含大量 alpha=0 像素
		// (cauldron_top 中间 10x10、cauldron_bottom 中心区域)。这些像素必须在 cutout 层
		// 被丢弃;若用默认的 solid 层,它们会以不透明像素写入,渲染成黑色块。
		// vanilla CauldronBlock 同样使用 cutout 层。
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DISTILLATION_CRUCIBLE, RenderLayer.getCutout());
	}
}