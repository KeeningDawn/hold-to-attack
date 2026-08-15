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

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import java.util.Optional;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import net.minecraft.network.chat.Component;

public class HoldToAttackModMenuIntegration implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return parent -> {
      HoldToAttackConfig config = HoldToAttackConfig.get();

      ConfigBuilder builder =
          ConfigBuilder.create()
              .setParentScreen(parent)
              .setTitle(Component.translatable("title.hold-to-attack.config"))
              .setSavingRunnable(config::save);

      ConfigCategory general =
          builder.getOrCreateCategory(Component.translatable("category.hold-to-attack.general"));
      ConfigEntryBuilder entryBuilder = builder.entryBuilder();

      general.addEntry(
          entryBuilder
              .startBooleanToggle(
                  Component.translatable("option.hold-to-attack.enabled"), config.enabled)
              .setDefaultValue(true)
              .setSaveConsumer(value -> config.enabled = value)
              .build());

      general.addEntry(
          entryBuilder
              .startBooleanToggle(
                  Component.translatable("option.hold-to-attack.random_delay_enabled"),
                  config.randomDelayEnabled)
              .setDefaultValue(false)
              .setSaveConsumer(value -> config.randomDelayEnabled = value)
              .build());

      IntegerListEntry minEntry =
          entryBuilder
              .startIntField(
                  Component.translatable("option.hold-to-attack.random_delay_min_ms"),
                  config.randomDelayMinMs)
              .setDefaultValue(10)
              .setMin(0)
              .setSaveConsumer(value -> config.randomDelayMinMs = value)
              .build();
      general.addEntry(minEntry);

      general.addEntry(
          entryBuilder
              .startIntField(
                  Component.translatable("option.hold-to-attack.random_delay_max_ms"),
                  config.randomDelayMaxMs)
              .setDefaultValue(50)
              .setMin(0)
              .setErrorSupplier(
                  value ->
                      value < minEntry.getValue()
                          ? Optional.of(
                              Component.translatable(
                                  "option.hold-to-attack.random_delay_max_too_low"))
                          : Optional.empty())
              .setSaveConsumer(value -> config.randomDelayMaxMs = value)
              .build());

      return builder.build();
    };
  }
}
