package dev.merosssany.slipoftongue.network;

import dev.merosssany.slipoftongue.CommandHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NarratorPacket {
    private final String message;
    
    // Constructor for creating the packet on the client
    public NarratorPacket(String message) {
        this.message = message;
    }
    
    // Decoder: Reads the bits from the network and turns them back into a String
    public NarratorPacket(FriendlyByteBuf buffer) {
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
            // This code runs on the Client Thread
            // Example: Update a local list or show a message
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                CommandHandler.narrate(message);
            });
        });
        context.setPacketHandled(true);
    }
}
