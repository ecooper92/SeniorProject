/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Exceptions;

/**
 *
 * @author Tom
 */
public class InvalidScaleException extends RuntimeException{
    public InvalidScaleException(String msg){
        super(msg);
    }
}
