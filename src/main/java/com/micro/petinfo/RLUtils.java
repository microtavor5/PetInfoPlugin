/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.micro.petinfo;

import net.runelite.api.AABB;
import net.runelite.api.Client;
import net.runelite.api.Model;
import net.runelite.api.WorldView;
import net.runelite.api.geometry.SimplePolygon;
import net.runelite.api.model.Jarvis;
import static net.runelite.api.Perspective.*;

public class RLUtils {
    /**
     * The following code is from the Runelite API Perspective class
     */

    static SimplePolygon calculateAABB(Client client, WorldView wv, Model m, int jauOrient, int x, int y, int z)
    {
        AABB aabb = m.getAABB(jauOrient);

        int x1 = aabb.getCenterX();
        int y1 = aabb.getCenterZ();
        int z1 = aabb.getCenterY();

        int ex = aabb.getExtremeX();
        int ey = aabb.getExtremeZ();
        int ez = aabb.getExtremeY();

        int x2 = x1 + ex;
        int y2 = y1 + ey;
        int z2 = z1 + ez;

        x1 -= ex;
        y1 -= ey;
        z1 -= ez;

        float[] xa = new float[]{
                x1, x2, x1, x2,
                x1, x2, x1, x2
        };
        float[] ya = new float[]{
                y1, y1, y2, y2,
                y1, y1, y2, y2
        };
        float[] za = new float[]{
                z1, z1, z1, z1,
                z2, z2, z2, z2
        };

        int[] x2d = new int[8];
        int[] y2d = new int[8];

        modelToCanvas(client, wv, 8, x, y, z, 0, xa, ya, za, x2d, y2d);

        return Jarvis.convexHull(x2d, y2d);
    }
}
