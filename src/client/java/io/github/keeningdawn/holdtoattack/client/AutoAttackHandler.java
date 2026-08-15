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

import io.github.keeningdawn.holdtoattack.client.mixin.MinecraftInvokerMixin;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.EntityHitResult;

public class AutoAttackHandler implements ClientTickEvents.EndTick {
  private static final long MIN_SWING_INTERVAL_MS = 1000L / 16L;

  private final HoldToAttackConfig config;

  private boolean cooldownReadyLastTick = false;
  private long jitterDeadlineMs = 0L;
  private long lastAutoSwingAtMs = 0L;

  public AutoAttackHandler(HoldToAttackConfig config) {
    this.config = config;
  }

  @Override
  public void onEndTick(Minecraft mc) {
    handleToggleKeybind(mc);

    if (!config.enabled || mc.player == null || mc.level == null) {
      cooldownReadyLastTick = false;
      return;
    }

    float scale = mc.player.getAttackStrengthScale(0f);
    boolean cooldownReadyNow = scale >= 1.0f;

    // roll jitter exactly once, at the moment cooldown completes, not every tick
    if (cooldownReadyNow && !cooldownReadyLastTick) {
      jitterDeadlineMs =
          config.randomDelayEnabled
              ? System.currentTimeMillis()
                  + ThreadLocalRandom.current()
                      .nextLong(config.randomDelayMinMs, config.randomDelayMaxMs + 1)
              : 0L;
    }
    cooldownReadyLastTick = cooldownReadyNow;
    if (!cooldownReadyNow) {
      return;
    }

    if (!mc.options.keyAttack.isDown()) {
      return;
    }
    if (!(mc.hitResult instanceof EntityHitResult)) {
      return;
    }

    long now = System.currentTimeMillis();
    if (config.randomDelayEnabled && now < jitterDeadlineMs) {
      return;
    }
    if (now - lastAutoSwingAtMs < MIN_SWING_INTERVAL_MS) {
      return;
    }

    ((MinecraftInvokerMixin) mc).holdToAttack$startAttack();
    lastAutoSwingAtMs = now;
  }

  private void handleToggleKeybind(Minecraft mc) {
    while (HoldToAttackKeybinds.TOGGLE_ENABLED.consumeClick()) {
      config.enabled = !config.enabled;
      config.save();
      if (mc.player != null) {
        String key =
            config.enabled ? "message.hold-to-attack.enabled" : "message.hold-to-attack.disabled";
        mc.player.sendOverlayMessage(Component.translatable(key));
      }
    }
  }
}
