package xtr.uhc.Manager;

import xtr.uhc.Enums.PlayerState;
import java.util.UUID;

public class UHCPlayer {

    private PlayerState state = PlayerState.LOBBY;
    private UUID uuid;

    public UHCPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state){
        this.state = state;
    }

    public UUID getUUID() {
        return uuid;
    }
}
