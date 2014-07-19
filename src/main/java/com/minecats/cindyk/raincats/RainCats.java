package com.minecats.cindyk.raincats;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by cindy on 7/12/14.
 */

public class RainCats extends JavaPlugin implements CommandExecutor {


    static RainCats plugin;

    static final Random random = new Random();
    
    public RainCats() {
        super();
    }

    @Override
    public void onEnable() {
    	
        plugin = this;

        getServer().getPluginManager().registerEvents(new CatListener(),this);
        getCommand("raincats").setExecutor(this);

    }


    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        //if not a player... we are done.
        if (!(sender instanceof Player)) {

            if(args.length != 1 )
                return false;

            List<Player> list = Bukkit.matchPlayer(args[0]);
            Player p = list.isEmpty() ? null : list.get(0);
            
            if(p == null)
            {
                sender.sendMessage("You need to send a valid player name.");
                return true;
            }
            else
            {
                getLogger().info("Console issued command : Raining cats on " + p.getName());
                /*for(int x=0;x<30;x++)
                {   int adjust = x%4;
                    Raining rr = new Raining(getServer().getPlayer(args[0]),adjust);
                }*/
                rain(p.getLocation(), 64);

            }
        }
        else
        {
            getLogger().info("Raining cats on " + sender.getName());
            /*for(int x=0;x<30;x++)
            {   int adjust = x%4;
                Raining rr = new Raining(getServer().getPlayer(args[0]),adjust);
            }*/
            rain(((Player) sender).getLocation(), 64);

        }
        
        return true;
    }
    
    private final double a = 1, b = 0.2;
    
    private void rain(final Location l, final int iter){
    	
    	new BukkitRunnable() {

    		int i = 0;
    		
			@Override
			public void run() {

				if(i >= iter){
					this.cancel();
					return;
				}
				
				float x = i * 20 / iter;
				
				double dx = a * Math.exp(b * x) * MathHelper.cos(x);
	    		double dz = a * Math.exp(b * x) * MathHelper.sin(x);
	    		
	    		Location relative = l.clone().add(dx, 0, dz);
				
	    		final Ocelot ocelot = l.getWorld().spawn(relative, Ocelot.class);

	            RainCats.plugin.getLogger().info("cat id: " + ocelot.getUniqueId().toString());

	            ocelot.setCatType(Ocelot.Type.values()[random.nextInt(Ocelot.Type.values().length)]);
	            ocelot.setTamed(true);
	            ocelot.setBaby();
	    		ocelot.setSitting(random.nextBoolean());
	            
	    		new BukkitRunnable() {

					@Override
					public void run() {

						Location l = ocelot.getLocation();
						ocelot.remove();
						l.getWorld().spigot().playEffect(l.add(0, 0.3, 0), Effect.COLOURED_DUST, 0, 0, 0.5F, 0.5F, 0.5F, 1, 32, 64);
					}
	    			
	    		}.runTaskLater(RainCats.this, (long) (100 + 100 * Math.abs(random.nextGaussian())));
	            
				i++;
			}
    		
    	}.runTaskTimer(this, 0, 15L);
    }

}
