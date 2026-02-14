package dev.merosssany.slipoftongue.network;

import dev.merosssany.slipoftongue.CommandHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TextPacket {
    private final String message;
    
    // Constructor for creating the packet on the client
    public TextPacket(String message) {
        this.message = message;
    }
    
    // Decoder: Reads the bits from the network and turns them back into a String
    public TextPacket(FriendlyByteBuf buffer) {
        this.message = buffer.readUtf();
    }
    
    // Encoder: Turns the String into bits to send over the network
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.message);
    }
    
    // The Logic: This runs on the SERVER when the packet arrives
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                CommandHandler.checkString(message,player);
            }
        });
        context.setPacketHandled(true);
    }
}
