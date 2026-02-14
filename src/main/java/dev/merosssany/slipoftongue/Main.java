package dev.merosssany.slipoftongue;

import dev.merosssany.slipoftongue.network.GrammarSettingsPacket;
import dev.merosssany.slipoftongue.network.ModMessages;
import dev.merosssany.slipoftongue.network.TextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.infinitytwogames.vosklib.VoskManager;

@Mod(Main.MODID)
public class Main {
    public static final String MODID = "slipoftongue";
    
    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        modEventBus.addListener(this::commonSetup);
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
        
        try {
            Class.forName("org.infinitytwogames.vosklib.VoskManager");
        } catch (ClassNotFoundException e) {
            showError("VoskLib has failed to load. Slip of a Tongue will not load properly.");
        }
    }
    
    public static void showError(String msg) {
        ModLoader.get().addWarning(new ModLoadingWarning(ModLoadingContext.get().getActiveContainer().getModInfo(),
                ModLoadingStage.CONSTRUCT, msg
        ));
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
    }
    
    @Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
            VoskManager.startListening(s -> {}, s -> ModMessages.sendToServer(new TextPacket(s)));
        }
        
        @SubscribeEvent
        public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
            VoskManager.stopListening();
        }
    }
    
    @Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerModEvents {
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                if (Config.grammarMode.get())
                    ModMessages.sendToPlayer(new GrammarSettingsPacket(), player);
            }
        }
    }
}
