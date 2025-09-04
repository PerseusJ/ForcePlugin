package org.perseus.forcePlugin;

/**
 * Represents the fixed alignments a player can choose in the Force.
 * Using an enum prevents errors from typos (e.g., "light" vs "LIGHT") and ensures type safety.
 */
public enum ForceSide {
    /**
     * Represents the Light Side. For abilities like Heal and Push.
     */
    LIGHT,

    /**
     * Represents the Dark Side. For abilities like Choke and Lightning.
     */
    DARK,

    /**
     * The default state for a player who has not yet chosen their path.
     */
    NONE
}