package dev.merosssany.slipoftongue;

import dev.merosssany.slipoftongue.network.GrammarSettingsPacket;
import dev.merosssany.slipoftongue.network.ModMessages;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    
    static final ForgeConfigSpec SERVER_SPEC;
    static final ForgeConfigSpec CLIENT_SPEC;
    
    static final ForgeConfigSpec.ConfigValue<List<? extends String>> commands;
    static final ForgeConfigSpec.BooleanValue forceNarrator;
    static final ForgeConfigSpec.BooleanValue grammarMode;
    
    private static final Pattern pattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    
    public static final Map<String, List<String>> words = new ConcurrentHashMap<>();
    public static final Map<String, String> narration = new ConcurrentHashMap<>();
    
    static {
        commands = SERVER_BUILDER
                .comment("List of banned words. \nFormat: word:[command1,command2]text to narrate")
                .defineList("words", List.of(), Config::validate);
        
        forceNarrator = CLIENT_BUILDER
                .comment("Whenever to enable narrator even if the Narrator is set of OFF.")
                .define("forceNarrator",false);
        
        grammarMode = SERVER_BUILDER
                .comment("Enables grammar (confined lists) mode for VOSK for better accuracy.\n\nNOTE: It sends the forbidden words to the client.")
                .define("grammarMode",true);
        
        SERVER_SPEC = SERVER_BUILDER.build();
        CLIENT_SPEC = CLIENT_BUILDER.build();
    }
    
    private static boolean validate(Object o) {
        if (o instanceof String entry) {
            if (entry.contains(":")) {
                String[] parts = entry.split(":");
                
                if (parts.length == 2) {
                    String list = parts[1];
                    
                    return list.contains("[") &&
                            list.contains("]")
                            ;
                }
            }
        }
        return false;
    }
    
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == Config.SERVER_SPEC) {
            words.clear(); // Important: Clear old values on config reload
            narration.clear();
            
            List<String> entries = (List<String>) commands.get();
            
            for (String entry : entries) {
                String[] entryParts = entry.split(":", 2);
                if (entryParts.length < 2) continue;
                
                String word = entryParts[0].toLowerCase();
                String rawList = entryParts[1];
                String bracket = extractMainBlock(rawList);
                List<String> cmdList = getCmdList(bracket);
                words.put(word, cmdList);
                
                String narratorText = rawList.replace(bracket, "");
                narration.put(word, narratorText);
            }
            
            if (grammarMode.get()) {
                ModMessages.sendToAllPlayers(new GrammarSettingsPacket());
            }
        }
    }
    
    private static @NotNull List<String> getCmdList(String bracket) {
        if (bracket.length() < 2) return new ArrayList<>(); // Safety check
        
        String inside = bracket.substring(1, bracket.length() - 1);
        String[] splitCommands = pattern.split(inside);
        
        List<String> cmdList = new ArrayList<>();
        for (String cmd : splitCommands) {
            String trimmed = cmd.trim();
            if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
                trimmed = trimmed.substring(1, trimmed.length() - 1);
            }
            if (!trimmed.isEmpty()) {
                cmdList.add(trimmed);
            }
        }
        return cmdList;
    }
    
    public static String extractMainBlock(String input) {
        // Pattern to capture from the first [ to the last ]
        Pattern greedyBrackets = Pattern.compile("(\\[.*])");
        Matcher matcher = greedyBrackets.matcher(input);
        
        if (matcher.find()) {
            return matcher.group(1); // This returns the brackets + everything inside
        }
        return "";
    }
}
