/*
 * Copyright (C) 2026 KeeningDawn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.keeningdawn.holdtoattack.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.keeningdawn.holdtoattack.HoldToAttack;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class HoldToAttackConfig {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final Path PATH =
      FabricLoader.getInstance().getConfigDir().resolve(HoldToAttack.MOD_ID + ".json");
  private static HoldToAttackConfig instance;

  public boolean enabled = true;
  public boolean randomDelayEnabled = false;
  public int randomDelayMinMs = 10;
  public int randomDelayMaxMs = 50;

  public static HoldToAttackConfig get() {
    if (instance == null) {
      instance = load();
    }
    return instance;
  }

  public static HoldToAttackConfig load() {
    if (Files.exists(PATH)) {
      try {
        HoldToAttackConfig loaded = GSON.fromJson(Files.readString(PATH), HoldToAttackConfig.class);
        if (loaded != null) {
          instance = loaded;
          return instance;
        }
      } catch (IOException e) {
        HoldToAttack.LOGGER.error("Failed to load config, using defaults", e);
      }
    }
    instance = new HoldToAttackConfig();
    return instance;
  }

  public void save() {
    try {
      Files.writeString(PATH, GSON.toJson(this));
    } catch (IOException e) {
      HoldToAttack.LOGGER.error("Failed to save config", e);
    }
  }
}
