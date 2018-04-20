package processing.test.roll3d_android;

import android.content.Context;

import processing.core.*;
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class roll3d_android extends PApplet {







    Context context;
    final global dataglobal;

int opacidad;
Storsimple estorninos;
Atractor central, lateral1, lateral2, lateral3,lateral4;
int color_fondo, color_particula;
float flujo;
float sigue;
boolean rotacion=true;
int sentido=1;
float angulorot=0;
PVector cameraPosition;
    public roll3d_android(Context context){
        super();
        this.context=context;
        dataglobal = (global) context;
    }

public void setup (){
              
            frameRate(60);
            opacidad=255;
            sigue=0;
            
            rotacion=true;
            sentido=1;
            angulorot=0;
    estorninos=new Storsimple(50,1);
    central=new Atractor(1);
    lateral1=new Atractor(3);
    lateral2=new Atractor(3);
    lateral3=new Atractor(3);
     lateral4=new Atractor(3);
    central.posicion=new PVector(0,0); 
 lateral1.posicion=new PVector(width/2, height/2,600);
 lateral2.posicion=new PVector(-width/2, -height/2, -600);
 lateral3.posicion=new PVector(-(width/2), height/2, 600);
 lateral4.posicion=new PVector((width/2), -(height/2),-600);
     color_fondo=color(0,0,0);

   
    }


public void draw(){
   ambientLight(200,200,200);
 directionalLight(200,200,200, 0, -1, -1);
    directionalLight(200,200,200, 0, 1, 1);
  ambient(255,255,255);
   lightFalloff(0.0f, 0.0f, 0.5f);
    //lights();
    shininess(1.0f);
    cameraPosition = new PVector(1,-1,1);


  if (rotacion==true){angulorot=sentido*(frameCount * 0.01f);}
    
  cameraPosition.rotate(angulorot);
  //cameraPosition.rotate(1.75);
 println(frameCount * 0.001f);
  float distance = 700;
 
  float eye_x = cameraPosition.x * distance;
  float eye_y = 100;
  float eye_z = cameraPosition.y * distance;
 
  camera(eye_x, eye_y, eye_z, 0.0f, 0.0f, 0.0f, 0, 1, 0);
  flujo=0;
    flujo=dataglobal.getIntensity();
central.sentido=-1-flujo; 
lateral1.sentido=-0.5f*flujo;
lateral2.sentido=-0.5f*flujo;
lateral3.sentido=-0.5f*flujo;
lateral4.sentido=-0.5f*flujo;








background(color_fondo);



estorninos.aceleradorparticulas(central);
estorninos.aceleradorparticulas(lateral1);
estorninos.aceleradorparticulas(lateral2);
estorninos.aceleradorparticulas(lateral3);
estorninos.aceleradorparticulas(lateral4);
estorninos.dibujaparticulas();
//central.visible();
//lateral1.visible();
//lateral2.visible();
//lateral3.visible();
//lateral4.visible();

sigue++;

}
class Atractor {
            PVector posicion, origen_icono;
            float sentido;
            int tipo_atractor;
            int interaccion;
            Icono icono_atractor, icono_repulsor;
            Atractor (int clase){
                                posicion=new PVector(random(width), random(height));
                              interaccion=0;
                              sentido=-1;
                              tipo_atractor=clase;
                              origen_icono=new PVector (0,0);
                                }
            public PVector fuerza (PVector posicionobjeto){
            
                                                    PVector f=posicionobjeto.get();
                                                    f.sub(posicion);
            
                                                    float modulo=f.mag();
                                                    if (modulo <0) {f.mult(-1);}
                                                    f.normalize();
                                                    switch(tipo_atractor) {
                                                                         case 1: 
                                                                              f.mult(modulo/50);
                                                                         break;
                                                                         case 2: 
                                                                              f.mult(150/modulo);
                                                                         break;
                                                                         case 3: 
                                                                              f.mult(4);
                                                                         break;
                                                                         case 4: 
                                                                              f.mult(150/modulo*modulo);
                                                                         break;
                                                                           }
                                                    f.mult(sentido);
                                                    return f;
                                                    }
           public void visible(){stroke (255,255,255);
                            strokeWeight(1);
                            //if (sentido>0) {fill(0,0,0);} else {fill(255,255,255);}
                            noFill();
                            ellipse (posicion.x, posicion.y, 10, 10);
 
                          }
                          
           public void addicon () {icono_atractor=new Icono(posicion);
                             icono_repulsor=new Icono(posicion);
                            icono_atractor.addmanipulador(origen_icono, 80,"atractor",1,2);
                            icono_repulsor.addmanipulador(origen_icono, 80,"repulsor",1,3);
                               }
                       
                      public void contacto(){
                            interaccion=icono_atractor.contacto(0);
                           
                              }
                              
                              
 public void operador(){
   //PVector mouse;
   
   
  
      
   
               switch(interaccion) {
                           case 1: 
                           
                              posicion.x=mouseX;
                            
                              posicion.y=mouseY;
                             
                            break;
                        
               }
 
 }
 public void liberador(){interaccion=0;
 
                   icono_atractor.liberador();
                   }
        
                      
                    }
class Icono{
            PVector centroicono;
            float angulo;
            ArrayList<Manipulador> manipuladores;
            Icono (PVector posicionicono){
  
                                 centroicono=posicionicono;
                                 angulo=0;
                                 manipuladores = new ArrayList<Manipulador>();
  }
  
public void addmanipulador (PVector c, float r, String n, int i, int tipo){
                      switch(tipo){
                                  case 1:
                                    manipuladores.add (new Manipulador(c,r,n, i));
                                   break;
                                   case 2:
                                    manipuladores.add (new MAtractor(c,r,n, i));
                                   break;
                                   case 3:
                                    manipuladores.add (new Repulsor(c,r,n, i));
                                   break;
                                    }



}
  
  
  
  public int contacto (float angulodesalida){
    
                              int tipointeraccion=0;
                              for (int i = 0; i < manipuladores.size(); i++) {
                                              Manipulador m = manipuladores.get(i);
                                              
                                              if (tipointeraccion==0){tipointeraccion=m.contacto(angulodesalida, centroicono);} 
                              
                              
                              
                                            }
                              //println(tipointeraccion);
                              if (tipointeraccion!=0){
                                                       for (int i = 0; i < manipuladores.size(); i++) {
                                                        Manipulador m = manipuladores.get(i); 
                                                              m.reloj=300;
                                                            }  
                              
                              
                              
                              
                                                    }
                              return tipointeraccion;
                              }
  
  
  
  
  
  
  
  
  public void dibujar(float angulodesalida){
                    
                  
                    
                    for (int i = 0; i < manipuladores.size(); i++) {
                                                                       Manipulador m = manipuladores.get(i);
                                                                       m.dibuja(angulodesalida, centroicono);
                    
                                                                    } 
                              
                              
                              
                              
                    
                      }
  
  
public void liberador(){
                    for (int i = 0; i < manipuladores.size(); i++) {
                                                                       Manipulador m = manipuladores.get(i);
                                                                       m.liberador();
                    
                                                                    } 



                }


//final de la clase icono
}
class Manipulador {
float radius;
String nombre;
PVector centromanipulador;
boolean activado;

int reloj;




int indice;
Manipulador(PVector c, float r, String n, int i){

                centromanipulador=c;
                radius=r;
                nombre=n;
                indice=i;
                activado=false;
                reloj=300;
                opacidad=255;
              }

public void dibuja (float angulodesalida, PVector ci){
          //reloj--;
          if (reloj<255){opacidad=reloj;} else {opacidad=255;}
          opacidad=255;
          pushMatrix();
          translate(ci.x, ci.y);
          rotate(angulodesalida);
          if(activado){ fill(255,255,255,opacidad);} else {noFill();}
          
          stroke (255,255,255,opacidad);
          strokeWeight(3);
          ellipse (centromanipulador.x, centromanipulador.y, radius, radius);
          
          popMatrix();
          


}

public int contacto(float angulodesalida, PVector ci){
              int identi=0;
              PVector mouse, absoluta, resta ;

  
              //println(pmouseX+" "+pmouseY );
              absoluta=centromanipulador.get();
              absoluta.rotate(angulodesalida);
              absoluta.add(ci);
              mouse=new PVector (mouseX, mouseY);
              resta=PVector.sub(mouse,absoluta);
              if (resta.mag()<radius){identi=indice;activado=true;reloj=500;}
              


              return identi;



}
public void liberador(){activado=false;}



//fin class manipulador
}

class MAtractor extends Manipulador{
 PImage img;
MAtractor(PVector c, float r, String n, int i){
            
                super(c,r,n,i);
          
          img=loadImage("atractorr.png");

          }

public void dibuja (float angulodesalida, PVector ci){
         // reloj--;
          if (reloj<255){opacidad=reloj;} else {opacidad=255;}
          pushMatrix();
          translate(ci.x, ci.y);
          rotate(angulodesalida);
          //if(activado){ fill(255,255,255,opacidad);} else {noFill();}
          
          //stroke (255,255,255,opacidad);
         // strokeWeight(3);
          //ellipse (centromanipulador.x, centromanipulador.y, radius, radius);
         tint (0,0,255, opacidad);
          image(img,centromanipulador.x,centromanipulador.y);
          popMatrix();
}
//fin class MAtractor
}
class Repulsor extends Manipulador{
PImage img;
int pulse;
Repulsor(PVector c, float r, String n, int i){
            
          super(c,r,n,i);
          centromanipulador=c;
                radius=r;
                nombre=n;
                indice=i;
          img=loadImage("atractorr.png");
          pulse=1000;
          }

public void dibuja (float angulodesalida, PVector ci){
       // reloj--;
          pulse=pulse-25;
          if (pulse<0) {pulse=1000;}
          if (reloj<255){opacidad=reloj;} else {opacidad=255;}
          pushMatrix();
          translate(ci.x, ci.y);
          rotate(angulodesalida);
          //if(activado){ fill(255,255,255,opacidad);} else {noFill();}
          
          //stroke (255,255,255,opacidad);
         // strokeWeight(3);
          //ellipse (centromanipulador.x, centromanipulador.y, radius, radius);
         tint (255,0,0, opacidad);
         imageMode(CENTER);
          image(img,centromanipulador.x,centromanipulador.y);
                   stroke (0,0,0,opacidad);
          strokeWeight(10);
          ellipseMode(CENTER);
          //ellipse(centromanipulador.x,centromanipulador.y,pulse,pulse);
          popMatrix();
}
//fin class Repulsor
}
class Particula {
int r,g,b,a;
  PVector posicion, velocidad, aceleracion, gravedad;
  float limite;
  float masa;
  boolean resistencia;
  float coefroz;
  float lifespan;
  boolean eterna;
  int decay;
    PShape rock;
  Particula() {
    posicion=new PVector(random(width), random(height));
    velocidad=new PVector (0, 0);
    aceleracion=new PVector (0, 0);
    gravedad=new PVector (0, 0.02f);
    limite=15;
    masa=random(3, 18);
    resistencia=false;
         r=PApplet.parseInt(random(0,255));
          g=PApplet.parseInt(random(0,255));
          b=PApplet.parseInt(random(0,255));
         // a=int(random(0,255));
         a=255;
    lifespan=255;
    eterna=false;
    decay=2;
    //masa=30;
      //rock=loadShape("rocket.obj");
      rock=loadShape("fish.obj");
      rock.scale(2f*masa);
  }

  public void acelerar(PVector acelerador) {
    PVector a=PVector.div(acelerador, masa);
    aceleracion.add(a);
  }
  public void caer() {
    velocidad.add(gravedad);
  }
  public void resistencia(float coeficiente) {
    resistencia=true; 
    coefroz=coeficiente;
  }
  
  public boolean muerta(){
          if (lifespan<0){return true;}else{return false;}
                
  
  }
  
  public void actualizar() { 
if (eterna==false){lifespan-=decay;}
    velocidad.add(aceleracion);
    if (resistencia) {
      PVector friccion=velocidad.get();

      friccion.normalize();
      friccion.mult(-1*coefroz);
      velocidad.add(friccion);
    }
    velocidad.limit(limite);
    posicion.add(velocidad);
    
    
    
    aceleracion.mult(0);

    if (posicion.x > width/2 ) {
      velocidad.x = velocidad.x*-1;
      posicion.x=width/2;
    } 
    if ( posicion.x <-(width/2)) {
      velocidad.x = velocidad.x*-1;
      posicion.x=-(width/2);
    } 
    if (posicion.y > height/2 ) {
      velocidad.y = velocidad.y*-1;
      posicion.y=height/2;
    }
    if (posicion.y <-(height/2)) {
      velocidad.y = velocidad.y*-1;
      posicion.y=-(height/2);
    }
  if (posicion.z > height/2 ) {
      velocidad.z = velocidad.z*-1;
      posicion.z=height/2;
    }
    if (posicion.z <-(height/2)) {
      velocidad.z = velocidad.z*-1;
      posicion.z=-(height/2);
    }
  }
    public void mostrar() {  
      if (eterna==false){ a=PApplet.parseInt(lifespan);}
                    stroke (r,g,b,a);
                    strokeWeight(1);
                      fill(r,g,b,a);
                   sphere(masa);
                      }
 public void lanzar(){
          actualizar();
          mostrar();
        
  
  
  }         
 
}
class Burbuja extends Particula{
        
  
         Burbuja(PVector origen, PVector vinicial, float masap){

         
         super();
       
         posicion.set(origen);
         masa=masap;
         velocidad=vinicial;
        
        }
            
}


class Astilla extends Particula{
      
        float angular;
         Astilla(PVector origen, PVector vinicial, float masap){

         
         super();
          posicion.set(origen);
         masa=masap;
         velocidad=vinicial;
          angular=0;
     }
    public void mostrar() {
                   if (eterna==false){ a=PApplet.parseInt(lifespan);}
                    stroke (r,g,b,a);
                    strokeWeight(1);
                      fill(r,g,b,a);
                      //angular=atan2(velocidad.y,velocidad.x);
                      angular=velocidad.heading()-(PI/2);
                      //angular=constrain (angular,-0.1,0.1);
                      
                      
                      rectMode (CENTER);
                     pushMatrix();
                     translate(posicion.x, posicion.y, posicion.z);
         //rocket            //
        rotateZ(angular);
        rotateY(PI/2);
                      // rotateZ(angular);
                      // rotateY(angular-(PI/2));
                   //rotateX(angular);
                  //box (masa*3, masa*15, masa*1);
        //tint (r,g,b, opacidad);
        rock.setFill(color(r,g,b,a));
        rock.setStroke((color(r,g,b,a)));
                    shape(rock);
                   //sphere(masa);
                    popMatrix();
                      }
                      
                      
                      public void lanzar(){
                          actualizar();
                          mostrar();
                         
                                     }        
}





class Dardo extends Particula{
      
        float angular;
         Dardo(PVector origen, PVector vinicial, float masap){

         
         super();
          posicion.set(origen);
         masa=masap;
         velocidad=vinicial;
          angular=0;
     }
    public void mostrar() {
                   if (eterna==false){ a=PApplet.parseInt(lifespan);}
                         // stroke (r,g,b,a);
                    //strokeWeight(1);
                      fill(r,g,b,a);
                      //angular=atan2(velocidad.y,velocidad.x);
                      angular=velocidad.heading()+(3*PI/2);
                      //angular=constrain (angular,-0.1,0.1);
                      
                      
                      //rectMode (CENTER);
                     pushMatrix();
                     translate(posicion.x, posicion.y);
                     rotate(angular);
                    triangle (0, 0, masa/2, 2*masa,masa,0);
                    popMatrix();
                      }
                      
                      
                      public void lanzar(){
                          actualizar();
                          mostrar();
                         
                                     }        
}

class Foto extends Particula{
       PImage img;
        float angular;
        int masafoto;
         Foto(PVector origen, PVector vinicial, float masap){
          
         
         super();
          posicion.set(origen);
         masafoto=PApplet.parseInt(masap);
         velocidad=vinicial;
          angular=0;
         img=loadImage("texture.png");
     }
    public void mostrar() {
                   if (eterna==false){ a=PApplet.parseInt(lifespan);}
                          //stroke (r,g,b,a);
                   // strokeWeight(1);
                     tint(r,g,b,a);
                      //angular=atan2(velocidad.y,velocidad.x);
                      angular=velocidad.heading()+(3*PI/2);
                      //angular=constrain (angular,-0.1,0.1);
                      
                      
                      imageMode (CENTER);
                     pushMatrix();
                     translate(posicion.x, posicion.y);
                     rotate(angular);
                     //img.resize(masafoto, masafoto);
                    image(img,0,0);
                    popMatrix();
                      }
                      
                      
                      public void lanzar(){
                          actualizar();
                          mostrar();
                         
                                     }        
}

class Stor {
  
  
        float [] magbrowniano;
        float numeroparticulas, masaparticula;
        int claseparticula, numeroatractores;
        PVector velocidadinicial;
        PVector origen;
        PVector browniano;
        boolean esbrowniano;
        //float magbrowniano;
        ArrayList<Particula> particulas;
        ArrayList<Atractor> atractores;
      
      Stor(float numpart,  int claspart){
                
                numeroparticulas=numpart;
                claseparticula=claspart;
               particulas=new ArrayList<Particula>() ;
               atractores=new ArrayList<Atractor>();
                numeroatractores=3;
                origen=new PVector(random(width), random(height));
                esbrowniano=true;
               magbrowniano=new float[PApplet.parseInt(numeroparticulas)];
                
                for(int i=0; i<numeroparticulas; i++){
                          velocidadinicial=new PVector (random (-15,15),random(-15,15));
                          masaparticula=random (3,10);
                          switch(claseparticula) {
                                                 case 1: 
                                                  particulas.add(new Astilla(origen, velocidadinicial, masaparticula));
                                                 break;
                                                 case 2: 
                                                   particulas.add(new Burbuja(origen, velocidadinicial, masaparticula));
                                                 break;
                                                case 3: 
                                                   particulas.add(new Dardo(origen, velocidadinicial, masaparticula));
                                                 break;
                                                 
                                                 
                                                 
                                                case 4: 
                                                   particulas.add(new Foto(origen, velocidadinicial, masaparticula));
                                                 break;
                                                  }         
                           particulas.get(i).eterna=true;
                                                  }
                                                  
               for (int j=0; j<numeroatractores;j++){
                                                     atractores.add(new Atractor(1));
                                                     float moneda=random(-3,1);
                                                    atractores.get(j).sentido=moneda;
                                                     atractores.get(j).addicon();
                                                     }
                          
      
            //fin constructor Stor
            }

  public void aceleradorparticulas(){
                                for (int i = 0; i < particulas.size(); i++) {
                                Particula p = particulas.get(i);
                                
                                for (int j=0; j<numeroatractores;j++){
                                                                      Atractor a=atractores.get(j);
                                                                      p.acelerar(a.fuerza(p.posicion));
                                                                      if(esbrowniano==true){
                                                                      
                                                                                    browniano=new PVector (0, magbrowniano[j]);
                                                                                    browniano.rotate(p.velocidad.heading());
                                                                                    p.acelerar(browniano);}
                                                                      }
                                
  
                                 }

}
public void dibujaparticulas(){
                         for (int j=0; j<numeroatractores;j++){
                                                                      Atractor a=atractores.get(j);
                                                                      a.visible();
                                                                      
                                                                      }
                        for (int i = 0; i < particulas.size(); i++) {
                                         Particula p = particulas.get(i);
                                         p.masa=magbrowniano[i]*10;
                                          p.caer();
                                          p.lanzar();
                         }
                       

}
                      public void contacto(){
                                  for (int j=0; j<numeroatractores;j++){
                                                                      Atractor a=atractores.get(j);
                                                                      a.contacto();
                                                                      
                                                                      }

                                                                        }
                      public void operador(){
                                  for (int j=0; j<numeroatractores;j++){
                                                                      Atractor a=atractores.get(j);
                                                                      a.operador();
                                                                      
                                                                      }
                      }

                      public void liberador(){
                                  for (int j=0; j<numeroatractores;j++){
                                                                      Atractor a=atractores.get(j);
                                                                      a.liberador();
                                                                      
                                                                      }

                                                                        }                                                                        }
                                          
//fin class Stor
class Storsimple {
  
  
        float magbrowniano;
        float numeroparticulas, masaparticula;
        int claseparticula;
        PVector velocidadinicial;
        PVector origen,centro, radial;
        PVector browniano;
        boolean esbrowniano;
        //float magbrowniano;
        ArrayList<Particula> particulas;
       
      
      Storsimple(float numpart,  int claspart){
                
                numeroparticulas=numpart;
                claseparticula=claspart;
               particulas=new ArrayList<Particula>() ;
               
               
                origen=new PVector(random(width/2), random(height/2));
                //origen=new PVector((width/2)+30, (height/2)+30);
                esbrowniano=true;
               magbrowniano=.8f;
                
                for(int i=0; i<numeroparticulas; i++){
                          //velocidadinicial=new PVector (0,50+random(-10,10));
                          velocidadinicial=new PVector (random (width/2),random(height/2), random(-100, 100));
                          //velocidadinicial=new PVector (random (-15,15),random(-15,15));
                          //velocidadinicial=new PVector (10+random(-3,3),10+random(-3,3));
                          masaparticula=random (3,10);
                          switch(claseparticula) {
                                                 case 1: 
                                                  particulas.add(new Astilla(origen, velocidadinicial, masaparticula));
                                                 break;
                                                 case 2: 
                                                   particulas.add(new Burbuja(origen, velocidadinicial, masaparticula));
                                                 break;
                                                case 3: 
                                                   particulas.add(new Dardo(origen, velocidadinicial, masaparticula));
                                                 break;
                                                 
                                                 
                                                 
                                                case 4: 
                                                   particulas.add(new Foto(origen, velocidadinicial, masaparticula));
                                                 break;
                                                  }         
                           particulas.get(i).eterna=true;
                                                  }
                                                  
      
      }                       
      
            //fin constructor Storsimple
           

  public void aceleradorparticulas(Atractor a){
                                for (int i = 0; i < particulas.size(); i++) {
                                              Particula p = particulas.get(i);
                                               p.acelerar(a.fuerza(p.posicion));
                                               if(esbrowniano==true){
                                                                     browniano=new PVector (0, magbrowniano);
                                                                     browniano.rotate(p.velocidad.heading());
                                                                     p.acelerar(browniano);
                                                                   }
                                               }

}






public void dibujaparticulas(){
                      float factor=0;
                      float factor1x,factor1y;
                      float factorx,factory;
                        for (int i = 0; i < particulas.size(); i++) {
                                         Particula p = particulas.get(i);
                                        
                                          p.caer();
                                          p.lanzar();
                                          
                                          //centro=new PVector(width/2, height/2);
                                          //centro.sub(p.posicion);
                                          //centro.rotate(PI/4);
                                          //noFill();
                                          //stroke (p.r,p.g,p.b,30);
                                          if (p.posicion.x>width/2){factorx=30;factor1x=-30;}else{factorx=-30;factor1x=+30;}
                                           if (p.posicion.y>height/2){factory=30;factor1y=-30;}else{factory=-30;factor1y=+30;}
                                          
                                         // bezier(p.posicion.x, p.posicion.y, p.posicion.x+factor1x, p.posicion.y+factor1y, width/2+factorx, height/2+factory,width/2, height/2);
                                          
                         }
                       

}
}     
//fin class Storsimple
  public void settings() { fullScreen(P3D);  smooth(8); }
}
