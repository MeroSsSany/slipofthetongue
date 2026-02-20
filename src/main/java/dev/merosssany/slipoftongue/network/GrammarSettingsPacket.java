package dev.merosssany.slipoftongue.network;

import dev.merosssany.slipoftongue.Config;
import dev.merosssany.slipoftongue.Main;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.infinitytwogames.vosklib.VoskManager;

import java.util.function.Supplier;

public class GrammarSettingsPacket {
    private final String words;
    
    // Constructor for creating the packet on the client
    public GrammarSettingsPacket() {
        StringBuilder builder = new StringBuilder();
        for (String word : Config.words.keySet()) {
            String cleanWord = word.toLowerCase().replaceAll("[,\"']", "").trim();
            if (!cleanWord.isEmpty()) {
                builder.append(cleanWord).append(",");
            }
        }
        words = builder.toString();
    }
    
    // Decoder: Reads the bits from the network and turns them back into a String
    public GrammarSettingsPacket(FriendlyByteBuf buffer) {
        this.words = buffer.readUtf();
    }
    
    // Encoder: Turns the String into bits to send over the network
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.words);
    }
    
    // The Logic: This runs on the SERVER when the packet arrives
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            VoskManager.stopListening();
            if (!words.trim().isEmpty()) VoskManager.createRecognition(words.split(","));
            else VoskManager.createRecognition();
            Main.ClientModEvents.onPlayerLoggedIn(null);
        }));
        context.setPacketHandled(true);
    }
}
