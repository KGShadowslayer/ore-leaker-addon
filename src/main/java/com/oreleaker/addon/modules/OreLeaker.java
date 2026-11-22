package com.oreleaker.addon.modules;

import com.oreleaker.addon.OreLeakerAddon;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Zeigt Erze als Boxen an (ESP). Funktioniert nur mit Bloecken, die der Server
 * wirklich an den Client schickt. Bei aktivem Anti-Xray braucht der Host dafuer
 * die Bypass-Berechtigung auf dem Server.
 */
public class OreLeaker extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("Welche Bloecke angezeigt werden.")
        .defaultValue(
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
            Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.ANCIENT_DEBRIS)
        .build());

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .description("Suchradius in Bloecken (hoeher = mehr Last).")
        .defaultValue(24).min(4).sliderRange(4, 48)
        .build());

    private final Setting<Integer> interval = sgGeneral.add(new IntSetting.Builder()
        .name("scan-interval")
        .description("Ticks zwischen zwei Scans.")
        .defaultValue(20).min(5).sliderRange(5, 100)
        .build());

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode").defaultValue(ShapeMode.Both).build());

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color").defaultValue(new SettingColor(0, 255, 255, 40)).build());

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color").defaultValue(new SettingColor(0, 255, 255, 255)).build());

    private final List<BlockPos> found = new ArrayList<>();
    private int timer;

    public OreLeaker() {
        super(OreLeakerAddon.CATEGORY, "ore-leaker", "Zeigt Erze in der Naehe als ESP an.");
    }

    @Override
    public void onActivate() {
        found.clear();
        timer = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.world == null || mc.player == null) return;
        if (timer-- > 0) return;
        timer = interval.get();

        List<BlockPos> result = new ArrayList<>();
        BlockPos center = mc.player.getBlockPos();
        int r = range.get();
        BlockPos.Mutable pos = new BlockPos.Mutable();

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    pos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    if (mc.world.isOutOfHeightLimit(pos)) continue;
                    if (blocks.get().contains(mc.world.getBlockState(pos).getBlock())) {
                        result.add(pos.toImmutable());
                    }
                }
            }
        }

        synchronized (found) {
            found.clear();
            found.addAll(result);
        }
    }

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        synchronized (found) {
            for (BlockPos p : found) {
                event.renderer.box(p, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
            }
        }
    }
}
