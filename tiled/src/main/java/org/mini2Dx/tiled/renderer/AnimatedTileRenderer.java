/*******************************************************************************
 * Copyright 2019 See AUTHORS file
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.tiled.renderer;

import org.mini2Dx.core.Graphics;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.tiled.tileset.TilesetSource;

/**
 * A {@link TileRenderer} for animated tiles
 */
public class AnimatedTileRenderer implements TileRenderer {
	private final TileFrame[] frames;
	private final TilesetSource tilesetSource;

	private int currentFrame;
	private float timer;
	private long lastFrameId;

	public AnimatedTileRenderer(TilesetSource tilesetSource, TileFrame[] frames) {
		super();
		this.tilesetSource = tilesetSource;
		this.frames = frames;
	}

	@Override
	public void update(float delta) {
		// Prevent duplicate updates per frame
		long currentFrameId = Mdx.graphicsContext.getFrameId();
		if (currentFrameId <= lastFrameId) {
			return;
		}
		timer += delta;
		while (timer >= frames[currentFrame].duration) {
			timer -= frames[currentFrame].duration;
			currentFrame = currentFrame == frames.length - 1 ? 0 : currentFrame + 1;
		}
		lastFrameId = currentFrameId;
	}

	@Override
	public void draw(Graphics g, int renderX, int renderY) {
		g.drawSprite(getCurrentTileImage(), renderX, renderY);
	}

	@Override
	public void draw(Graphics g, int renderX, int renderY, boolean flipH, boolean flipV, boolean flipD) {
		Sprite tileImage = getCurrentTileImage();
		StaticTileRenderer.drawTileImage(g, tileImage, renderX, renderY, flipH, flipV, flipD);
	}

	@Override
	public Sprite getCurrentTileImage() {
		return tilesetSource.getTileImage(frames[currentFrame].tileId);
	}

	@Override
	public void dispose() {
	}
}
