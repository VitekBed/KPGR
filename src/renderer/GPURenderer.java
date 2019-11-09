package renderer;

import model3d.Solid;
import transforms.Mat4;

public interface GPURenderer {
    void clear();
    void draw(Solid... solids);
    void setModel(Mat4 model);  //modelová matice - provádí modelovací transformace těles (transpose, scaling, rotation,...) Obvykle se matice aplikujjí v pořadí S-R-T
    void setView(Mat4 view);    //kamera - převádí do soustavy pozorovatele
    void setProjection(Mat4 projection);    //projekční - provádí transformaci do "zorného pole", tedy zkreslení například perspektivou
}
