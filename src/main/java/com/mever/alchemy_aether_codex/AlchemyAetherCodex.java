package com.mever.alchemy_aether_codex;

import com.mever.alchemy_aether_codex.item.ModItems;
import com.mever.alchemy_aether_codex.block.ModBlocks;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlchemyAetherCodex implements ModInitializer {
	public static final String MOD_ID = "alchemy-aether-codex";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.register();
        ModBlocks.register();
		LOGGER.info("[AAC] 以太卷轴开始书写......");
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
