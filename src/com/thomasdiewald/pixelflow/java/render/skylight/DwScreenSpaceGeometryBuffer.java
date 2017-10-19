/**
 * 
 * PixelFlow | Copyright (C) 2017 Thomas Diewald (www.thomasdiewald.com)
 * 
 * src  - www.github.com/diwi/PixelFlow
 * 
 * A Processing/Java library for high performance GPU-Computing.
 * MIT License: https://opensource.org/licenses/MIT
 * 
 */






package com.thomasdiewald.pixelflow.java.render.skylight;

import com.jogamp.opengl.GL2;
import com.thomasdiewald.pixelflow.java.DwPixelFlow;
import com.thomasdiewald.pixelflow.java.dwgl.DwGLTextureUtils;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.opengl.PGraphics3D;
import processing.opengl.PShader;

public class DwScreenSpaceGeometryBuffer {
  
  String dir = DwPixelFlow.SHADER_DIR+"render/skylight/";
  
  DwPixelFlow context;
  public PApplet papplet;
  public PGraphics3D pg_geom;
  
  public DwSceneDisplay scene_display;
  public PShader shader;
  
  public DwScreenSpaceGeometryBuffer(DwPixelFlow context, DwSceneDisplay scene_display){
    this.context = context;
    this.papplet = context.papplet;
    this.scene_display = scene_display;
    
    String[] src_frag = context.utils.readASCIIfile(dir+"geometryBuffer.frag");
    String[] src_vert = context.utils.readASCIIfile(dir+"geometryBuffer.vert");

    this.shader = new PShader(papplet, src_vert, src_frag);
//    this.shader = papplet.loadShader(dir+"geometryBuffer.frag", dir+"geometryBuffer.vert");
  }
  
  public void resize(int w, int h){
    boolean[] resized = {false};
    pg_geom = DwGLTextureUtils.changeTextureSize(papplet, pg_geom, w, h, 0, resized);
    
    if(resized[0]){
      DwGLTextureUtils.changeTextureFormat(pg_geom, GL2.GL_RGBA16F, GL2.GL_RGBA, GL2.GL_FLOAT, GL2.GL_NEAREST, GL2.GL_CLAMP_TO_EDGE);
    }
  }
  
  public void updateMatrices(PGraphics3D pg_src){
    DwGLTextureUtils.copyMatrices(pg_src, pg_geom);
  }

  public void update(PGraphics3D pg_src){
    
    resize(pg_src.width, pg_src.height); // TODO
    
    pg_geom.beginDraw();
    updateMatrices(pg_src);
    pg_geom.blendMode(PConstants.REPLACE);
    pg_geom.clear();
    pg_geom.shader(shader);
    pg_geom.noStroke();
    scene_display.display(pg_geom);
    pg_geom.endDraw();
  }
  
}
