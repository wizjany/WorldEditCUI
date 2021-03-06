package com.mumfrey.worldeditcui.render.shapes;

import com.mumfrey.worldeditcui.render.LineStyle;
import com.mumfrey.worldeditcui.render.RenderStyle;
import com.mumfrey.worldeditcui.render.points.PointCube;
import com.mumfrey.worldeditcui.util.Vector3;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * Draws the circles around a cylindrical region
 * 
 * @author yetanotherx
 * @author Adam Mummery-Smith
 */
public class RenderCylinderCircles extends RenderRegion
{
	protected double radX = 0;
	protected double radZ = 0;
	protected int minY;
	protected int maxY;
	protected double centreX;
	protected double centreZ;
	
	public RenderCylinderCircles(RenderStyle style, PointCube centre, double radX, double radZ, int minY, int maxY)
	{
		super(style);
		this.radX = radX;
		this.radZ = radZ;
		this.minY = minY;
		this.maxY = maxY;
		this.centreX = centre.getPoint().getX() + 0.5;
		this.centreZ = centre.getPoint().getZ() + 0.5;
	}

	@Override
	public void render(Vector3 cameraPos)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBufferBuilder();

		double xPos = this.centreX - cameraPos.getX();
		double zPos = this.centreZ - cameraPos.getZ();
		
		for (LineStyle line : this.style.getLines())
		{
			if (!line.prepare(this.style.getRenderType()))
			{
				continue;
			}
			
			double twoPi = Math.PI * 2;
			for (int yBlock = this.minY + 1; yBlock <= this.maxY; yBlock++)
			{
				buf.begin(GL11.GL_LINE_LOOP, VertexFormats.POSITION);
				line.applyColour();
				
				for (int i = 0; i <= 75; i++)
				{
					double tempTheta = i * twoPi / 75;
					double tempX = this.radX * Math.cos(tempTheta);
					double tempZ = this.radZ * Math.sin(tempTheta);
					
					buf.vertex(xPos + tempX, yBlock - cameraPos.getY(), zPos + tempZ).next();
				}
				tessellator.draw();
			}
		}
	}
}
