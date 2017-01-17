package me.Ckay.pvp;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Map;
import java.util.Random;
//import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Ckay.pvp.SettingsManager;
import me.Ckay.pvp.Main;

public class Main extends JavaPlugin implements Listener {
	
	//private Map<Integer, List<Player>> inArena;
	private List<Player> inArena2;
	private List <Player> zone1;
	private List <Player> zone2;
	private List <Player> zone3;
	private List <Player> zone4;
	private List <Player> zone5;
	private List <Player> zone6;
	private List <Player> zone7;
	private List <Player> zone8;
	
	
	Boolean allowAll = false;
	boolean god = true;
	boolean lobby = true;
	boolean gameStarted = false;
	boolean delay1done = false;
	boolean delay2done = false;
	boolean delay3done = false;
	boolean delay4done = false;
	boolean delay5done = false;
	boolean delay6done = false;
	boolean delay7done = false;
	boolean delay8done = false;
	
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Main plugin;
	
	private Map<String, Integer> cooldownTime;
	private Map<String, Integer> gracePeriod;
	private Map<String, Integer> gracePeriod2;
	private Map<String, Integer> delayPeriod;
	private List<Player> banList;
	private Map<String, BukkitRunnable> graceTask;
	private Map<String, BukkitRunnable> borderTask;
	private Map<String, BukkitRunnable> nightTask;
	private Map<String, BukkitRunnable> delayTask;
	
	//private static Main instance;
	
	SettingsManager settings = SettingsManager.getInstance();

	public void onEnable()
	  {
		getServer().getPluginManager().registerEvents(this, this);
		
		settings.setup(this);
		settings.setupBanned(this);
		
		World arena = Bukkit.getWorld("World");
        arena.setAutoSave(false);
		
		nightTask = new HashMap<String, BukkitRunnable>();
		graceTask = new HashMap<String, BukkitRunnable>();
		borderTask = new HashMap<String, BukkitRunnable>();
		delayTask = new HashMap<String, BukkitRunnable>();
		
		//inArena = new HashMap<Integer, List<Player>>();
		inArena2 = new ArrayList<Player>();
		zone1 = new ArrayList<Player> ();
		zone2 = new ArrayList<Player> ();
		zone3 = new ArrayList<Player> ();
		zone4 = new ArrayList<Player> ();
		zone5 = new ArrayList<Player> ();
		zone6 = new ArrayList<Player> ();
		zone7 = new ArrayList<Player> ();
		zone8 = new ArrayList<Player> ();
		banList = new ArrayList<Player>();
		cooldownTime = new HashMap <String, Integer>();
		gracePeriod = new HashMap <String, Integer>();
		gracePeriod2 = new HashMap <String, Integer>();
		delayPeriod = new HashMap <String, Integer>();
		
		for (Player playersOnline : Bukkit.getOnlinePlayers()) {
	        for(Entity en : playersOnline.getWorld().getEntities()){
	            if(!(en instanceof Player)) {

	            en.remove();
	            }
	      }
		}
		
		//instance = this; 
		
	  }
	
	public void onDisable() {
		
	for (Player playersOnline : Bukkit.getOnlinePlayers()) {
        for(Entity en : playersOnline.getWorld().getEntities()){
            if(!(en instanceof Player)) {

            en.remove();
            }
      }
	}
		
		File folder = getServer().getWorld("World").getWorldFolder();

		deleteDirectory(folder);

		final File templateFolder = new File(getServer().getWorldContainer(), "WorldTemplate");
		final File worldOneFolder = new File(getServer().getWorldContainer(), "World");

		copyDir(templateFolder, worldOneFolder);
		
		System.out.println("Directories (Worlds Copied)");
		
	}
	
//	public void onDeath(PlayerDeathEvent e) {
//		
//		Player p = e.getEntity();
//		
//		if (inArena2.contains(p)) {
//			System.out.println(p + " is in the arena, removing him");
//			System.out.println(inArena2.size());
//			inArena2.remove(p);
//			System.out.println("Removed player from arena.");
//			System.out.println(inArena2.size());
//			
//			
//			
//		}
//		
//	}
	
	
	@EventHandler
	  public void FixIt(FoodLevelChangeEvent e)
	  {
	    if ((e.getFoodLevel() < ((Player)e.getEntity()).getFoodLevel()) && 
	      (new Random().nextInt(100) > 4)) {
	      e.setCancelled(true);
	    }
	  }
	
	@EventHandler
	public void VehicleDestroy(VehicleDestroyEvent e) {
		if (e.getVehicle() instanceof Boat) {
			e.setCancelled(true);
		}
	}
	
	public void spawnBoat(Location l, Player p){
		 Entity e = l.getWorld().spawnEntity(l, EntityType.BOAT);
		 e.setPassenger(p);
		}
	

