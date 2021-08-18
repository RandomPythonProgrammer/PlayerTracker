package com.jchen.playertracker;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler()
	public void onClick(PlayerInteractEvent event) {
		if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
				&& event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
			event.getPlayer().openInventory(createTrackerMenu(event.getPlayer()));
		}
	}

	@EventHandler()
	public void onItemClick(InventoryClickEvent event) {
		if (event.getWhoClicked().getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
			Player player = ((SkullMeta) event.getCurrentItem().getItemMeta()).getOwningPlayer().getPlayer();
			((Player) event.getWhoClicked()).setCompassTarget(player.getLocation());
			event.getWhoClicked().getLocation().getWorld().playSound(event.getWhoClicked().getLocation(),
					Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			event.setCancelled(true);
		}
	}

	public Inventory createTrackerMenu(Player player) {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		int inventorySize;
		if (players.size() <= 9) {
			inventorySize = 9;
		} else {
			inventorySize = (int) (Math.ceil(players.size() / 9) * 9);
		}
		Inventory menu = Bukkit.createInventory(null, inventorySize, "Choose a player to track:");
		for (Player onlinePlayer : players) {
			if (!onlinePlayer.equals(player)) {
				menu.addItem(createPlayerItem(onlinePlayer));
			}
		}
		return menu;
	}

	public ItemStack createPlayerItem(Player player) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
		skullMeta.setDisplayName(player.getDisplayName());
		skullMeta.setOwningPlayer(player);
		item.setItemMeta(skullMeta);
		return item;
	}

}
