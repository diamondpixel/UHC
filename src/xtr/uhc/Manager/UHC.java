package xtr.uhc.Manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import xtr.uhc.Core;
import xtr.uhc.Enums.GameState;
import xtr.uhc.Enums.ScatterType;
import xtr.uhc.Enums.TeamType;
import xtr.uhc.Util.Utilities;

import java.util.*;

public class UHC {

    private Core core = Core.instance;
    private Utilities util = core.getUtil();
    public World lobby_world = Bukkit.getWorld(core.getConfig().get("Worlds.Lobby.World").toString());
    public World uhc_world = Bukkit.getWorld(core.getConfig().get("Worlds.UHC").toString());
    public Location uhc_center = new Location(uhc_world, 0, Objects.requireNonNull(uhc_world).getHighestBlockAt(0, 0).getY(), 0);
    public Location lobby_loc = util.getLocationString(core.getConfig().get("Worlds.Lobby.Spawn").toString());
    public Map<UUID, UHCPlayer> players = new HashMap<>();
    private List<UUID> moderators = new ArrayList<>();
    private List<UUID> spectators = new ArrayList<>();
    private UUID Host;
    private GameState gameState = GameState.NOTSTARTED;
    private ScatterType scatterType = ScatterType.NORMAL;
    private TeamType teamType = TeamType.I;

    public void setLobbyWorld(World world) {
        lobby_world = world;
        core.getConfig().set("Worlds.Lobby.World", world.toString());
    }

    public World getLobbyWorld() {
        return lobby_world;
    }

    public void setUHCWorld(World world) {
        uhc_world = world;
        core.getConfig().set("Worlds.UHC", world.toString());
    }

    public World getUHCWorld() {
        return uhc_world;
    }

    public void setUHCCenter(Location location) {
        uhc_center = location;

    }

    public Location getUHCCenter() {
        return uhc_center;
    }

    public void setLobbySpawn(Location location) {
        lobby_loc = location;
        core.getConfig().set("Worlds.Lobby.Spawn", util.locationToString(location));
    }

    public Location getLobbySpawn() {
        return lobby_loc;
    }

    public Map<UUID, UHCPlayer> getPlayers() {
        return players;
    }

    public List<UUID> getModerators() {
        return moderators;
    }

    public void addModerators(UUID uuid) {
        this.moderators.add(uuid);
    }

    public List<UUID> getSpectators() {
        return spectators;
    }

    public void addSpectator(UUID uuid) {
        this.spectators.add(uuid);
    }

    public UUID getHost() {
        return Host;
    }

    public void setHost(UUID uuid) {
        this.Host = uuid;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ScatterType getScatterType() {
        return scatterType;
    }

    public TeamType getTeamType() {
        return teamType;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public void setScatterType(ScatterType type) {
        this.scatterType = type;
    }

    public void setTeamType(TeamType type) {
        this.teamType = type;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public void addPlayer(UUID uuid, UHCPlayer player) {
        players.put(uuid, player);
    }

    public void removePlayer(UHCPlayer player) {
        players.remove(player);
    }
}
