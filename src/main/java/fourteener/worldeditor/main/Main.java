package fourteener.worldeditor.main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import fourteener.worldeditor.commands.CommandFx;
import fourteener.worldeditor.commands.CommandUndo;
import fourteener.worldeditor.worldeditor.brush.BrushListener;
import fourteener.worldeditor.worldeditor.selection.SelectionWandListener;

public class Main extends JavaPlugin {
	// Global variables
	public static World world;
	public static SimplexNoise simplexNoise;
	
	// For debugging
	public static boolean isDebug = true;
	
	@Override
	public void onEnable () {
		// Register commands
		this.getCommand("fx").setExecutor(new CommandFx());
		CommandUndo undoCmd = new CommandUndo();
		this.getCommand("undo").setExecutor(undoCmd);
		this.getCommand("redo").setExecutor(undoCmd);
		
		// Register listeners
		getServer().getPluginManager().registerEvents(new SelectionWandListener(), this);
		getServer().getPluginManager().registerEvents(new BrushListener(), this);
		
		// These are needed by the plugin, but should only be loaded once as they are very slow to load
		world = Bukkit.getWorlds().get(0);
		simplexNoise = new SimplexNoise (world.getSeed()); // Seeded using the world seed for variance between worlds but consistency in the same world
	}
	
	@Override
	public void onDisable () {
		
	}
}
