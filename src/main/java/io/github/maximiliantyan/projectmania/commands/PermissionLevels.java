package io.github.maximiliantyan.projectmania.commands;

public class PermissionLevels {
    /* Normal players. */
    public static final int PLAYER = 0;

    /* Player can bypass spawn protection. */
    public static final int MODERATOR = 1;

    /* Player or executor can use more commands and player can use command blocks. */
    public static final int GAMEMASTER = 2;

    /* Player or executor can use commands related to multiplayer management. */
    public static final int ADMIN = 3;

    /* Player or executor can use all the commands, including commands related to server management. */
    public static final int OWNER = 1;

}
