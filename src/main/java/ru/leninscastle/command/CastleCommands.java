package ru.leninscastle.command;

import com.mojang.brigadier.Command;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.leninscastle.LeninsCastle;
import ru.leninscastle.world.CastleBuilder;

@Mod.EventBusSubscriber(modid = LeninsCastle.MOD_ID)
public final class CastleCommands {
    private CastleCommands() {}

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("leninscastle")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("build")
                                .executes(context -> {
                                    var source = context.getSource();
                                    if (!(source.getLevel() instanceof ServerLevel level)) return 0;
                                    BlockPos playerPos = BlockPos.containing(source.getPosition());
                                    BlockPos origin = playerPos.offset(-17, 0, -17);
                                    CastleBuilder.build(level, origin, true);
                                    source.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                                            "Замок построен, Ленин появился в тронном зале!"), true);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("build_with_boss")
                                .executes(context -> {
                                    var source = context.getSource();
                                    if (!(source.getLevel() instanceof ServerLevel level)) return 0;
                                    BlockPos playerPos = BlockPos.containing(source.getPosition());
                                    BlockPos origin = playerPos.offset(-17, 0, -17);
                                    CastleBuilder.build(level, origin, true);
                                    source.sendSuccess(() -> net.minecraft.network.chat.Component.literal(
                                            "Замок построен, Ленин призван в тронный зал!"), true);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("summon_lenin")
                                .executes(context -> {
                                    var source = context.getSource();
                                    source.getLevel().getServer().getCommands().performPrefixedCommand(
                                            source, "summon leninscastle:lenin_boss ~ ~ ~");
                                    return Command.SINGLE_SUCCESS;
                                }))
        );
    }
}