	private static void copyDir(File source, File target) {
		try {
			ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
			if (!ignore.contains(source.getName())) {
				if (source.isDirectory()) {
					if (!target.exists())
						target.mkdirs();
					String files[] = source.list();
					for (String file : files) {
						File srcFile = new File(source, file);
						File destFile = new File(target, file);
						copyDir(srcFile, destFile);
					}
				} else {
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0)
						out.write(buffer, 0, length);
					in.close();
					out.close();
				}
			}
		} catch (IOException e) {

		}
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File files[] = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				} // end else
			}
		}
		return (path.delete());
	}


	
	 public Player getNearest(Player p, Double range) {
		 double distance = Double.POSITIVE_INFINITY; // To make sure the first
		 // player checked is closest
		 Player target = null;
		 	for (Entity e : p.getNearbyEntities(range, range, range)) {
		 		if (!(e instanceof Player))
		 			continue;
		 		if(e == p) continue; //Added this check so you don't target yourself.
		 			double distanceto = p.getLocation().distance(e.getLocation());
		 		if (distanceto > distance)
		 			continue;
		 			distance = distanceto;
		 			target = (Player) e;
		 }
		 return target;
		 }
	 
	 
	 Random rand = new Random();
	 int generateInt(int min, int max)
	 {
	     int randInt = rand.nextInt(min - max) + 1 + min;
	     return randInt;
	 }
	 
	 public void tpDelay(final Player p, final String zone) {
		 
		 System.out.println("Got to tpDelay");
		 
		 final Random random = new Random();
		 
		 delayPeriod.put("Delay", 1);
		  
		  delayTask.put("delay", new BukkitRunnable() {
			  
			    public void run() {
			    	
			    	System.out.println("Got to Run");
			    	
			    	if (delayPeriod.get("Delay") != null) {
			    		
			    		System.out.println("Got to null check");
						   
			    		delayPeriod.put("Delay", delayPeriod.get("Delay") - 1);
			    		System.out.println("zone Delay: " + delayPeriod.get("Delay"));
			    		
			    		if (delayPeriod.get("Delay") == 0) {
			    			
			    			//delay2done = true;
			    			System.out.println("zone Delay Done");
			    			

								 double x1 = settings.getData().getDouble("warps."+zone+".x");
								 double z1 = settings.getData().getDouble("warps."+zone+".z");
								 
								 if (zone == "zone2") {
									 System.out.println("TP Player in Zone2");
									 int numberBetweenX = random.nextInt(40) +1;
									 int numberBetweenZ = random.nextInt(12) +1;
									 //int numberBetweenY = random.nextInt(81) + 61;
									 int X = 82;
								 
									 int x12 = (int) x1;
									 int z12 = (int) z1;
								 
									 int RanX = x12 + numberBetweenX;
									 int RanZ = z12 + numberBetweenZ;
								 
									 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
									 p.teleport(teleportLocation1);
								 }
								 if (zone == "zone1") {
									 System.out.println("TP player in zone1");
									 int numberBetweenX = random.nextInt(40) +1;
									 int numberBetweenZ = random.nextInt(5) +1;
									 //int numberBetweenY = random.nextInt(81) + 61;
									 int X = 81;
									 
									 int x12 = (int) x1;
									 int z12 = (int) z1;
									 
									 int RanX = x12 + numberBetweenX;
									 int RanZ = z12 + numberBetweenZ;
									 
									 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
									 p.teleport(teleportLocation1);
								 }
								 if (zone == "zone3") {
									 int numberBetweenX = random.nextInt(20) +1;
									 int numberBetweenZ = random.nextInt(5) +1;
									 //int numberBetweenY = random.nextInt(81) + 61;
									 int X = 73;
									 
									 int x12 = (int) x1;
									 int z12 = (int) z1;
									 
									 int RanX = x12 + numberBetweenX;
									 int RanZ = z12 + numberBetweenZ;
									 
									 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
									 p.teleport(teleportLocation1);
								 }
								 if (zone == "zone4") {
									 int numberBetweenX = random.nextInt(40) +1;
									 int numberBetweenZ = random.nextInt(5) +1;
									 //int numberBetweenY = random.nextInt(81) + 61;
									 int X = 81;
									 
									 int x12 = (int) x1;
									 int z12 = (int) z1;
									 
									 int RanX = x12 + numberBetweenX;
									 int RanZ = z12 + numberBetweenZ;
									 
									 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
									 p.teleport(teleportLocation1);
								 }
								 if (zone == "zone5") {
									 int numberBetweenX = random.nextInt(40) +1;
									 int numberBetweenZ = random.nextInt(15) +1;
									 //int numberBetweenY = random.nextInt(81) + 61;
									 int X = 78;
									 
									 int x12 = (int) x1;
									 int z12 = (int) z1;
									 
									 int RanX = x12 + numberBetweenX;
									 int RanZ = z12 + numberBetweenZ;
									 
									 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
									 p.teleport(teleportLocation1);
								 }
								 if (zone == "zone6") {
									 int numberBetweenX = random.nextInt(40) +1;
									 int numberBetweenZ = random.nextInt(10) +1;
									 //int numberBetweenY = random.nextInt(81) + 61;
									 int X = 77;
									 
									 int x12 = (int) x1;
									 int z12 = (int) z1;
									 
									 int RanX = x12 + numberBetweenX;
									 int RanZ = z12 + numberBetweenZ;
									 
									 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
									 p.teleport(teleportLocation1);
								 }
								 if (zone == "zone7") {
									 System.out.println("TP'ing Player in zone7");
									 System.out.println(p.getName());
									 int numberBetweenX = random.nextInt(40) +1;
									 int numberBetweenZ = random.nextInt(10) +1;
									 //int numberBetweenY = random.nextInt(81) + 61;
									 int X = 86;
									 
									 int x12 = (int) x1;
									 int z12 = (int) z1;
									 
									 int RanX = x12 + numberBetweenX;
									 int RanZ = z12 + numberBetweenZ;
									 
									 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
									 p.teleport(teleportLocation1);
								 }
								 if (zone == "zone8") {
									 System.out.println("TP'ing Player in zone8");
									 System.out.println(p.getName());
									 int numberBetweenX = random.nextInt(25) +1;
									 int numberBetweenZ = random.nextInt(5) +1;
									 //int numberBetweenY = random.nextInt(81) + 61;
									 int X = 73;
									 
									 int x12 = (int) x1;
									 int z12 = (int) z1;
									 
									 int RanX = x12 + numberBetweenX;
									 int RanZ = z12 + numberBetweenZ;
									 
									 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
									 p.teleport(teleportLocation1);
								 }

			    			delayTask.remove("delay");
			    			delayPeriod.put("Delay", 1);
			    			delayTask.clear();
			    			cancel();
			    			
			    			}
			    		
			    		}
			    	else {
			    		System.out.println("Delay is null");
			    	}
			    }

			});
		  delayTask.get("delay").runTaskTimer(this, 1, 2);
		 
	 }
	 
	 
	 @SuppressWarnings("deprecation")
	public static DyeColor randomizeColor() {
	        Random random = new Random();
	        int color = random.nextInt(16);
	 
	        for (int i = 0; i < 16; i++) {
	            if (i == color)
	                return DyeColor.getByData((byte) i);
	            continue;
	        }
	        return null;
	    }
	 
	 public void spawnWolf(Player p) {
	        Wolf wolf = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
	 
	        // Just to make sure it's a normal wolf.
	        wolf.setAdult();
	        wolf.setTamed(true);
	        wolf.setOwner(p);
	 
	        // We don't want extra wolves.
	        wolf.setBreed(false);
	 
	        // Clarify the owner.
	        
	        
	        //green red blue yellow
	        
	        if (zone1.contains(p)) {
	        	wolf.setCustomName(ChatColor.GREEN + p.getName() + "'s Wolf");
	        }
	        else if (zone2.contains(p)) {
	        	wolf.setCustomName(ChatColor.RED + p.getName() + "'s Wolf");
	        }
	        else if (zone3.contains(p)) {
	        	wolf.setCustomName(ChatColor.BLUE + p.getName() + "'s Wolf");
	        }
	        else if (zone4.contains(p)) {
	        	wolf.setCustomName(ChatColor.YELLOW + p.getName() + "'s Wolf");
	        }
	        
	        
	        //wolf.setCustomNameVisible(true);
	 
	        // Let's have a little bit of variation
	        wolf.setCollarColor(randomizeColor());
	 
	        // Misc.
	        wolf.setHealth(wolf.getMaxHealth());
	        wolf.setCanPickupItems(false);
	    }
	 
	@EventHandler
	public void stopLeaveBoat(VehicleExitEvent e) {
		
		
		if (e.getExited() instanceof Boat) {
			
			e.setCancelled(true);
			
		}
		
		if (e.getVehicle().equals(EntityType.BOAT)) {
			e.setCancelled(true);
		}
		
		e.setCancelled(true);
		
	}
	 
	 @EventHandler
	    public void onCompassTracker(PlayerInteractEvent e){
	       final Player p = e.getPlayer();
	       
	       
//	         ItemStack bone = new ItemStack(Material.BONE, 1);
//			 ItemMeta itemMeta3 = bone.getItemMeta();
//			 itemMeta3.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Spawn Pet Wolf"));
//			 itemMeta3.setLore(Arrays.asList(ChatColor.GREEN + "Right click to spawn a pet wolf!"));
//			 bone.setItemMeta(itemMeta3);
	       
			 
	       if((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && p.getItemInHand().getType() == Material.ENDER_PEARL){

	    	// Remove the pearl.
		        //p.getInventory().removeItem(new ItemStack[] {new ItemStack(Material.ENDER_PEARL, 1)});
	    	   if(p.getItemInHand().getAmount() > 1) {
	    		   p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
	    		 } else {
	    		   p.getItemInHand().setAmount(0); 
	    		 }
			        	e.setCancelled(true);

			        
			 }
	       
	       
			 
	        
	     // Your method stuff
	        final Player target = getNearest(p, 50.0);
	        //Then you need to make sure there was a player
	        
	     if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && p.getItemInHand().getType() == Material.COMPASS){
	        if(target == null) {
	          //Uh-oh, there was not a player in that range
	        	p.sendMessage(ChatColor.RED + "There are no players within a 50 block radius!");
	        	ItemMeta itemMeta3 = p.getInventory().getItemInHand().getItemMeta();
	        	
	        	itemMeta3.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Track Player's"));
	        	p.getInventory().getItemInHand().setItemMeta(itemMeta3);
	        }
	        else {
	          //There was a player so set the compass
	        	
		             p.setCompassTarget(target.getLocation());
		            
		             //ItemStack compass = new ItemStack(Material.COMPASS, 1);
					 ItemMeta itemMeta2 = p.getInventory().getItemInHand().getItemMeta();
					 //final ItemMeta itemMeta3 = p.getInventory().getItemInHand().getItemMeta();
					 
					
					 itemMeta2.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + " " + target.getName()));
					 
					 p.getInventory().getItemInHand().setItemMeta(itemMeta2);
					 
					 //itemMeta3.getDisplayName().replace("CraftPlayer{name=", "").replace("}", "");
					 
					 //p.getInventory().getItemInHand().setItemMeta(itemMeta3);
					 
					 Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
						    public void run() {
						    	 p.setCompassTarget(target.getLocation());
						    	 //itemMeta2.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + " " + target.getName()));
						    	 //p.getInventory().getItemInHand().setItemMeta(itemMeta2);
						    	 //itemMeta3.getDisplayName().replace("CraftPlayer{name=", "").replace("}", "");
						    	 
						    }
						}, 1, 1);

	        }
	        
	        
	        }
	       
	    }
	 
	 
	@EventHandler
	public void onCancelFallDamage(EntityDamageEvent e) {
	    
		
		if(e.getEntity() instanceof Player) { //Checks to see if the entity that is taking damage is a player
	    	
			//Player p = (Player) e.getEntity();
			
	    if (god == true) {

	        //if(e.getCause() == DamageCause.FALL) { //if the cause of damage is fall damage
	            e.setCancelled(true); //you cancel the event.
	            
	    	
	          }
	    
	    
	        	//}
	    	}
	    }
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin (PlayerJoinEvent e) {
		
		final Player p = e.getPlayer();

		if (!(p.getName().equalsIgnoreCase("ABkayCkay") || p.getName().equalsIgnoreCase("PlayPocketPixels") || p.getName().equalsIgnoreCase("MikeSN") || p.getName().equalsIgnoreCase("killzyazmadness") || p.getName().equalsIgnoreCase("Vegeta") || p.getName().equalsIgnoreCase("Hacko_Jacko") || p.getName().equalsIgnoreCase("PokeKhan") || p.getName().equalsIgnoreCase("lod77") || p.getName().equalsIgnoreCase("Pagub"))) {

			if (lobby = true) {
			//MikeSN
			//killzyazmadness
			//Hacko_Jacko
			//Pagub
			//Vegeta
			//p.getName().equalsIgnoreCase("ABkayCkay") 
			
			inArena2.add(p);
			
			if (zone1.size() >= zone2.size()) {

				if (zone2.size() >= zone3.size()) {
					
					if (zone3.size() >= zone4.size()) {
						
						if (zone4.size() >= zone5.size()) {
							
							if (zone5.size() >= zone6.size()) {
								
								if (zone6.size() >= zone7.size()) {
									
									if (zone7.size() >= zone8.size()) {
										zone8.add(p);
										System.out.println(p.getName() + " joined zone8");
										p.sendMessage(ChatColor.LIGHT_PURPLE + "You have joined Zone8! Welcome to the Battle Of the Pirates!");
									}
									else {
										zone7.add(p);
										System.out.println(p.getName() + " joined zone7");
										p.sendMessage(ChatColor.GOLD + "You have joined Zone7! Welcome to the Battle Of the Pirates!");
									}
								}
								else {
									zone6.add(p);
									System.out.println(p.getName() + " joined zone6");
									p.sendMessage(ChatColor.GRAY + "You have joined Zone6! Welcome to the Battle Of the Pirates!");
								}
							}
							else {
								zone5.add(p);
								System.out.println(p.getName() + " joined zone5");
								p.sendMessage(ChatColor.AQUA + "You have joined Zone5! Welcome to the Battle Of the Pirates!");
							}
						}
						else {
						zone4.add(p);
						System.out.println(p.getName() + " joined zone4");
						p.sendMessage(ChatColor.YELLOW + "You have joined Zone4! Welcome to the Battle Of the Pirates!");
						}
					}
					else {
						zone3.add(p);
						System.out.println(p.getName() + " joined zone3");
						p.sendMessage(ChatColor.BLUE + "You have joined Zone3! Welcome to the Battle Of the Pirates!");
					}
				}
				else {
					zone2.add(p);
					System.out.println(p.getName() + " joined zone2");
					p.sendMessage(ChatColor.RED + "You have joined Zone2! Welcome to the Battle Of the Pirates!");
				}
			}
			else {
				zone1.add(p);
				System.out.println(p.getName() + " joined zone1");
				p.sendMessage(ChatColor.GREEN + "You have joined Zone1! Welcome to the Battle Of the Pirates!");
			}
			
			p.setFoodLevel(20);
			p.setHealth(20);
			
			p.setGameMode(GameMode.SURVIVAL);
			
			p.getInventory().clear();
            p.getInventory().setArmorContents(null);
		
			p.sendMessage(ChatColor.GREEN + "You have sucesfully joined the arena, teleporting to waiting area.");
			
			
		 if (inArena2.size() == 25) {
			 Bukkit.broadcastMessage(ChatColor.GOLD + "There are now " + inArena2.size() + " players in the arena waiting for the game to start.");
		 }
		 else if (inArena2.size() == 50) {
			 Bukkit.broadcastMessage(ChatColor.GOLD + "There are now " + inArena2.size() + " players in the arena waiting for the game to start.");
		 }
		 else if (inArena2.size() == 75) {
			 Bukkit.broadcastMessage(ChatColor.GOLD + "There are now " + inArena2.size() + " players in the arena waiting for the game to start.");
		 }
		 else if (inArena2.size() == 100) {
			 Bukkit.broadcastMessage(ChatColor.GOLD + "There are now " + inArena2.size() + " players in the arena waiting for the game to start.");
		 }
		 else if (inArena2.size() == 125) {
			 Bukkit.broadcastMessage(ChatColor.GOLD + "There are now " + inArena2.size() + " players in the arena waiting for the game to start.");
		 }
		 
		 for (Player staff : Bukkit.getOnlinePlayers()) {
             
             if (staff.hasPermission("arena.admin")) {
           	  staff.sendMessage(ChatColor.BLUE + "A challenger has joined the arena (" + p.getName() + "), there are now " + inArena2.size() + " People in the arena.");
                 
               }
		  }
		 		
		}
			else {
				p.setGameMode(GameMode.SPECTATOR);
				World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.spectator.world"));
		        double x = settings.getData().getDouble("warps.spectator.x");
		        double y = settings.getData().getDouble("warps.spectator.y");
		        double z = settings.getData().getDouble("warps.spectator.z");
		        p.teleport(new Location(w, x, y, z));
		        
			}

		}
		
		if (lobby = true) {
			System.out.println("Teleporting to waitarea");
			
			World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
			double x = settings.getData().getDouble("warps.waitarea.x");
			double y = settings.getData().getDouble("warps.waitarea.y");
			double z = settings.getData().getDouble("warps.waitarea.z");
			p.teleport(new Location(w, x, y, z));
        
			Location loc = new Location(w, x, y, z);
        
			p.setBedSpawnLocation(loc, true);
        
			System.out.println(loc);
			System.out.println("Teleported to " + loc);
		
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
	Player p = e.getPlayer();
	if (inArena2.contains(p)) {
		if (p.getLocation().getBlock().getType() == Material.WATER
				|| (p.getLocation().getBlock().getType() == Material.STATIONARY_WATER) && p.getHealth() > 0) {
			
			if (gameStarted = false) {
				
				 Random random = new Random();
				
				 if (zone2.contains(p)) {
				 
					 double x1 = settings.getData().getDouble("warps.zone2.x");
					 double z1 = settings.getData().getDouble("warps.zone2.z");
					 
					 int numberBetweenX = random.nextInt(40) +1;
					 int numberBetweenZ = random.nextInt(12) +1;
					 //int numberBetweenY = random.nextInt(81) + 61;
					 int X = 82;
					 
					 int x12 = (int) x1;
					 int z12 = (int) z1;
					 
					 int RanX = x12 + numberBetweenX;
					 int RanZ = z12 + numberBetweenZ;
					 
					 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
					 p.teleport(teleportLocation1);
					 
				 }
				 else if (zone1.contains(p)) {
					 
					 double x1 = settings.getData().getDouble("warps.zone1.x");
					 double z1 = settings.getData().getDouble("warps.zone1.z");
					 
					 int numberBetweenX = random.nextInt(40) +1;
					 int numberBetweenZ = random.nextInt(5) +1;
					 //int numberBetweenY = random.nextInt(81) + 61;
					 int X = 81;
					 
					 int x12 = (int) x1;
					 int z12 = (int) z1;
					 
					 int RanX = x12 + numberBetweenX;
					 int RanZ = z12 + numberBetweenZ;
					 
					 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
					 p.teleport(teleportLocation1);
				 }
				 else if (zone3.contains(p)) {
					 
					 double x1 = settings.getData().getDouble("warps.zone3.x");
					 double z1 = settings.getData().getDouble("warps.zone3.z");
					 
					 int numberBetweenX = random.nextInt(20) +1;
					 int numberBetweenZ = random.nextInt(5) +1;
					 //int numberBetweenY = random.nextInt(81) + 61;
					 int X = 73;
					 
					 int x12 = (int) x1;
					 int z12 = (int) z1;
					 
					 int RanX = x12 + numberBetweenX;
					 int RanZ = z12 + numberBetweenZ;
					 
					 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
					 p.teleport(teleportLocation1);
					 
				 }
				 else if (zone4.contains(p)) {
					 
					 double x1 = settings.getData().getDouble("warps.zone4.x");
					 double z1 = settings.getData().getDouble("warps.zone4.z");
					 
					 int numberBetweenX = random.nextInt(40) +1;
					 int numberBetweenZ = random.nextInt(5) +1;
					 //int numberBetweenY = random.nextInt(81) + 61;
					 int X = 81;
					 
					 int x12 = (int) x1;
					 int z12 = (int) z1;
					 
					 int RanX = x12 + numberBetweenX;
					 int RanZ = z12 + numberBetweenZ;
					 
					 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
					 p.teleport(teleportLocation1);

				 }
				 else if (zone5.contains(p)) {
					 
					 double x1 = settings.getData().getDouble("warps.zone5.x");
					 double z1 = settings.getData().getDouble("warps.zone5.z");
					 
					 int numberBetweenX = random.nextInt(40) +1;
					 int numberBetweenZ = random.nextInt(15) +1;
					 //int numberBetweenY = random.nextInt(81) + 61;
					 int X = 78;
					 
					 int x12 = (int) x1;
					 int z12 = (int) z1;
					 
					 int RanX = x12 + numberBetweenX;
					 int RanZ = z12 + numberBetweenZ;
					 
					 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
					 p.teleport(teleportLocation1);

				 }
				 else if (zone6.contains(p)) {
					 
					 double x1 = settings.getData().getDouble("warps.zone6.x");
					 double z1 = settings.getData().getDouble("warps.zone6.z");
					 
					 int numberBetweenX = random.nextInt(40) +1;
					 int numberBetweenZ = random.nextInt(10) +1;
					 //int numberBetweenY = random.nextInt(81) + 61;
					 int X = 77;
					 
					 int x12 = (int) x1;
					 int z12 = (int) z1;
					 
					 int RanX = x12 + numberBetweenX;
					 int RanZ = z12 + numberBetweenZ;
					 
					 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
					 p.teleport(teleportLocation1);

				 }
				 else if (zone7.contains(p)) {
					 
					 double x1 = settings.getData().getDouble("warps.zone7.x");
					 double z1 = settings.getData().getDouble("warps.zone7.z");
					 
					 int numberBetweenX = random.nextInt(40) +1;
					 int numberBetweenZ = random.nextInt(10) +1;
					 //int numberBetweenY = random.nextInt(81) + 61;
					 int X = 86;
					 
					 int x12 = (int) x1;
					 int z12 = (int) z1;
					 
					 int RanX = x12 + numberBetweenX;
					 int RanZ = z12 + numberBetweenZ;
					 
					 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
					 p.teleport(teleportLocation1);

				 }
				 
				 else if (zone8.contains(p)) {
					 
					 double x1 = settings.getData().getDouble("warps.zone8.x");
					 double z1 = settings.getData().getDouble("warps.zone8.z");
					 
					 int numberBetweenX = random.nextInt(30) +1;
					 int numberBetweenZ = random.nextInt(10) +1;
					 //int numberBetweenY = random.nextInt(81) + 61;
					 int X = 73;
					 
					 int x12 = (int) x1;
					 int z12 = (int) z1;
					 
					 int RanX = x12 + numberBetweenX;
					 int RanZ = z12 + numberBetweenZ;
					 
					 Location teleportLocation1 = new Location(p.getWorld(), RanX, X, RanZ);
					 p.teleport(teleportLocation1);

				 }
		
			}
					p.setHealth(0);
					inArena2.remove(p);
					
					if (!(lobby == true)) {
						
						for (Player players : Bukkit.getOnlinePlayers()) {
							
							players.playSound(players.getLocation(), Sound.FIREWORK_BLAST, 2F, 1F);
						}
						
						Bukkit.broadcastMessage(ChatColor.GOLD + p.getName() + " has fallen in water and been eliminated! There are " + inArena2.size() + " Players left.");
						
						if (inArena2.size() == 1) {
							
							Player playerWinner = inArena2.get(0).getPlayer();
							
							cooldownTime.remove("Countdown");
							Bukkit.broadcastMessage(ChatColor.GOLD + "We have our winner! " + inArena2.get(0).getPlayer().getDisplayName());
							
							World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.win.world"));
			                double x = settings.getData().getDouble("warps.win.x");
			                double y = settings.getData().getDouble("warps.win.y");
			                double z = settings.getData().getDouble("warps.win.z");
			                playerWinner.teleport(new Location(w, x, y, z));
			                playerWinner.sendMessage(ChatColor.GREEN + "Teleported to winning area!");
			                
			                god = true;
			                
			               for (Player players : Bukkit.getOnlinePlayers()) {
			                if (players != p.getPlayer()) {
			                	World ww = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
						        double xx = settings.getData().getDouble("warps.waitarea.x");
						        double yy = settings.getData().getDouble("warps.waitarea.y");
						        double zz = settings.getData().getDouble("warps.waitarea.z");
						        players.teleport(new Location(ww, xx, yy, zz));
			        			players.playSound(players.getLocation(), Sound.FIREWORK_BLAST, 2F, 1F);
			                	}
			                }
							
						}

						
						World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
				        double x = settings.getData().getDouble("warps.waitarea.x");
				        double y = settings.getData().getDouble("warps.waitarea.y");
				        double z = settings.getData().getDouble("warps.waitarea.z");
				        p.teleport(new Location(w, x, y, z));
						}
			}
		
		}
	}
	
	@EventHandler
	public void onLeave (PlayerQuitEvent e) {
		
		Player p = e.getPlayer();
		
		if (inArena2.contains(p)) {
			inArena2.remove(p);
			
			if (zone1.contains(p)) {
				zone1.remove(p);
				System.out.println(p + " removed from zone1");
			}
			else if (zone2.contains(p)) {
				zone2.remove(p);
				System.out.println(p + " removed from zone2");
			}
			else if (zone3.contains(p)) {
				zone3.remove(p);
				System.out.println(p + " removed from zone3");
			}
			else if (zone4.contains(p)) {
				zone4.remove(p);
				System.out.println(p + " removed from zone4");
			}
			else if (zone5.contains(p)) {
				zone5.remove(p);
				System.out.println(p + " removed from zone5");
			}
			else if (zone6.contains(p)) {
				zone6.remove(p);
				System.out.println(p + " removed from zone6");	
			}
			else if (zone7.contains(p)) {
				zone7.remove(p);
				System.out.println(p + " removed from zone7");
			}
			else if (zone8.contains(p)) {
				zone8.remove(p);
				System.out.println(p + " removed from zone8");
			}
			
		if (!(lobby == true)) {
			
			for (Player players : Bukkit.getOnlinePlayers()) {
				
				players.playSound(players.getLocation(), Sound.FIREWORK_BLAST, 2F, 1F);
			}
			
			Bukkit.broadcastMessage(ChatColor.GOLD + p.getName() + " has left and has forfitted the match! There are " + inArena2.size() + " Players left.");
			
			if (inArena2.size() == 1) {
				
				Player playerWinner = inArena2.get(0).getPlayer();
				
				cooldownTime.remove("Countdown");
				Bukkit.broadcastMessage(ChatColor.GOLD + "We have our winner! " + inArena2.get(0).getPlayer().getDisplayName());
				
				World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.win.world"));
                double x = settings.getData().getDouble("warps.win.x");
                double y = settings.getData().getDouble("warps.win.y");
                double z = settings.getData().getDouble("warps.win.z");
                playerWinner.teleport(new Location(w, x, y, z));
                playerWinner.sendMessage(ChatColor.GREEN + "Teleported to winning area!");
                
                god = true;
                
               for (Player players : Bukkit.getOnlinePlayers()) {
                if (players != p.getPlayer()) {
                	World ww = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
			        double xx = settings.getData().getDouble("warps.waitarea.x");
			        double yy = settings.getData().getDouble("warps.waitarea.y");
			        double zz = settings.getData().getDouble("warps.waitarea.z");
			        players.teleport(new Location(ww, xx, yy, zz));
        			players.playSound(players.getLocation(), Sound.FIREWORK_BLAST, 2F, 1F);
                	}
                }
				
			}

			
			World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
	        double x = settings.getData().getDouble("warps.waitarea.x");
	        double y = settings.getData().getDouble("warps.waitarea.y");
	        double z = settings.getData().getDouble("warps.waitarea.z");
	        p.teleport(new Location(w, x, y, z));
			}
		}
//	if (lobby != true) {
//		if (!(p.getName().equalsIgnoreCase("ABkayCkay") || p.getName().equalsIgnoreCase("MikeSN") || p.getName().equalsIgnoreCase("killzyazmadness") || p.getName().equalsIgnoreCase("Vegeta") || p.getName().equalsIgnoreCase("Hacko_Jacko") || p.getName().equalsIgnoreCase("PokeKhan") || p.getName().equalsIgnoreCase("lod77") || p.getName().equalsIgnoreCase("Pagub"))) {
//		
//			banList.add(p);
//		
//			settings.getBan().set("players", p.getName());
//        
//			settings.saveBan();
//
//			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " + p.getName() + " Better luck next time!");
//		}
//	}
		
	if (inArena2.contains(p)) {
		inArena2.remove(p);
	//inArena2.remove(p); 
	}
	// do nothing
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		
		Player p = (Player) e.getEntity();
		
		
		if (inArena2.contains(p)) {
			System.out.println(p + " is in the arena, removing him");
			System.out.println(inArena2.size());
			inArena2.remove(p);
			System.out.println("Removed player from arena.");
			System.out.println(inArena2.size());
			
			if (zone1.contains(p)) {
				zone1.remove(p);
				System.out.println(p + " removed from zone1");
			}
			else if (zone2.contains(p)) {
				zone2.remove(p);
				System.out.println(p + " removed from zone2");
			}
			else if (zone3.contains(p)) {
				zone3.remove(p);
				System.out.println(p + " removed from zone3");
			}
			else if (zone4.contains(p)) {
				zone4.remove(p);
				System.out.println(p + " removed from zone4");
			}
			else if (zone5.contains(p)) {
				zone5.remove(p);
				System.out.println(p + " removed from zone5");
			}
			else if (zone6.contains(p)) {
				zone6.remove(p);
				System.out.println(p + " removed from zone6");	
			}
			else if (zone7.contains(p)) {
				zone7.remove(p);
				System.out.println(p + " removed from zone7");
			}
			else if (zone8.contains(p)) {
				zone8.remove(p);
				System.out.println(p + " removed from zone8");
			}
			
			Bukkit.broadcastMessage(ChatColor.GOLD + p.getName() + " has been killed! There are " + inArena2.size() + " Players left alive.");
			
			Location loc = e.getEntity().getLocation();
						
				p.setHealth(20);
				p.setGameMode(GameMode.SPECTATOR);
				p.teleport(loc);
				p.sendMessage(ChatColor.RED + "You have died, you can now spectate other players!");
			
			 
			 for (Player players : Bukkit.getOnlinePlayers()) {
					
					players.playSound(players.getLocation(), Sound.FIREWORK_BLAST, 2F, 1F);
				}
			
	if (cooldownTime.get("Countdown") != null) {
	
		
				 
			}
	
	if (inArena2.size() == 1) {
		
		Player playerWinner = inArena2.get(0).getPlayer();
		
		cooldownTime.remove("Countdown");
		Bukkit.broadcastMessage(ChatColor.GOLD + "We have our winner! " + inArena2.get(0).getPlayer().getDisplayName());
		
		World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.win.world"));
        double x = settings.getData().getDouble("warps.win.x");
        double y = settings.getData().getDouble("warps.win.y");
        double z = settings.getData().getDouble("warps.win.z");
        playerWinner.teleport(new Location(w, x, y, z));
        playerWinner.sendMessage(ChatColor.GREEN + "Teleported to winning area!");
        
        god = true;
        
       for (Player players : Bukkit.getOnlinePlayers()) {
        if (players != p.getPlayer()) {
        	World ww = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
	        double xx = settings.getData().getDouble("warps.waitarea.x");
	        double yy = settings.getData().getDouble("warps.waitarea.y");
	        double zz = settings.getData().getDouble("warps.waitarea.z");
	        players.teleport(new Location(ww, xx, yy, zz));
			players.playSound(players.getLocation(), Sound.FIREWORK_BLAST, 2F, 1F);
        	}
        }
		
	}
	
	if (e.getEntity().getKiller() != null) {
		if (e.getEntity().getKiller().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER) {
			
			Player pkilled = (Player) e.getEntity();
			Player pkiller = (Player) e.getEntity().getKiller();	
	
	Player killerName = e.getEntity().getKiller();
	
	if (killerName instanceof Player ) {
		
	Bukkit.broadcastMessage(ChatColor.GREEN + pkilled.getName() + ChatColor.GOLD + " has been slain by " + ChatColor.RED + pkiller.getName() + ChatColor.GOLD + " with a " + killerName.getInventory().getItemInHand().getType());
	//Bukkit.broadcastMessage(ChatColor.GOLD + "There are " + inArena2.size() + " Players left alive.");
		
	}
		}
		else {
			//do nothing
		}
		
	}
		}
		
			}
	
	
	 @Override
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
		  final Player p = (Player)sender;
		  
		  if (cmd.getName().equalsIgnoreCase("boat")) {
			  if (args.length == 0) {
				  if (p.hasPermission("boat.admin")) {
					  p.sendMessage(ChatColor.GREEN + "/boat setwarp waitarea - Sets the waiting area location");
					  p.sendMessage(ChatColor.GREEN + "/boat start - Stats the game by teleporting players to the random locations");
					  //p.sendMessage(ChatColor.GREEN + "/boat fillslots - Allows any players to join the arena without the permission");
				  }
				  else {
				  p.sendMessage(ChatColor.GREEN + "/boat join");
				  }
			  	}
			  else if (args.length == 1) {
				  
				  
				  if (args[0].equalsIgnoreCase("reset")) {
					  if (p.hasPermission("boat.admin")) {
						  
						  for (Player playersOnline : Bukkit.getOnlinePlayers()) {
						        for(Entity en : playersOnline.getWorld().getEntities()){
						            if(!(en instanceof Player)) {
						            	if (!(en instanceof ItemFrame)) {

						            en.remove();
						            	}
						            }
						      }
							}
						  
						  cooldownTime.remove("Countdown");
						  cooldownTime.clear();
						  
						  Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb World set 450 450 spawn");
						  Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist off");
						  
						  int banned = banList.size();
						  //int banned2 = banList2.size();

						  System.out.println(banList.size());
						  //System.out.println(banList.get(0));
						  
						  for (int i = 1; i <= banned; i++) {
							  int playerBanSpot = i -1;

							  Player playerInBan = banList.get(playerBanSpot);

							  Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "unban " + playerInBan.getPlayer().getName());

							  playerInBan.getInventory().clear();
							  playerInBan.getInventory().setArmorContents(null);
						  
						  World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
					      double x = settings.getData().getDouble("warps.waitarea.x");
					      double y = settings.getData().getDouble("warps.waitarea.y");
					      double z = settings.getData().getDouble("warps.waitarea.z");
					      playerInBan.teleport(new Location(w, x, y, z));
					      
					      Location loc = new Location(w, x, y, z);
					      
					      playerInBan.setFoodLevel(20);
					      playerInBan.setHealth(20);
					      
						  playerInBan.setBedSpawnLocation(loc, true);
						

						  }
						  
						  for (Player online : Bukkit.getOnlinePlayers()) {
			                  
							  World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
						      double x = settings.getData().getDouble("warps.waitarea.x");
						      double y = settings.getData().getDouble("warps.waitarea.y");
						      double z = settings.getData().getDouble("warps.waitarea.z");
							  
							   online.teleport(new Location(w, x, y, z));
							   
							   Location loc = new Location(w, x, y, z);
				               online.setBedSpawnLocation(loc);
				               online.setGameMode(GameMode.SURVIVAL);
				               online.getInventory().clear();
				               online.getInventory().setArmorContents(null);
				               
				               online.setFoodLevel(20);
				   			   online.setHealth(20);
		                              
				                    
							  	}
						  
						  settings.getBan().set("players", null);
			               settings.saveBan();
						  
						  inArena2.clear();
						  System.out.println("inArena2 list size: "+ inArena2.size());
						  
						  gracePeriod.remove("Grace");
						  gracePeriod.clear();
						  System.out.println("gracePeriod on reset size: "+ gracePeriod.size());
						  
						  god = true;
						  lobby = true;
						  gameStarted = false;
						  
						  banList.clear();
						  
						  p.sendMessage(ChatColor.GREEN + "Sucessfully unbanned all banned players and re-set cooldowns");
					  }
				  }
				  
				  if (args[0].equalsIgnoreCase("gamestarted")) {
					  p.sendMessage(ChatColor.GOLD + " " + gameStarted);
				  }
				  
				  if (args[0].equalsIgnoreCase("join")) {
					 if (!inArena2.contains(p)) {
					  if (p.isOp() == true) {
						  
					  inArena2.add(p);
					  p.sendMessage(ChatColor.GREEN + "You have sucesfully joined the arena, teleporting to waiting area.");

					  if (settings.getData().getConfigurationSection("warps.waitarea") == null) {
		                    p.sendMessage(ChatColor.RED + "Warp waitarea does not exist!");
		                    return true;
		            }
		        		World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
		                double x = settings.getData().getDouble("warps.waitarea.x");
		                double y = settings.getData().getDouble("warps.waitarea.y");
		                double z = settings.getData().getDouble("warps.waitarea.z");
		                p.teleport(new Location(w, x, y, z));
		                p.sendMessage(ChatColor.GREEN + "Teleported to waiting area!");
					  
					  for (Player staff : Bukkit.getOnlinePlayers()) {
		                  
		                  if (staff.hasPermission("arena.admin")) {
		                	  staff.sendMessage(ChatColor.BLUE + "A challenger has joined the arena, there are now " + inArena2.size() + " People in the arena.");
                              
		                    }
					  }
				  }
					  else {
						  p.sendMessage("You cannot join the area");
					  }
				  }
			  }
				  if (args[0].equalsIgnoreCase("fillslots")) {
					  allowAll = true;
					  p.sendMessage(ChatColor.GREEN + "Allowed all players to join arena");
				  }
				  
				  if (args[0].equalsIgnoreCase("start")) {
					  if (p.hasPermission("arena.admin")) {
						  
						  //final Player p1 = (Player) sender;
						  
						  final int arenaSize = inArena2.size();
						  gameStarted = true;
						  lobby = false;
						  
						  //p.sendMessage(ChatColor.GREEN + "Starting arena, teleporting players to random locations within 560 blocks radius.");
						  Bukkit.broadcastMessage(ChatColor.GOLD + "The game has started! Teleporting to ships! If you fall in the water, you will die!");
						  
						  //Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb World set 450 450 spawn");
						  //Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist on");
						  
						  
						  
						 //double x = settings.getData().getDouble("warps.spawn.x");
						 //double z = settings.getData().getDouble("warps.spawn.z");
						  
						  for (int i = 1; i <= arenaSize; i++) {
							  int playerSpot = i -1;
							  
							  final Player playerInArena = inArena2.get(playerSpot);
							  
							  System.out.println("Looping: "+playerInArena);
							  
							  	playerInArena.setFoodLevel(20);
								playerInArena.setHealth(20);
								
								if (zone2.contains(playerInArena)) {
									System.out.println("Started zone2");
									tpDelay(playerInArena, "zone2");
									 System.out.println("Done zone2");
									playerInArena.sendMessage(ChatColor.RED + "You have been teleported to your starting boat!");
								}
								if (zone1.contains(playerInArena)) {
									System.out.println("Started zone1");
									tpDelay(playerInArena, "zone1");
									playerInArena.sendMessage(ChatColor.RED + "You have been teleported to your starting boat!");
									System.out.println("Done zone1");
								}
								if (zone3.contains(playerInArena)) {
									tpDelay(playerInArena, "zone3");
									playerInArena.sendMessage(ChatColor.RED + "You have been teleported to your starting boat!");
								}
								if (zone4.contains(playerInArena)) {
									tpDelay(playerInArena, "zone4");
									playerInArena.sendMessage(ChatColor.RED + "You have been teleported to your starting boat!");
								}
								if (zone5.contains(playerInArena)) {
									tpDelay(playerInArena, "zone5");
									playerInArena.sendMessage(ChatColor.RED + "You have been teleported to your starting boat!");
								}
								if (zone6.contains(playerInArena)) {
									tpDelay(playerInArena, "zone6");
									playerInArena.sendMessage(ChatColor.RED + "You have been teleported to your starting boat!");
								}
								if (zone7.contains(playerInArena)) {
									tpDelay(playerInArena, "zone7");
									playerInArena.sendMessage(ChatColor.RED + "You have been teleported to your starting boat!");
								}
								if (zone8.contains(playerInArena)) {
									tpDelay(playerInArena, "zone8");
									playerInArena.sendMessage(ChatColor.RED + "You have been teleported to your starting boat!");
								}
					
								 ItemStack boat = new ItemStack(Material.BOAT, 1);
								 ItemMeta itemMeta = boat.getItemMeta();
								 itemMeta.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Starting Boat"));
								 itemMeta.setLore(Arrays.asList(ChatColor.GREEN + "A starting boat incase you spawn in water!"));
								 boat.setItemMeta(itemMeta);
								 
								 ItemStack compass = new ItemStack(Material.COMPASS, 1);
								 ItemMeta itemMeta2 = compass.getItemMeta();
								 itemMeta2.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Track Player's"));
								 itemMeta2.setLore(Arrays.asList(ChatColor.GREEN + "Right click to track the nearest player to you!"));
								 compass.setItemMeta(itemMeta2);
								 
								 ItemStack bow = new ItemStack(Material.BOW, 1);
								 bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
								 ItemMeta itemMeta3 = bow.getItemMeta();
								 itemMeta3.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Bow of DOOM"));
								 itemMeta3.setLore(Arrays.asList(ChatColor.GREEN + "Take to the Crowsnest!"));
								 bow.setItemMeta(itemMeta3);
								 
								 ItemStack arrows = new ItemStack(Material.ARROW, 32);
								 ItemStack cookedfish = new ItemStack(Material.COOKED_FISH, 32);
								 
								 ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
								 sword.addEnchantment(Enchantment.KNOCKBACK, 2);
								 ItemMeta itemMeta4 = sword.getItemMeta();
								 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Knockback Stone Sword"));
								 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "Rek your opponents!"));
								 sword.setItemMeta(itemMeta4);
								 
								 ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
								 ItemMeta itemMeta5 = chest.getItemMeta();
								 itemMeta5.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Iron Chest"));
								 itemMeta5.setLore(Arrays.asList(ChatColor.GREEN + "Your only protection!"));
								 chest.setItemMeta(itemMeta5);
								
								 if (!playerInArena.getInventory().contains(arrows)) {
									 playerInArena.getInventory().addItem(arrows);
									 }
								 
								 if (!playerInArena.getInventory().contains(cookedfish)) {
									 playerInArena.getInventory().addItem(cookedfish);
									 }
								 
								 if (!playerInArena.getInventory().contains(chest)) {
										playerInArena.getInventory().setChestplate(chest);
									 }
								 
								 if (!playerInArena.getInventory().contains(sword)) {
									playerInArena.getInventory().addItem(sword);
									
								 }
								 
								 
								 
								 if (!playerInArena.getInventory().contains(compass)) {
										playerInArena.getInventory().addItem(compass);
										
									 }

					  }

							  
							  
							  final int radius = 450;
							  
							  cooldownTime.put("Countdown", radius);
							  gracePeriod.put("Grace", 10);
							  final World world = Bukkit.getServer().getWorld("World");
							  
							  //final int night = 18000;
							  final int day = 6000;
							  
							  System.out.println("gamestarted on start = " + gameStarted);
							  
							  world.setTime(day);
							  
							  System.out.println("gracePeriod on start size: "+ gracePeriod.size());
							  System.out.println("inArena2 list on start size: "+ inArena2.size());
							  
							  
							  //if (gameStarted = true) {
								  
								  //String ckay = "ABkayCkay";
									 
								
								//Player playerInArena = Bukkit.getPlayer(ckay);
								  
									 //final World world = playerInArena.getWorld();
									 final int night = 18000;
									 //final int day = 6000;
									  
									  nightTask.put("night", new BukkitRunnable() {
					    					
											@Override
											public void run() {
												
												world.setTime(night);
												
												if (gameStarted = false) {
													world.setTime(day);
													nightTask.remove("night");
									    			nightTask.clear();
													cancel();
												}

											}
					    					
					    				});
					    				
					    				nightTask.get("night").runTaskTimer(this, 10800, 20);
									  
//									  Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
//										    public void run() {
//										    	
//										    	world.setTime(night);
//										    	
//										    }
//										}, 20, 20);
									  
									  gracePeriod2.put("Grace", 60);
									  
									  graceTask.put("grace", new BukkitRunnable() {
										  
										    public void run() {
										    	
										    	if (gracePeriod2.get("Grace") != null) {
													   
										    		gracePeriod2.put("Grace", gracePeriod2.get("Grace") - 1);
										    	
										    		if (gracePeriod2.get("Grace") > 0) {
										    	
										    	Bukkit.broadcastMessage(ChatColor.GOLD + "There is a grace period for " + (gracePeriod2.get("Grace") + " more seconds!"));

										    		}
										    		
										    		if (gracePeriod2.get("Grace") == 0) {
										    			
										    			god = false;
										    			gameStarted = true;
										    			
										    			for (int i = 1; i <= arenaSize; i++) {
															  int playerSpot = i -1;
															  
															  Player playerInArena = inArena2.get(playerSpot);
															  
															     ItemStack bow = new ItemStack(Material.BOW, 1);
																 bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
																 ItemMeta itemMeta3 = bow.getItemMeta();
																 itemMeta3.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Bow of DOOM"));
																 itemMeta3.setLore(Arrays.asList(ChatColor.GREEN + "Take to the Crowsnest!"));
																 bow.setItemMeta(itemMeta3);
																 
																 if (!playerInArena.getInventory().contains(bow)) {
																	 playerInArena.getInventory().addItem(bow);

																    }
										    			}
										    			
										    			Bukkit.broadcastMessage(ChatColor.RED + "The grace period has ended! Player damage is now enabled! Good luck Players!");
										    			graceTask.remove("grace");
										    			graceTask.clear();
										    			cancel();
										    			
										    			}
										    		}
										    	
										    }
										});
									  
									  graceTask.get("grace").runTaskTimer(this, 20, 20);
									  
									  borderTask.put("border", new BukkitRunnable() {
										    public void run() {
										    	
										    	if (cooldownTime.get("Countdown") != null) {
										   
										    	cooldownTime.put("Countdown", cooldownTime.get("Countdown") - 50);
										    	System.out.println(cooldownTime.get("Countdown"));
										    	
										    	
										    	if (cooldownTime.get("Countdown") == 350) {
										    		 
										    		for (Player playersOnline : Bukkit.getOnlinePlayers()) {
										    			
										    			if (!(playersOnline.getName().equalsIgnoreCase("ABkayCkay") || playersOnline.getName().equalsIgnoreCase("MikeSN") || playersOnline.getName().equalsIgnoreCase("killzyazmadness") || playersOnline.getName().equalsIgnoreCase("Vegeta") || playersOnline.getName().equalsIgnoreCase("Hacko_Jacko") || playersOnline.getName().equalsIgnoreCase("PokeKhan") || playersOnline.getName().equalsIgnoreCase("lod77") || playersOnline.getName().equalsIgnoreCase("Pagub"))) {	
										    				if (!(playersOnline.getGameMode() == GameMode.SPECTATOR)) {
										    				
										    		playersOnline.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600, 1));
										    				}
										    		
										    			}
										    		}
										    		Bukkit.broadcastMessage(ChatColor.RED + "Blindness event for 30 seconds!");
										    	}
										    	
										    	if (cooldownTime.get("Countdown") == 250) {
										    		for (Player playersOnline : Bukkit.getOnlinePlayers()) {
										    			
										    			if (!(playersOnline.getName().equalsIgnoreCase("ABkayCkay") || playersOnline.getName().equalsIgnoreCase("MikeSN") || playersOnline.getName().equalsIgnoreCase("killzyazmadness") || playersOnline.getName().equalsIgnoreCase("Vegeta") || playersOnline.getName().equalsIgnoreCase("Hacko_Jacko") || playersOnline.getName().equalsIgnoreCase("PokeKhan") || playersOnline.getName().equalsIgnoreCase("lod77") || playersOnline.getName().equalsIgnoreCase("Pagub"))) {	
										    		
										    				if (!(playersOnline.getGameMode() == GameMode.SPECTATOR)) {
										    				
										    		playersOnline.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 1));
										    				}
										    		
										    			}
										    		}
										    		Bukkit.broadcastMessage(ChatColor.RED + "Confusion event for 30 seconds!");
										    	}
										    	
										    	
										    		if (cooldownTime.get("Countdown") == 100) {
															  
												                cooldownTime.remove("Countdown");
												                borderTask.remove("border");
												    			borderTask.clear();
												                cancel();
															 
										    			}
										    			
										    		}
										    }
										});
									  borderTask.get("border").runTaskTimer(this, 10800, 3600);
								
						    
					  }  
						  
						  
				  }
			  }
			  else if (args.length == 2) {
				  if ((args[0].equalsIgnoreCase("setwarp"))) {
						 if (p.hasPermission("arena.admin")) {
							if (settings.getData().get("warps." +args[1]) != (null)) {
								p.sendMessage(ChatColor.RED + args[1] + " warp already exists. If you want to overwrite it, do /arena delwarp "+args[1] + ". And then re-set the new warp.");
							}
							else {
			        	 	
			        		settings.getData().set("warps." + args[1] + ".world", p.getLocation().getWorld().getName());
			                settings.getData().set("warps." + args[1] + ".x", p.getLocation().getX());
			                settings.getData().set("warps." + args[1] + ".y", p.getLocation().getY());
			                settings.getData().set("warps." + args[1] + ".z", p.getLocation().getZ());
			                settings.saveData();
			                p.sendMessage(ChatColor.GREEN + "Set warp " + args[1] + "!");
							}	
			        	  }
						 else {
							 p.sendMessage(ChatColor.RED + "You do not have permission to set a warp!");
						 }
						}
			        	
			        	if ((args[0].equalsIgnoreCase("delwarp"))) {
			        	  if (p.hasPermission("arena.admin")) {
			        		if (settings.getData().getConfigurationSection("warps." + args[1]) == null) {
			                    p.sendMessage(ChatColor.RED + "Warp " + args[1] + " does not exist!");
			                    return true;
			            }
			        		settings.getData().set("warps." + args[1], null);
			                settings.saveData();
			                p.sendMessage(ChatColor.GREEN + "Removed warp " + args[1] + "!");
			        	  }
			        	  else {
								 p.sendMessage(ChatColor.RED + "You do not have permission to delete a warp!");
							 }
			        	}
			        	
			        	if ((args[0].equalsIgnoreCase("warp"))) {
			        	  if (p.hasPermission("arena.admin")) {
			        		if (settings.getData().getConfigurationSection("warps." + args[1]) == null) {
			                    p.sendMessage(ChatColor.RED + "Warp " + args[1] + " does not exist!");
			                    return true;
			            }
			        		World w = Bukkit.getServer().getWorld(settings.getData().getString("warps." + args[1] + ".world"));
			                double x = settings.getData().getDouble("warps." + args[1] + ".x");
			                double y = settings.getData().getDouble("warps." + args[1] + ".y");
			                double z = settings.getData().getDouble("warps." + args[1] + ".z");
			                p.teleport(new Location(w, x, y, z));
			                p.sendMessage(ChatColor.GREEN + "Teleported to " + args[1] + "!");
			        		
			        	  }
			        	  else {
			        		  p.sendMessage(ChatColor.RED + "You do not have permission to warp to a spawn location!");
			        	  }
			        	}
			  }
		     }
		 
		  
		  return false;
	 }
	
    }
	
