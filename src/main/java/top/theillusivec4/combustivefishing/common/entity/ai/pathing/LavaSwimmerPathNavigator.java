/*
 * Copyright (C) 2017-2019  C4
 *
 * This file is part of Combustive Fishing, a mod made for Minecraft.
 *
 * Combustive Fishing is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Combustive Fishing is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Combustive Fishing.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.combustivefishing.common.entity.ai.pathing;

import javax.annotation.Nonnull;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.world.World;

public class LavaSwimmerPathNavigator extends SwimmerPathNavigator {

    private boolean isSwordfish;

    public LavaSwimmerPathNavigator(MobEntity mobEntity, World worldIn) {
        super(mobEntity, worldIn);
    }

    @Nonnull
    @Override
    protected PathFinder getPathFinder(int param1) {
        this.isSwordfish = false;
        this.nodeProcessor = new LavaSwimNodeProcessor(isSwordfish);
        return new PathFinder(this.nodeProcessor, param1);
    }
}
