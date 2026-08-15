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

import io.github.keeningdawn.holdtoattack.HoldToAttack;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class HoldToAttackKeybinds {
  private static final KeyMapping.Category CATEGORY =
      KeyMapping.Category.register(HoldToAttack.id("main"));

  public static final KeyMapping TOGGLE_ENABLED =
      new KeyMapping("key.hold-to-attack.toggle_enabled", GLFW.GLFW_KEY_UNKNOWN, CATEGORY);

  public static void register() {
    KeyBindingHelper.registerKeyBinding(TOGGLE_ENABLED);
  }
}
