package me.unraveledmc.unraveledmcmod.httpd;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import me.unraveledmc.unraveledmcmod.UnraveledMCMod;
import me.unraveledmc.unraveledmcmod.httpd.module.HTTPDModule;
import me.unraveledmc.unraveledmcmod.util.FLog;
import net.pravian.aero.component.PluginComponent;
import org.bukkit.Bukkit;

public abstract class ModuleExecutable
{

    @Getter
    private final boolean async;

    public ModuleExecutable(boolean async)
    {
        this.async = async;
    }

    public NanoHTTPD.Response execute(final NanoHTTPD.HTTPSession session)
    {
        try
        {
            if (async)
            {
                return getResponse(session);
            }

            // Sync to server thread
            return Bukkit.getScheduler().callSyncMethod(UnraveledMCMod.plugin(), new Callable<NanoHTTPD.Response>()
            {
                @Override
                public NanoHTTPD.Response call() throws Exception
                {
                    return getResponse(session);
                }
            }).get();

        }
        catch (Exception ex)
        {
            FLog.severe(ex);
        }
        return null;
    }

    public abstract NanoHTTPD.Response getResponse(NanoHTTPD.HTTPSession session);

    public static ModuleExecutable forClass(final UnraveledMCMod plugin, Class<? extends HTTPDModule> clazz, boolean async)
    {
        final Constructor<? extends HTTPDModule> cons;
        try
        {
            cons = clazz.getConstructor(UnraveledMCMod.class, NanoHTTPD.HTTPSession.class);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Improperly defined module!");
        }

        return new ModuleExecutable(async)
        {
            @Override
            public NanoHTTPD.Response getResponse(NanoHTTPD.HTTPSession session)
            {
                try
                {
                    return cons.newInstance(plugin, session).getResponse();
                }
                catch (Exception ex)
                {
                    FLog.severe(ex);
                    return null;
                }
            }
        };
    }

}
