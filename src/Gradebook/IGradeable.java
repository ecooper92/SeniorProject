/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Gradebook;

import java.util.HashMap;

/**
 *
 * @author Tom
 */
public interface IGradeable {
    public HashMap getGrade();
    public Scale getScale();
    public void setScale(double[] value);
    public void setScale(Scale scale);
    public Weight getWeight();
    public IGradeable getCopy();
}
