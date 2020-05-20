/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

/**
 *
 * @author Admin
 */
public interface Validator {
    boolean isValid(String text);
    
    String getMessage();
}
