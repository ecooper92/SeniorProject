/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Exceptions;

/**
 *
 * @author Admin
 */
public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String msg){
        super(msg);
    }
}
