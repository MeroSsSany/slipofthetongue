package dev.merosssany.slipoftongue;

import com.mojang.text2speech.Narrator;
import dev.merosssany.slipoftongue.network.ModMessages;
import dev.merosssany.slipoftongue.network.NarratorPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;

public class CommandHandler {
    public static void checkString(String input, ServerPlayer player) {
        String lowerInput = input.toLowerCase();
        
        for (Map.Entry<String, List<String>> entry : Config.words.entrySet()) {
            String bannedWord = entry.getKey().toLowerCase();
            
            if (lowerInput.contains(bannedWord.toLowerCase())) {
                executeCommands(player, entry.getValue());
                ModMessages.sendToPlayer(new NarratorPacket(Config.narration.getOrDefault(bannedWord, "")), player);
            }
        }
    }
    
    private static void executeCommands(ServerPlayer player, List<String> cmdList) {
        MinecraftServer server = player.getServer();
        if (server == null) return;
        
        for (String cmd : cmdList) {
            // Replace placeholders if necessary, e.g., /tp %player% 0 0 0
            String finalCmd = cmd.replace("%player%", player.getGameProfile().getName());
            
            // Execute on the server thread
            server.getCommands().performPrefixedCommand(
                    player.createCommandSourceStack().withSuppressedOutput(),
                    finalCmd
            );
        }
    }
    
    public static void narrate(String message) {
        boolean narration = Config.forceNarrator.get();
        if (narration) {
            Narrator narrator = Narrator.getNarrator();
            narrator.say(message, true);
            
        } else {
            Minecraft.getInstance().getNarrator().sayChat(Component.literal(message));
        }
        
        Minecraft.getInstance().player.displayClientMessage(Component.literal(message), true);
    }
}
