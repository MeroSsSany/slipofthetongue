package dev.merosssany.slipoftongue.network;

import dev.merosssany.slipoftongue.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() { return packetId++; }
    
    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Main.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        
        INSTANCE.messageBuilder(TextPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(TextPacket::new)
                .encoder(TextPacket::encode)
                .consumerMainThread(TextPacket::handle)
                .add();
        
        INSTANCE.messageBuilder(NarratorPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(NarratorPacket::new)
                .encoder(NarratorPacket::encode)
                .consumerMainThread(NarratorPacket::handle)
                .add();
        
        INSTANCE.messageBuilder(GrammarSettingsPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GrammarSettingsPacket::new)
                .encoder(GrammarSettingsPacket::encode)
                .consumerMainThread(GrammarSettingsPacket::handle)
                .add();
    }
    
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
    
    // Send to a specific player
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
    
    // Send to everyone (useful for global config updates)
    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
